package net.purelic.CGC.maps.elements;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.standard.EnumArgument;
import cloud.commandframework.arguments.standard.IntegerArgument;
import cloud.commandframework.arguments.standard.StringArgument;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.CylinderRegion;
import com.sk89q.worldedit.regions.EllipsoidRegion;
import net.purelic.CGC.managers.MapManager;
import net.purelic.CGC.maps.CustomMap;
import net.purelic.CGC.maps.MapElement;
import net.purelic.CGC.maps.MapYaml;
import net.purelic.CGC.maps.NamedMapElement;
import net.purelic.CGC.maps.constants.*;
import net.purelic.CGC.maps.previews.Preview;
import net.purelic.CGC.maps.previews.RegionPreview;
import net.purelic.CGC.maps.settings.MapEnumSetting;
import net.purelic.CGC.maps.settings.MapSetting;
import net.purelic.CGC.maps.settings.MapToggleSetting;
import net.purelic.commons.Commons;
import net.purelic.commons.utils.CommandUtils;
import net.purelic.commons.utils.TaskUtils;
import net.purelic.commons.utils.YamlUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Region extends NamedMapElement {

    private static final String BASE_COMMAND = "region";
    private static final String OWNER_SETTING = "Owner";
    private static final String TELEPORTER_TYPE_SETTING = "Teleporter Type";
    private static final String ENTER_SETTING = "Enter";
    private static final String LEAVE_SETTING = "Leave";
    private static final String BREAK_BLOCKS_SETTING = "Break Blocks";
    private static final String PLACE_BLOCKS_SETTING = "Place Blocks";
    private static final String PVP_SETTING = "PvP";
    private static final String DAMAGE_SETTING = "Damage";
    private static final String INSTANT_DEATH_SETTING = "Instant Death";
    public static final MapSetting[] SETTINGS = new MapSetting[]{
        new MapEnumSetting<>(
            BASE_COMMAND,
            MatchTeam.class,
            OWNER_SETTING,
            "This team can bypass any region filters"
        ),
        new MapEnumSetting<>(
            BASE_COMMAND,
            TeleporterType.class,
            TELEPORTER_TYPE_SETTING,
            "What block the teleporter should be set to"
        ),
        new MapToggleSetting(
            BASE_COMMAND,
            ENTER_SETTING,
            "Can enter the region",
            true
        ),
        new MapToggleSetting(
            BASE_COMMAND,
            LEAVE_SETTING,
            "Can leave the region",
            true
        ),
        new MapToggleSetting(
            BASE_COMMAND,
            BREAK_BLOCKS_SETTING,
            "Can break blocks in region",
            true
        ),
        new MapToggleSetting(
            BASE_COMMAND,
            PLACE_BLOCKS_SETTING,
            "Can place blocks in region",
            true
        ),
        new MapToggleSetting(
            BASE_COMMAND,
            PVP_SETTING,
            "Can take PvP damage in region",
            true
        ),
        new MapToggleSetting(
            BASE_COMMAND,
            DAMAGE_SETTING,
            "Can take any damage in region",
            true
        ),
        new MapToggleSetting(
            BASE_COMMAND,
            INSTANT_DEATH_SETTING,
            "Die instantly on region enter",
            false
        ),
    };

    private RegionType type;
    private Vector min;
    private Vector max;
    private Vector center;
    private Vector radius;
    private int minY;
    private int maxY;
    private String destination;
    private DependencyType dependencyType;
    private int dependencyId;

    public Region(Map<String, Object> yaml) {
        super(MapElementType.REGION, yaml, SETTINGS);

        String rawRegionType = (String) yaml.get("type");
        if (rawRegionType != null) {
            this.type = RegionType.valueOf(rawRegionType);
            this.min = this.vectorFromCoords((String) yaml.get("min"));
            this.max = this.vectorFromCoords((String) yaml.get("max"));
            this.center = this.vectorFromCoords((String) yaml.get("center"));
            this.radius = this.vectorFromCoords((String) yaml.get("radius"));
            this.minY = (int) yaml.get("min_y");
            this.maxY = (int) yaml.get("max_y");
            this.destination = (String) yaml.get("destination");

            String rawDependencyType = (String) yaml.get("dependency_type");
            if (rawDependencyType != null) {
                this.dependencyType = DependencyType.valueOf(rawDependencyType);
                this.dependencyId = (int) yaml.get("dependency_id");
            }
        }
    }

    public MatchTeam getOwner() {
        return this.getEnumSetting(OWNER_SETTING);
    }

    public RegionType getType() {
        return this.type;
    }

    public Vector getMin() {
        return this.min;
    }

    public Vector getMax() {
        return this.max;
    }

    public Vector getCenter() {
        return this.center;
    }

    public Vector getRadius() {
        return this.radius;
    }

    public int getMinY() {
        return this.minY;
    }

    public int getMaxY() {
        return this.maxY;
    }

    @Override
    public String getBookHover() {
        return "\n\n" + this.getName() +
            "\nType: " + YamlUtils.formatEnumString(this.type.name()) +
            "\nOwned by " + this.getOwner().getChatColor() + YamlUtils.formatEnumString(this.getOwner().name()) +
            "\nDestination: " + (this.destination == null ? "None" : YamlUtils.formatCoords(this.destination, true, false)) +
            "\nDependency: " + (this.dependencyType == null ? "None" : YamlUtils.formatEnumString(this.dependencyType.name())) +
            "\nDependency ID: " + (this.dependencyType == null ? "-" : this.dependencyId) +
            "\n" + YamlUtils.formatCoords(this.getLocation(), true, false);
    }

    @Override
    public Preview getPreview(Player player) {
        return new RegionPreview(
            player,
            this.location,
            this
        );
    }

    @Override
    public void registerAddCommand(String base, MapSetting[] settings) {
        Command.@NonNull Builder<CommandSender> cmdBuilder =
            this.getCommandBuilder(BASE_COMMAND, "add")
                .argument(StringArgument.quoted("name"));

        for (MapSetting setting : SETTINGS) {
            cmdBuilder = cmdBuilder.argument(setting.getArgument());
        }

        this.registerCommand(cmdBuilder.handler(c -> {
            Player player = (Player) c.getSender();
            World world = player.getWorld();

            if (world == Commons.getLobby()) {
                CommandUtils.sendErrorMessage(player, "Please join the map you want to edit the config of!");
                return;
            }

            if (((String) c.get("name")).length() > 24) {
                CommandUtils.sendErrorMessage(player, this.elementType.getName() + " names can only be 24 characters or less!");
                return;
            }

            LocalSession session = WorldEdit.getInstance()
                .getSessionManager()
                .findByName(player.getName());

            if (session == null) {
                CommandUtils.sendErrorMessage(player, "Please make a selection for the region you want to create!");
                return;
            }

            com.sk89q.worldedit.world.World selectionWorld = session.getSelectionWorld();

            if (selectionWorld == null) {
                CommandUtils.sendErrorMessage(player, "Please make a selection for the region you want to create!");
                return;
            }

            Region region = (Region) this.create(player.getLocation(), c);

            try {
                boolean success = region.setRegion(session.getSelection(selectionWorld));

                if (!success) {
                    CommandUtils.sendErrorMessage(player, "This selection type is not currently supported! Change your selection type with //sel <type>");
                    return;
                }
            } catch (IncompleteRegionException e) {
                CommandUtils.sendErrorMessage(player, "You must complete your selection to create a region!");
                return;
            }

            Location location = new Location(player.getWorld(), region.getCenter().getX(), region.getCenter().getY(), region.getCenter().getZ());
            region.setLocation(location);

            CustomMap map = MapManager.getMap(player.getWorld().getName());
            MapYaml yaml = map.getYaml();

            yaml.addMapElement(this.elementType, region);
            region.openBook(player, yaml.getMapElements(this.elementType).size());

            CommandUtils.sendSuccessMessage(player, "You successfully added a " + this.elementType.getName().toLowerCase() + "!");
            TaskUtils.run(() -> region.preview(player));
        }));
    }

    public void registerSetCommand() {
        Command.@NonNull Builder<CommandSender> cmdBuilder =
            this.getCommandBuilder(BASE_COMMAND, "set")
                .argument(IntegerArgument.<CommandSender>newBuilder("id").withMin(1));

        this.registerCommand(cmdBuilder.handler(c -> {
            Player player = (Player) c.getSender();
            World world = player.getWorld();

            if (world == Commons.getLobby()) {
                CommandUtils.sendErrorMessage(player, "Please join the map you want to edit the config of!");
                return;
            }

            int id = c.get("id");

            MapYaml yaml = MapManager.getMap(player.getWorld().getName()).getYaml();
            List<MapElement> mapElements = yaml.getMapElements(this.elementType);

            if (id > mapElements.size()) {
                CommandUtils.sendErrorMessage(player, "Invalid " + this.elementType.getLowerName() + " id!");
                return;
            }

            LocalSession session = WorldEdit.getInstance()
                .getSessionManager()
                .findByName(player.getName());

            if (session == null) {
                CommandUtils.sendErrorMessage(player, "Please make a selection for the region you want to set!");
                return;
            }

            com.sk89q.worldedit.world.World selectionWorld = session.getSelectionWorld();

            if (selectionWorld == null) {
                CommandUtils.sendErrorMessage(player, "Please make a selection for the region you want to create!");
                return;
            }

            Region region = (Region) mapElements.get(id - 1);

            try {
                boolean success = region.setRegion(session.getSelection(selectionWorld));

                if (!success) {
                    CommandUtils.sendErrorMessage(player, "This selection type is not currently supported! Change your selection type with //sel <type>");
                    return;
                }
            } catch (IncompleteRegionException e) {
                CommandUtils.sendErrorMessage(player, "You must complete your selection to set a region!");
                return;
            }

            region.openBook(player, yaml.getMapElements(this.elementType).size());

            CommandUtils.sendSuccessMessage(player, "You successfully updated the " + this.elementType.getName().toLowerCase() + "!");
            TaskUtils.run(() -> region.preview(player));
        }));
    }

    public void registerDestinationCommand() {
        Command.@NonNull Builder<CommandSender> cmdBuilder =
            this.getCommandBuilder(BASE_COMMAND, "destination")
                .argument(IntegerArgument.<CommandSender>newBuilder("id").withMin(1));

        this.registerCommand(cmdBuilder.handler(c -> {
            Player player = (Player) c.getSender();
            World world = player.getWorld();

            if (world == Commons.getLobby()) {
                CommandUtils.sendErrorMessage(player, "Please join the map you want to edit the config of!");
                return;
            }

            int id = c.get("id");

            MapYaml yaml = MapManager.getMap(player.getWorld().getName()).getYaml();
            List<MapElement> mapElements = yaml.getMapElements(this.elementType);

            if (id > mapElements.size()) {
                CommandUtils.sendErrorMessage(player, "Invalid " + this.elementType.getLowerName() + " id!");
                return;
            }

            Region region = (Region) mapElements.get(id - 1);
            region.setDestination(player.getLocation());

            CommandUtils.sendSuccessMessage(player, "You successfully set the teleport destination for " + this.elementType.getName().toLowerCase() + "!");
        }));
    }

    public void registerClearDestinationCommand() {
        Command.@NonNull Builder<CommandSender> cmdBuilder =
            this.getCommandBuilder(BASE_COMMAND, "cleardestination")
                .argument(IntegerArgument.<CommandSender>newBuilder("id").withMin(1));

        this.registerCommand(cmdBuilder.handler(c -> {
            Player player = (Player) c.getSender();
            World world = player.getWorld();

            if (world == Commons.getLobby()) {
                CommandUtils.sendErrorMessage(player, "Please join the map you want to edit the config of!");
                return;
            }

            int id = c.get("id");

            MapYaml yaml = MapManager.getMap(player.getWorld().getName()).getYaml();
            List<MapElement> mapElements = yaml.getMapElements(this.elementType);

            if (id > mapElements.size()) {
                CommandUtils.sendErrorMessage(player, "Invalid " + this.elementType.getLowerName() + " id!");
                return;
            }

            Region region = (Region) mapElements.get(id - 1);
            region.clearDestination();

            CommandUtils.sendSuccessMessage(player, "You successfully removed the teleport destination from " + this.elementType.getName().toLowerCase() + "!");
        }));
    }

    public void registerDependCommand() {
        Command.@NonNull Builder<CommandSender> cmdBuilder =
            this.getCommandBuilder(BASE_COMMAND, "depend")
                .argument(IntegerArgument.<CommandSender>newBuilder("region id").withMin(1))
                .argument(EnumArgument.of(DependencyType.class, "dependency type"))
                .argument(IntegerArgument.<CommandSender>newBuilder("dependency id").withMin(1));

        this.registerCommand(cmdBuilder.handler(c -> {
            Player player = (Player) c.getSender();
            World world = player.getWorld();

            if (world == Commons.getLobby()) {
                CommandUtils.sendErrorMessage(player, "Please join the map you want to edit the config of!");
                return;
            }

            int id = c.get("region id");

            MapYaml yaml = MapManager.getMap(player.getWorld().getName()).getYaml();
            List<MapElement> mapElements = yaml.getMapElements(this.elementType);

            if (id > mapElements.size()) {
                CommandUtils.sendErrorMessage(player, "Invalid " + this.elementType.getLowerName() + " id!");
                return;
            }

            DependencyType dependencyType = c.get("dependency type");
            int dependencyId = c.get("dependency id");

            MapElementType elementType = dependencyType.getElementType();
            List<MapElement> dependencies = yaml.getMapElements(elementType);

            if (id > dependencies.size()) {
                CommandUtils.sendErrorMessage(player, "Invalid " + elementType.getLowerName() + " id!");
                return;
            }

            Region region = (Region) mapElements.get(id - 1);
            region.setDependency(dependencyType, dependencyId);

            CommandUtils.sendSuccessMessage(player, "You successfully set the dependency for " + this.elementType.getName().toLowerCase() + "!");
        }));
    }

    public void registerClearDependencyCommand() {
        Command.@NonNull Builder<CommandSender> cmdBuilder =
            this.getCommandBuilder(BASE_COMMAND, "cleardependency")
                .argument(IntegerArgument.<CommandSender>newBuilder("id").withMin(1));

        this.registerCommand(cmdBuilder.handler(c -> {
            Player player = (Player) c.getSender();
            World world = player.getWorld();

            if (world == Commons.getLobby()) {
                CommandUtils.sendErrorMessage(player, "Please join the map you want to edit the config of!");
                return;
            }

            int id = c.get("id");

            MapYaml yaml = MapManager.getMap(player.getWorld().getName()).getYaml();
            List<MapElement> mapElements = yaml.getMapElements(this.elementType);

            if (id > mapElements.size()) {
                CommandUtils.sendErrorMessage(player, "Invalid " + this.elementType.getLowerName() + " id!");
                return;
            }

            LocalSession session = WorldEdit.getInstance()
                .getSessionManager()
                .findByName(player.getName());

            if (session == null) {
                CommandUtils.sendErrorMessage(player, "Please make a selection for the region you want to set!");
                return;
            }

            Region region = (Region) mapElements.get(id - 1);
            region.clearDependency();

            CommandUtils.sendSuccessMessage(player, "You successfully removed the dependency from " + this.elementType.getName().toLowerCase() + "!");
        }));
    }

    @Override
    public Map<String, Object> toYaml() {
        Map<String, Object> yaml = new HashMap<>();

        yaml.put("location", this.location);
        yaml.put("name", this.name);
        yaml.put("type", this.type.name());
        yaml.put("min", this.vectorToCoords(this.min));
        yaml.put("max", this.vectorToCoords(this.max));
        yaml.put("center", this.vectorToCoords(this.center));
        yaml.put("radius", this.vectorToCoords(this.radius));
        yaml.put("min_y", this.minY);
        yaml.put("max_y", this.maxY);

        if (this.destination != null) {
            yaml.put("destination", this.destination);
        }

        if (this.dependencyType != null && this.dependencyId >= 0) {
            yaml.put("dependency_type", this.dependencyType.name());
            yaml.put("dependency_id", this.dependencyId);
        }

        for (MapSetting setting : this.getSettings()) {
            if (!setting.isDefault()) {
                yaml.put(setting.getYamlKey(), setting.getYamlValue());
            }
        }

        return yaml;
    }

    private boolean setRegion(com.sk89q.worldedit.regions.Region region) {
        RegionType type = null;

        if (region instanceof CuboidRegion) {
            type = RegionType.CUBOID;
        } else if (region instanceof CylinderRegion) {
            type = RegionType.CYLINDER;
        } else if (region instanceof EllipsoidRegion) {
            type = RegionType.ELLIPSOID;
        }

        if (type == null) return false;

        this.type = type;
        this.min = region.getMinimumPoint();
        this.max = region.getMaximumPoint();
        this.center = region.getCenter();
        this.minY = this.min.getBlockY();
        this.maxY = this.max.getBlockY();

        if (type == RegionType.ELLIPSOID) {
            this.radius = ((EllipsoidRegion) region).getRadius();
        } else if (type == RegionType.CYLINDER) {
            this.radius = ((CylinderRegion) region).getRadius().toVector();
        } else {
            this.radius = new Vector(0, 0, 0);
        }

        return true;
    }

    private void setDestination(Location destination) {
        this.destination = YamlUtils.locationToCoords(destination, true);
    }

    private void clearDestination() {
        this.destination = null;
    }

    private void setDependency(DependencyType type, int id) {
        this.dependencyType = type;
        this.dependencyId = id;
    }

    private void clearDependency() {
        this.dependencyType = null;
        this.dependencyId = -1;
    }

    private String vectorToCoords(Vector vector) {
        return vector.getX() + "," + vector.getY() + "," + vector.getZ();
    }

    private Vector vectorFromCoords(String coords) {
        if (coords == null || coords.isEmpty()) return null;
        String[] args = coords.split(",");
        return new Vector(Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]));
    }

}
