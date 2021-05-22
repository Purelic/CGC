package net.purelic.CGC.maps.settings;

import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.standard.EnumArgument;
import cloud.commandframework.arguments.standard.IntegerArgument;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.purelic.CGC.maps.constants.MapElementType;
import net.purelic.CGC.maps.MapElement;
import net.purelic.CGC.maps.MapYaml;
import net.purelic.CGC.managers.MapManager;
import net.purelic.commons.Commons;
import net.purelic.commons.commands.parsers.Permission;
import net.purelic.commons.utils.CommandUtils;
import net.purelic.commons.utils.YamlUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("all")
public class MapEnumSetting<E extends Enum<E>> implements MapSetting {

    private final String baseCommand;
    protected final String name;
    private final String descriptionRaw;
    private final String description;
    private final String command;
    private final E defaultValue;
    private final String yamlKey;
    protected final List<E> options;
    private final Class<E> enumClass;

    private E current;

    public MapEnumSetting(String baseCommand, Class<E> enumClass, String name, String description) {
        this(
            baseCommand,
            enumClass,
            name,
            description,
            enumClass.getEnumConstants()
        );
    }

    public MapEnumSetting(String baseCommand, Class<E> enumClass, String name, String description, E... options) {
        this(
            baseCommand,
            enumClass,
            name,
            description,
            Arrays.asList(options)
        );
    }

    public MapEnumSetting(String baseCommand, Class<E> enumClass, String name, String description, List<E> options) {
        this.baseCommand = baseCommand;
        this.name = name;
        this.descriptionRaw = description;
        this.description = WordUtils.wrap(description, 40, "\n", false);
        this.command = this.getSettingCommand(baseCommand, name);
        this.defaultValue = options.get(0);
        this.yamlKey = YamlUtils.toKey(name);
        this.options = options;
        this.enumClass = enumClass;
    }

    public Class<E> getEnumClass() {
        return this.enumClass;
    }

    @Override
    public String getSettingName() {
        return this.name;
    }

    @Override
    public E getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public boolean isDefault() {
        return this.defaultValue == this.getCurrentValue();
    }

    @Override
    public String getYamlKey() {
        return this.yamlKey;
    }

    @Override
    public String getYamlValue() {
        return ((E) this.getCurrentValue()).name();
    }

    @Override
    public BaseComponent[] getName() {
        return new ComponentBuilder(this.name + ": ")
            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(this.description).create()))
            .create();
    }

    @Override
    public BaseComponent[] getValue(int id) {
        String prevCmd = this.command.replaceAll("<id>", id + "").replaceAll("<value>", this.getPreviousOption());
        prevCmd = prevCmd.replaceAll(" -1 ", " ");

        String nextCmd = this.command.replaceAll("<id>", id + "").replaceAll("<value>", this.getNextOption());
        nextCmd = nextCmd.replaceAll(" -1 ", " ");

        return new ComponentBuilder("《 ")
            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Previous " + this.name + "\n" + prevCmd).create()))
            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, prevCmd))
            .append(YamlUtils.formatEnumString(this.current.name()).replaceAll("Solo", "Solo/Neutral")).reset()
            .append(" 》")
            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Next " + this.name + "\n" + nextCmd).create()))
            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, nextCmd))
            .create();
    }

    @Override
    public void setCurrent(Object current) {
        if (current instanceof String) this.current = E.valueOf(this.enumClass, (String) current);
        else this.current = (E) current;
    }

    public E getCurrentValue() {
        return this.current;
    }

    private String getNextOption() {
        int index = this.options.indexOf(this.current) + 1;
        index = index == this.options.size() ? 0 : index;
        return this.options.get(index).name().toLowerCase();
    }

    private String getPreviousOption() {
        int index = this.options.indexOf(this.current) + -1;
        index = index < 0 ? this.options.size() - 1 : index;
        return this.options.get(index).name().toLowerCase();
    }

    @Override
    public void registerCommand(MapElementType objectType) {
        Commons.registerCommand(mgr ->
            mgr.commandBuilder(objectType.getBaseCommand(false))
                .senderType(Player.class)
                .permission(Permission.isMapDev(true))
                .literal(this.getCommandLiteral())
                .argument(IntegerArgument.<CommandSender>newBuilder("id").withMin(1).build())
                .argument(this.getArgument())
                .handler(c -> {
                    Player player = (Player) c.getSender();
                    World world = player.getWorld();
                    int id = c.get("id");

                    if (world == Commons.getLobby()) {
                        CommandUtils.sendErrorMessage(player, "Please join the map you want to edit the config of!");
                        return;
                    }

                    E arg = c.get(this.getCommandArgument());

                    if (this.options.contains(arg.name())) {
                        CommandUtils.sendErrorMessage(player, "That's not a valid " + this.getSettingName().toLowerCase() + " for " + objectType.getLowerName() + "s!");
                        return;
                    }

                    MapYaml yaml = MapManager.getMap(player.getWorld().getName()).getYaml();
                    List<MapElement> mapElements = yaml.getMapElements(objectType);

                    if (id > mapElements.size()) {
                        CommandUtils.sendErrorMessage(player, "Invalid " + objectType.getLowerName() + " id!");
                        return;
                    }

                    MapElement mapElement = mapElements.get(id - 1);
                    mapElement.getSetting(this.name).setCurrent(arg);
                    yaml.updateYaml();
                    mapElement.openBook(player, id);
                }));
    }

    @Override
    public @NonNull CommandArgument<CommandSender, E> getArgument() {
        return EnumArgument.optional(
            this.enumClass,
            this.getCommandArgument(),
            this.defaultValue
        );
    }

    @Override
    public MapEnumSetting copy() {
        return new MapEnumSetting(
            this.baseCommand,
            this.enumClass,
            this.name,
            this.descriptionRaw,
            this.options
        );
    }

}
