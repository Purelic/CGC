package net.purelic.CGC.maps;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.standard.EnumArgument;
import cloud.commandframework.arguments.standard.IntegerArgument;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.purelic.CGC.managers.MapManager;
import net.purelic.CGC.maps.constants.MapElementType;
import net.purelic.CGC.maps.settings.MapSetting;
import net.purelic.commons.Commons;
import net.purelic.commons.commands.parsers.Permission;
import net.purelic.commons.utils.CommandUtils;
import net.purelic.commons.utils.YamlUtils;
import net.purelic.commons.utils.book.BookBuilder;
import net.purelic.commons.utils.book.PageBuilder;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.*;

public abstract class NestedMapElement<E extends Enum<E>> extends MapElement {

    private final Class<E> enumClass;
    private final String argument;
    private final boolean coords;
    private final Map<E, List<String>> locations;

    @SuppressWarnings("unchecked")
    public NestedMapElement(MapElementType objectType, Class<E> enumClass, String argument, boolean coords, Map<String, Object> yaml) {
        super(objectType);
        this.enumClass = enumClass;
        this.argument = argument;
        this.coords = coords;
        this.locations = new HashMap<>();

        for (E enumValue : enumClass.getEnumConstants()) {
            List<String> locations = (List<String>) yaml.getOrDefault(enumValue.name().toLowerCase(), new ArrayList<>());
            this.locations.put(enumValue, locations);
        }
    }

    public List<String> getLocations(E enumValue) {
        return this.locations.get(enumValue);
    }

    public void addLocation(E enumValue, Location location) {
        this.addLocation(enumValue,
            this.coords ? YamlUtils.locationToCoords(location, false) : YamlUtils.locationToString(location));
    }

    private void addLocation(E enumValue, String location) {
        this.locations.putIfAbsent(enumValue, new ArrayList<>());
        this.locations.get(enumValue).add(location);
    }

    public void offsetLocations(World world, Vector offset) {
        for (E enumValue : this.enumClass.getEnumConstants()) {
            List<String> locations = this.getLocations(enumValue);

            if (locations == null || locations.isEmpty()) continue;

            List<String> newLocations = new ArrayList<>();

            for (String coords : locations) {
                Location location = YamlUtils.getLocationFromCoords(world, coords);
                location.add(offset);
                newLocations.add(this.coords ?
                    YamlUtils.locationToCoords(location, false) : YamlUtils.locationToString(location));
            }

            this.locations.put(enumValue, newLocations);
        }
    }

    @Override
    public Map<String, Object> toYaml() {
        Map<String, Object> data = new HashMap<>();

        for (Map.Entry<E, List<String>> entry : this.locations.entrySet()) {
            if (!entry.getValue().isEmpty()) {
                data.put(entry.getKey().name().toLowerCase(), entry.getValue());
            }
        }

        return data;
    }

    @Override
    public void registerListCommand(String base) {
        Command.@NonNull Builder<CommandSender> cmdBuilder = Commons.getPlugin().getCommandManager()
            .commandBuilder(this.elementType.getBaseCommand(false) + "s")
            .senderType(Player.class)
            .permission(Permission.isMapDev(true))
            .argument(EnumArgument.optional(this.enumClass, this.argument));

        this.registerCommand(cmdBuilder.handler(c -> {
            Player player = (Player) c.getSender();
            Optional<E> optional = c.getOptional(this.argument);

            if (player.getWorld() == Commons.getLobby()) {
                CommandUtils.sendErrorMessage(player, "Please join the map you want to edit the config of!");
                return;
            }

            CustomMap map = MapManager.getMap(player.getWorld().getName());
            MapYaml yaml = map.getYaml();

            if (this.elementType == MapElementType.CHEST) {
                yaml.cleanChests();
            }

            if (optional.isPresent()) {
                this.openBook(player, optional.get(), yaml);
            } else {
                PageBuilder pageBuilder = new PageBuilder()
                    .add(new ComponentBuilder("⬅ ")
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Back").create()))
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/edit"))
                        .bold(true)
                        .create())
                    .add(this.elementType.getName() + "s").newLine().newLine();

                int i = 1;
                for (E enumValue : this.enumClass.getEnumConstants()) {
                    pageBuilder.add(this.getLocationLine(i, yaml, enumValue)).newLine();
                    i++;
                }

                new BookBuilder().pages(pageBuilder).open(player);
            }
        }));
    }

