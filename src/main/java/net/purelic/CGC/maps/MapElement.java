package net.purelic.CGC.maps;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.standard.IntegerArgument;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.context.CommandContext;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.purelic.CGC.managers.MapManager;
import net.purelic.CGC.maps.constants.MapElementType;
import net.purelic.CGC.maps.previews.Preview;
import net.purelic.CGC.maps.settings.MapEnumSetting;
import net.purelic.CGC.maps.settings.MapNumberSetting;
import net.purelic.CGC.maps.settings.MapSetting;
import net.purelic.CGC.maps.settings.MapToggleSetting;
import net.purelic.commons.Commons;
import net.purelic.commons.commands.parsers.Permission;
import net.purelic.commons.utils.CommandUtils;
import net.purelic.commons.utils.TaskUtils;
import net.purelic.commons.utils.YamlUtils;
import net.purelic.commons.utils.book.BookBuilder;
import net.purelic.commons.utils.book.PageBuilder;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.*;

@SuppressWarnings("unchecked")
public abstract class MapElement {

    protected boolean previewing = false;
    private Preview preview;

    protected final MapElementType elementType;
    protected final Map<String, MapSetting> settings;
    protected String location;

    public MapElement(MapElementType elementType, Map<String, Object> yaml, MapSetting... settings) {
        this.elementType = elementType;
        this.location = (String) yaml.get("location");
        this.settings = new LinkedHashMap<>(); // linked map keeps the settings in order

        for (MapSetting setting : settings) {
            MapSetting copy = setting.copy();
            copy.setCurrent(yaml.getOrDefault(setting.getYamlKey(), setting.getDefaultValue()));
            this.settings.put(setting.getSettingName(), copy);
        }
    }

    public MapElement(MapElementType elementType) {
        this.elementType = elementType;
        this.location = null;
        this.settings = new HashMap<>();
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        this.location = YamlUtils.locationToCoords(location, false);
    }

    public boolean isPreviewing() {
        return this.previewing;
    }

    public String getBookName() {
        return YamlUtils.formatCoords(this.getLocation(), true, true);
    }

    public String getBookHover() {
        return "";
    }

    public MapSetting getSetting(String name) {
        return this.settings.get(name);
    }

    public <E extends Enum<E>> E getEnumSetting(String name) {
        return ((MapEnumSetting<E>) this.getSetting(name)).getCurrentValue();
    }

    public int getNumberSetting(String name) {
        return ((MapNumberSetting) this.getSetting(name)).getCurrentValue();
    }

    public boolean getToggleSetting(String name) {
        return ((MapToggleSetting) this.getSetting(name)).getCurrentValue();
    }

    public void openBook(Player player, int id) {
        if (this instanceof GenericMapElement) {
            id = -1;
        }

        List<BaseComponent[]> pages = new ArrayList<>();
        BookBuilder bookBuilder = new BookBuilder();
        PageBuilder pageBuilder = this.getPageHeader(id);

        int count = 0;

        for (MapSetting setting : this.settings.values()) {
            // every 4 settings we need a new page
            if (count % 4 == 0 && count > 0) {
                pages.add(pageBuilder.build());
                pageBuilder = this.getPageHeader(id);
            }

            pageBuilder
                .add(setting.getName()).newLine()
                .add(setting.getValue(id)).newLine().newLine();

            count++;
        }

        pages.add(pageBuilder.build());
        bookBuilder.pages(pages).open(player);
    }

