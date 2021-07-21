package net.purelic.CGC.maps.elements;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.standard.IntegerArgument;
import cloud.commandframework.arguments.standard.StringArgument;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import net.purelic.CGC.managers.MapManager;
import net.purelic.CGC.maps.CustomMap;
import net.purelic.CGC.maps.MapElement;
import net.purelic.CGC.maps.MapYaml;
import net.purelic.CGC.maps.NamedMapElement;
import net.purelic.CGC.maps.constants.MapElementType;
import net.purelic.CGC.maps.constants.MatchTeam;
import net.purelic.CGC.maps.settings.MapEnumSetting;
import net.purelic.CGC.maps.settings.MapNumberSetting;
import net.purelic.CGC.maps.settings.MapSetting;
import net.purelic.commons.Commons;
import net.purelic.commons.utils.CommandUtils;
import net.purelic.commons.utils.YamlUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Monument extends NamedMapElement {

    private static final String BASE_COMMAND = "monument";
    private static final String OWNER_SETTING = "Owner";
    private static final String MATERIAL_SETTING = "Material";
    private static final String SIZE_SETTING = "Size";
    public static final MapSetting[] SETTINGS = new MapSetting[]{
        new MapEnumSetting<>(
            BASE_COMMAND,
            MatchTeam.class,
            OWNER_SETTING,
            "This team has to defend the monument",
            MatchTeam.BLUE, MatchTeam.RED, MatchTeam.GREEN, MatchTeam.YELLOW,
            MatchTeam.AQUA, MatchTeam.PINK, MatchTeam.WHITE, MatchTeam.GRAY
        ),
        new MapEnumSetting<>(
            BASE_COMMAND,
            Material.class,
            MATERIAL_SETTING,
            "Material of monument",
            Material.EMERALD_BLOCK, Material.GOLD_BLOCK, Material.ENDER_STONE, Material.OBSIDIAN
        ),
        new MapNumberSetting(
            BASE_COMMAND,
            SIZE_SETTING,
            "Number of blocks required to destroy the monument",
            " block",
            1,
            100,
            1,
            true,
            1
        ),
    };

    private Vector min;
    private Vector max;
    private Vector center;

    public Monument(Map<String, Object> yaml) {
        super(MapElementType.MONUMENT, yaml, SETTINGS);
        this.min = this.vectorFromCoords((String) yaml.get("min"));
        this.max = this.vectorFromCoords((String) yaml.get("max"));
        this.center = this.vectorFromCoords((String) yaml.get("center"));
    }

    public MatchTeam getOwner() {
        return this.getEnumSetting(OWNER_SETTING);
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

    @Override
    public String getBookHover() {
        return "\n\n" + this.getName() +
            "\nOwned by " + this.getOwner().getChatColor() + YamlUtils.formatEnumString(this.getOwner().name()) +
            "\n" + YamlUtils.formatCoords(this.getLocation(), true, false);
    }

    @Override
    public void registerAddCommand(String base, MapSetting[] settings) {
        Command.@NonNull Builder<CommandSender> cmdBuilder =
            this.getCommandBuilder(BASE_COMMAND, "add")
                .argument(StringArgument.quoted("name"));

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
                CommandUtils.sendErrorMessage(player, "Please make a selection for the monument region you want to create!");
                return;
            }

            com.sk89q.worldedit.world.World selectionWorld = session.getSelectionWorld();

            if (selectionWorld == null) {
                CommandUtils.sendErrorMessage(player, "Please make a selection for the monument region you want to create!");
                return;
            }

            Monument monument = (Monument) this.create(player.getLocation(), c);

            try {
                boolean success = monument.setRegion(session.getSelection(selectionWorld));

                if (!success) {
                    CommandUtils.sendErrorMessage(player, "Only cuboid selections are currently supported! Change your selection type with //sel cuboid");
                    return;
                }
            } catch (IncompleteRegionException e) {
                CommandUtils.sendErrorMessage(player, "You must complete your selection to create a monument region!");
                return;
            }

            Location location = new Location(player.getWorld(), monument.getCenter().getX(), monument.getCenter().getY(), monument.getCenter().getZ());
            monument.setLocation(location);

            CustomMap map = MapManager.getMap(player.getWorld().getName());
            MapYaml yaml = map.getYaml();

            yaml.addMapElement(this.elementType, monument);
            monument.openBook(player, yaml.getMapElements(this.elementType).size());

            CommandUtils.sendSuccessMessage(player, "You successfully added a " + this.elementType.getName().toLowerCase() + "!");
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
                CommandUtils.sendErrorMessage(player, "Please make a selection for the monument region you want to set!");
                return;
            }

            com.sk89q.worldedit.world.World selectionWorld = session.getSelectionWorld();

            if (selectionWorld == null) {
                CommandUtils.sendErrorMessage(player, "Please make a selection for the monument region you want to create!");
                return;
            }

            Monument monument = (Monument) mapElements.get(id - 1);

            try {
                boolean success = monument.setRegion(session.getSelection(selectionWorld));

                if (!success) {
                    CommandUtils.sendErrorMessage(player, "Only cuboid selections are currently supported! Change your selection type with //sel cuboid");
                    return;
                }
            } catch (IncompleteRegionException e) {
                CommandUtils.sendErrorMessage(player, "You must complete your selection to set a monument region!");
                return;
            }

            monument.openBook(player, yaml.getMapElements(this.elementType).size());

            CommandUtils.sendSuccessMessage(player, "You successfully updated the " + this.elementType.getName().toLowerCase() + "!");
        }));
    }

    private boolean setRegion(Region region) {
        if (!(region instanceof CuboidRegion)) {
            return false;
        }

        this.min = region.getMinimumPoint();
        this.max = region.getMaximumPoint();
        this.center = region.getCenter();
        return true;
    }

    @Override
    public Map<String, Object> toYaml() {
        Map<String, Object> yaml = new HashMap<>();

        yaml.put("location", this.location);
        yaml.put("name", this.name);
        yaml.put("min", this.vectorToCoords(this.min));
        yaml.put("max", this.vectorToCoords(this.max));
        yaml.put("center", this.vectorToCoords(this.center));

        for (MapSetting setting : this.getSettings()) {
            if (!setting.isDefault()) {
                yaml.put(setting.getYamlKey(), setting.getYamlValue());
            }
        }

        return yaml;
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