    @Override
    public void registerAddCommand(String base, MapSetting[] settings) {
        Command.@NonNull Builder<CommandSender> cmdBuilder = this.getCommandBuilder(base, "add")
            .argument(EnumArgument.of(this.enumClass, this.argument));

        this.registerCommand(cmdBuilder.handler(c -> {
            Player player = (Player) c.getSender();
            Location location = player.getLocation();
            World world = player.getWorld();

            if (world == Commons.getLobby()) {
                CommandUtils.sendErrorMessage(player, "Please join the map you want to edit the config of!");
                return;
            }

            MapYaml yaml = MapManager.getMap(player.getWorld().getName()).getYaml();
            MapElement mapElement = this.create(location, c);
            yaml.addMapElement(this.elementType, mapElement);
            E enumValue = c.get(this.argument);
            yaml.<E>getNestedMapElement(this.elementType).addLocation(enumValue, location);
            this.openBook(player, enumValue, yaml);
            CommandUtils.sendSuccessMessage(player, "You successfully added a " +
                YamlUtils.formatEnumString(enumValue.name()) + " " + this.elementType.getLowerName() + "!");
        }));
    }

    @Override
    public void registerRemoveCommand(String base) {
        Command.@NonNull Builder<CommandSender> cmdBuilder = this.getCommandBuilder(base, "remove")
            .argument(EnumArgument.of(this.enumClass, this.argument))
            .argument(IntegerArgument.<CommandSender>newBuilder("id").withMin(1));

        this.registerCommand(cmdBuilder.handler(c -> {
            Player player = (Player) c.getSender();

            if (player.getWorld() == Commons.getLobby()) {
                CommandUtils.sendErrorMessage(player, "Please join the map you want to edit the config of!");
                return;
            }

            int id = c.get("id");

            E enumValue = c.get(this.argument);
            MapYaml yaml = MapManager.getMap(player.getWorld().getName()).getYaml();
            List<String> locations = yaml.<E>getNestedMapElement(this.elementType).getLocations(enumValue);

            if (id > locations.size()) {
                CommandUtils.sendErrorMessage(player, "Invalid " + this.elementType.getLowerName() + " id!");
                return;
            }

            locations.remove(id - 1);
            this.openBook(player, enumValue, yaml);
            CommandUtils.sendSuccessMessage(player, "You successfully removed a " +
                YamlUtils.formatEnumString(enumValue.name()) + " " + this.elementType.getLowerName() + "!");
        }));
    }

    @Override
    public void registerPreviewCommand(String base) {
        Command.@NonNull Builder<CommandSender> cmdBuilder = this.getCommandBuilder(base, "preview")
            .argument(EnumArgument.of(this.enumClass, this.argument))
            .argument(IntegerArgument.<CommandSender>newBuilder("id").withMin(1));

        this.registerCommand(cmdBuilder.handler(c -> {
            Player player = (Player) c.getSender();
            World world = player.getWorld();

            if (world == Commons.getLobby()) {
                CommandUtils.sendErrorMessage(player, "Please join the map you want to edit the config of!");
                return;
            }

            E enumValue = c.get(this.argument);
            int id = c.get("id");

            MapYaml yaml = MapManager.getMap(player.getWorld().getName()).getYaml();
            List<String> locations = yaml.<E>getNestedMapElement(this.elementType).getLocations(enumValue);

            if (id > locations.size()) {
                CommandUtils.sendErrorMessage(player, "Invalid " + this.elementType.getLowerName() + " id!");
                return;
            }

            YamlUtils.teleportToCoords(player, yaml.<E>getNestedMapElement(this.elementType).getLocations(enumValue).get(id - 1));
        }));
    }