    private PageBuilder getPageHeader(int id) {
        if (id == -1) {
            return new PageBuilder()
                .add(new ComponentBuilder("⬅ ")
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Back").create()))
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/edit"))
                    .bold(true)
                    .create())
                .add(new ComponentBuilder(this.elementType.getName() + "s").create())
                .newLine().newLine();
        }

        return new PageBuilder()
            .add(new ComponentBuilder("⬅ ")
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Back").create()))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + this.elementType.getListCommand()))
                .bold(true)
                .create())
            .add(new ComponentBuilder(this.elementType.getName() + " #" + id)
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(this.location + "\n" + this.elementType.getBaseCommand(true) + " preview " + id).create()))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, this.elementType.getBaseCommand(true) + " preview " + id))
                .create()).newLine().newLine();
    }

    protected Preview getPreview(Player player) {
        return null;
    }

    private void preview(Player player) {
        this.preview = this.getPreview(player);

        if (this.previewing || this.preview == null) return;

        this.previewing = true;

        TaskUtils.run(this.preview);
        TaskUtils.runLater(this::destroyPreview, 300); // 15 seconds

        CommandUtils.sendAlertMessage(player, "This preview will last for 15 seconds and shows you what " +
            "this " + this.elementType.getLowerName() + " will look like in-game");
    }

    public void destroyPreview() {
        this.previewing = false;

        if (this.preview == null) return;

        TaskUtils.run(this.preview.destroy());
        this.preview = null;
    }

    public MapElement create(Location location, @NonNull CommandContext<CommandSender> c) {
        Map<String, Object> yaml = new HashMap<>();

        // Some objects are offset (e.g. spawners get placed below the player)
        location.add(0, this.elementType.getOffsetY(), 0);

        yaml.put("location", YamlUtils.locationToCoords(location, false));

        if (this instanceof NamedMapElement) {
            yaml.put("name", c.get("name"));
        }

        for (MapSetting setting : this.settings.values()) {
            yaml.put(setting.getYamlKey(), c.get(setting.getYamlKey().replaceAll("_", " ")));
        }

        return this.elementType.create(yaml);
    }

    public Map<String, Object> toYaml() {
        Map<String, Object> yaml = new HashMap<>();

        if (!(this instanceof GenericMapElement)) {
            yaml.put("location", this.location);
        }

        if (this instanceof NamedMapElement) {
            String name = ((NamedMapElement) this).getName();
            yaml.put("name", name);
        }

        for (MapSetting setting : this.settings.values()) {
            if (!setting.isDefault()) {
                yaml.put(setting.getYamlKey(), setting.getYamlValue());
            }
        }

        return yaml;
    }

    public void registerCommands(String base, MapSetting[] settings) {
        this.registerListCommand(base);

        if (this instanceof GenericMapElement) {
            return;
        }

        this.registerAddCommand(base, settings);
        this.registerPreviewCommand(base);
        this.registerRemoveCommand(base);
        this.registerReorderCommand(base);

        if (!(this instanceof NestedMapElement)) {
            this.registerEditCommand(base);
            this.registerCopyCommand(base);
        }

        if (this instanceof NamedMapElement) {
            ((NamedMapElement) this).registerRenameCommand(base);
        }
    }

    protected void registerAddCommand(String base, MapSetting[] settings) {
        Command.@NonNull Builder<CommandSender> cmdBuilder = this.getCommandBuilder(base, "add");

        if (this instanceof NamedMapElement) {
            cmdBuilder = cmdBuilder.argument(StringArgument.quoted("name"));
        }

        for (MapSetting setting : settings) {
            cmdBuilder = cmdBuilder.argument(setting.getArgument());
        }

        this.registerCommand(cmdBuilder.handler(c -> {
            Player player = (Player) c.getSender();
            World world = player.getWorld();

            if (world == Commons.getLobby()) {
                CommandUtils.sendErrorMessage(player, "Please join the map you want to edit the config of!");
                return;
            }

            if (this instanceof NamedMapElement) {
                if (((String) c.get("name")).length() > 16) {
                    CommandUtils.sendErrorMessage(player, this.elementType.getName() + " names can only be 16 characters or less!");
                    return;
                }
            }

            Location pLocation = player.getLocation();
            Location location = new Location(world, pLocation.getBlockX(), pLocation.getBlockY(), pLocation.getBlockZ());

            CustomMap map = MapManager.getMap(player.getWorld().getName());
            MapYaml yaml = map.getYaml();

            MapElement mapElement = this.create(location, c);
            yaml.addMapElement(this.elementType, mapElement);
            mapElement.openBook(player, yaml.getMapElements(this.elementType).size());

            CommandUtils.sendSuccessMessage(player, "You successfully added a " + this.elementType.getName().toLowerCase() + "!");
            mapElement.preview(player);
        }));
    }

    protected void registerRemoveCommand(String base) {
        Command.@NonNull Builder<CommandSender> cmdBuilder = this.getCommandBuilder(base,"remove")
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

            yaml.removeMapElement(this.elementType, id - 1);
            CommandUtils.sendSuccessMessage(player, "You successfully removed the " + this.elementType.getLowerName() + "!");
        }));
    }

    protected void registerPreviewCommand(String base) {
        Command.@NonNull Builder<CommandSender> cmdBuilder = this.getCommandBuilder(base, "preview")
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

            yaml.getMapElements(this.elementType).get(id - 1).preview(player);
        }));
    }

    private void registerEditCommand(String base) {
        Command.@NonNull Builder<CommandSender> cmdBuilder = this.getCommandBuilder(base, "edit")
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

            yaml.getMapElements(this.elementType).get(id - 1).openBook(player, id);
        }));
    }

    private void registerReorderCommand(String base) {
        Command.@NonNull Builder<CommandSender> cmdBuilder = this.getCommandBuilder(base, "reorder")
            .argument(IntegerArgument.<CommandSender>newBuilder("id").withMin(1))
            .argument(IntegerArgument.<CommandSender>newBuilder("position").withMin(1));

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

            int position = c.get("position");

            if (position > mapElements.size()) {
                CommandUtils.sendErrorMessage(player, "Invalid position number!");
                return;
            }

            mapElements.add(position - 1, mapElements.remove(id - 1));
            yaml.updateYaml();
            yaml.getMapElements(this.elementType).get(id - 1).openBook(player, position);

            CommandUtils.sendSuccessMessage(player, "Successfully reordered the " + this.elementType.getLowerName() + "!");
        }));
    }

    private void registerCopyCommand(String base) {
        Command.@NonNull Builder<CommandSender> cmdBuilder = this.getCommandBuilder(base, "copy")
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

            yaml.getMapElements(this.elementType).get(id - 1).openBook(player, id);

            MapElement toCopy = yaml.getMapElements(this.elementType).get(id - 1);
            MapElement mapElement = this.elementType.create(toCopy.toYaml());
            yaml.addMapElement(this.elementType, mapElement);

            mapElement.openBook(player, mapElements.size());
            CommandUtils.sendSuccessMessage(player, "You successfully added a jump pad!");
            mapElement.preview(player);
        }));
    }

    protected void registerListCommand(String base) {
        Command.@NonNull Builder<CommandSender> cmdBuilder = Commons.getPlugin().getCommandManager()
            .commandBuilder(base + "s")
            .senderType(Player.class)
            .permission(Permission.isMapDev(true));

        this.registerCommand(cmdBuilder.handler(c -> {
            Player player = (Player) c.getSender();
            World world = player.getWorld();

            if (world == Commons.getLobby()) {
                CommandUtils.sendErrorMessage(player, "Please join the map you want to edit the config of!");
                return;
            }

            MapYaml yaml = MapManager.getMap(player.getWorld().getName()).getYaml();
            List<MapElement> mapElements = yaml.getMapElements(this.elementType);

            int total = mapElements.size();

            if (total == 0) {
                if (this instanceof NamedMapElement) {
                    CommandUtils.sendErrorMessage(player, "You haven't created any " + this.elementType.getLowerName() + "s yet! " +
                        "Use " + this.elementType.getBaseCommand(true) + " add <name>");
                } else {
                    this.openEmptyBook(player);
                }
                return;
            }

            List<BaseComponent[]> pages = new ArrayList<>();
            PageBuilder page = new PageBuilder();

            int i = 0;

            for (MapElement mapElement : mapElements) {
                if (i % 10 == 0) {
                    if (i != 0) pages.add(page.build());
                    page = new PageBuilder();
                    page
                        .add(new ComponentBuilder("⬅ ")
                            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Back").create()))
                            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/edit"))
                            .bold(true)
                            .create())
                        .add(total + " " + this.elementType.getName() + (total == 1 ? "" : "s")).newLine().newLine();
                }

                int number = i + 1;
                String name = mapElement.getBookName();
                String command = this.elementType.getBaseCommand(true) + " edit " + number;
                String hover = "Edit " + this.elementType.getName() + "\n" + command + mapElement.getBookHover();

                page.newLine()
                    .add(
                        new ComponentBuilder(number + ". ")
                            .append(name.length() >= 14 ? name.substring(0, 13) + ".." : name)
                            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create()))
                            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command))
                            .create()
                    )
                    .add(
                        new ComponentBuilder("  ")
                            .append("✗")
                            .color(ChatColor.DARK_RED)
                            .bold(true)
                            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("DELETE").color(ChatColor.RED).create()))
                            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, this.elementType.getBaseCommand(true) + " remove " + number))
                            .create()
                    );

                i++;
            }

            pages.add(page.build());

            new BookBuilder().pages(pages).open(player);
        }));
    }

    private void openEmptyBook(Player player) {
        PageBuilder page = new PageBuilder()
            .add(new ComponentBuilder("⬅ ")
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Back").create()))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/edit"))
                .bold(true)
                .create())
            .add("0 total " + this.elementType.getName() + "s").newLine().newLine()
            .add(new ComponentBuilder("+ ADD")
                .color(ChatColor.DARK_GREEN)
                .bold(true)
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Add " + this.elementType.getName() + "\n" + this.elementType.getBaseCommand(true) + " add").create()))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, this.elementType.getBaseCommand(true) + " add"))
                .create()).newLine();

        new BookBuilder().pages(page.build()).open(player);
    }

    protected Command.@NonNull Builder<CommandSender> getCommandBuilder(String base, String literal) {
        return Commons.getPlugin().getCommandManager()
            .commandBuilder(base)
            .senderType(Player.class)
            .permission(Permission.isMapDev(true))
            .literal(literal);
    }

    protected void registerCommand(Command.@NonNull Builder<CommandSender> commandBuilder) {
        Commons.getPlugin().getCommandManager().command(commandBuilder.build());
    }

}