    private BaseComponent[] getLocationLine(int index, MapYaml yaml, E enumValue) {
        NestedMapElement<E> mapObject = yaml.getNestedMapElement(this.elementType);
        int size = mapObject == null || mapObject.getLocations(enumValue) == null ?
            0 : mapObject.getLocations(enumValue).size();

        return new ComponentBuilder(index + ". " + YamlUtils.formatEnumString(enumValue.name()))
            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(this.elementType.getBaseCommand(true) + "s " + enumValue.name().toLowerCase()).create()))
            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, this.elementType.getBaseCommand(true) + "s " + enumValue.name().toLowerCase()))
            .append(" (" + size + ")").color(ChatColor.GRAY)
            .create();
    }

    private void openBook(Player player, E enumValue, MapYaml yaml) {
        String name = YamlUtils.formatEnumString(enumValue.name());
        NestedMapElement<E> mapObject = yaml.getNestedMapElement(this.elementType);
        List<String> locations = mapObject == null || mapObject.getLocations(enumValue) == null ?
            new ArrayList<>()  : mapObject.getLocations(enumValue);

        int total = locations.size();

        if (total == 0) {
            openEmptyBook(player, enumValue);
            return;
        }

        List<BaseComponent[]> pages = new ArrayList<>();
        PageBuilder page = new PageBuilder();
        String cmd = this.elementType.getBaseCommand(true) + " add " + enumValue.name().toLowerCase();

        int i = 0;

        for (String location : locations) {
            if (i % 10 == 0) {
                if (i != 0) pages.add(page.build());
                page = new PageBuilder();
                page
                    .add(new ComponentBuilder("⬅ ")
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Back").create()))
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, this.elementType.getBaseCommand(true) + "s"))
                        .bold(true)
                        .create())
                    .add(total + " " + name + " " + this.elementType.getName() + (total == 1 ? "" : "s")).newLine().newLine()
                    .add(new ComponentBuilder("+ ADD")
                        .color(ChatColor.DARK_GREEN)
                        .bold(true)
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Add " + name + " " + this.elementType.getName() + "\n" + cmd).create()))
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd))
                        .create()).newLine();
            }

            int number = i + 1;
            String label = YamlUtils.formatCoords(location, true, true);
            String command = this.elementType.getBaseCommand(true) + " preview " + enumValue.name().toLowerCase() + " " + number;
            String hover = "Teleport to " + this.elementType.getName() + "\n" + command;

            page.newLine()
                .add(
                    new ComponentBuilder(number + ". ")
                        .append(label)
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create()))
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command))
                        .create())
                .add(
                    new ComponentBuilder("  ")
                        .append("✗")
                        .color(ChatColor.DARK_RED)
                        .bold(true)
                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("DELETE").color(ChatColor.RED).create()))
                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, this.elementType.getBaseCommand(true) + " remove " + enumValue.name().toLowerCase() + " " + number))
                        .create());

            i++;
        }

        pages.add(page.build());

        new BookBuilder().pages(pages).open(player);
    }

    private void openEmptyBook(Player player, E enumValue) {
        String name = YamlUtils.formatEnumString(enumValue.name());
        String cmd = this.elementType.getBaseCommand(true) + " add " + name.toLowerCase();

        PageBuilder pages = new PageBuilder()
            .add(new ComponentBuilder("⬅ ")
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Back").create()))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, this.elementType.getBaseCommand(true) + "s"))
                .bold(true)
                .create())
            .add("0 " + name + " " + this.elementType.getLowerName() + "s").newLine().newLine()
            .add(new ComponentBuilder("+ ADD")
                .color(ChatColor.DARK_GREEN)
                .bold(true)
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Add " + name + " " + this.elementType.getName() + "\n" + cmd).create()))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd))
                .create()).newLine();

        new BookBuilder().pages(pages).open(player);
    }

}
