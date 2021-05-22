package net.purelic.CGC.maps.settings;

import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.standard.IntegerArgument;
import net.md_5.bungee.api.ChatColor;
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

import java.util.List;

public class MapNumberSetting implements MapSetting {

    private final String baseCommand;
    protected final String name;
    private final String descriptionRaw;
    private final String description;
    private final String command;
    private final String suffix;
    private final int min;
    private final int max;
    private final int increment;
    private final boolean plural;
    private final int defaultValue;
    private final String yamlKey;

    private int current;

    public MapNumberSetting(String baseCommand, String name, String description, String suffix, int min, int max, int increment, boolean plural, int defaultValue) {
        this.baseCommand = baseCommand;
        this.name = name;
        this.descriptionRaw = description;
        this.description = WordUtils.wrap(description, 40, "\n", false);
        this.command = this.getSettingCommand(baseCommand, name);
        this.suffix = suffix;
        this.min = min;
        this.max = max;
        this.increment = increment;
        this.plural = plural;
        this.defaultValue = defaultValue;
        this.yamlKey = YamlUtils.toKey(name);
    }

    @Override
    public String getSettingName() {
        return this.name;
    }

    @Override
    public Integer getDefaultValue() {
        return this.defaultValue;
    }

    @Override
    public boolean isDefault() {
        return this.defaultValue == this.current;
    }

    @Override
    public String getYamlKey() {
        return this.yamlKey;
    }

    @Override
    public Integer getYamlValue() {
        return this.current;
    }

    @Override
    public BaseComponent[] getName() {
        return new ComponentBuilder(this.name + ": ")
            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(this.description).create()))
            .create();
    }

    @Override
    public BaseComponent[] getValue(int id) {
        int minusVal = this.current - this.increment;
        int plusVal = this.current + this.increment;

        String minusCmd = this.command.replaceAll("<id>", id + "").replaceAll("<value>", "" + minusVal);
        minusCmd = minusCmd.replaceAll(" -1 ", " ");

        String plusCmd = this.command.replaceAll("<id>", id + "").replaceAll("<value>", "" + plusVal);
        plusCmd = plusCmd.replaceAll(" -1 ", " ");

        ComponentBuilder builder = new ComponentBuilder("");

        if (minusVal >= this.min) {
            builder
                .append("- ")
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("-" + this.increment + this.getSuffix() + "\n" + minusCmd).create()))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, minusCmd));
        } else {
            builder
                .append("- ")
                .color(ChatColor.GRAY)
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Minimum " + this.min).create()));
        }

        builder.append(this.current + (!this.plural ? this.getSuffix() : this.getValueSuffix())).reset().color(ChatColor.BLACK);

        if (plusVal <= this.max) {
            builder
                .append(" +")
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("+" + this.increment + this.getSuffix() + "\n" + plusCmd).create()))
                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, plusCmd));
        } else {
            builder
                .append(" +")
                .color(ChatColor.GRAY)
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Maximum " + this.max).create()));
        }

        return builder.create();
    }

    @Override
    public void setCurrent(Object current) {
        this.current = (int) current;
    }

    private String getSuffix() {
        return this.suffix + (this.plural && this.increment != 1 ? "s" : "");
    }

    private String getValueSuffix() {
        return this.suffix + (this.current != 1 ? "s" : "");
    }

    @Override
    public void registerCommand(MapElementType objectType) {
        Commons.registerCommand(mgr ->
            mgr.commandBuilder(objectType.getBaseCommand(false))
                .senderType(Player.class)
                .permission(Permission.isMapDev(true))
                .literal(this.getCommandLiteral())
                .argument(IntegerArgument.<CommandSender>newBuilder("id").withMin(1))
                .argument(this.getArgument())
                .handler(c -> {
                    Player player = (Player) c.getSender();
                    World world = player.getWorld();
                    int id = c.get("id");

                    if (world == Commons.getLobby()) {
                        CommandUtils.sendErrorMessage(player, "Please join the map you want to edit the config of!");
                        return;
                    }

                    MapYaml yaml = MapManager.getMap(player.getWorld().getName()).getYaml();
                    List<MapElement> mapElements = yaml.getMapElements(objectType);

                    if (id > mapElements.size()) {
                        CommandUtils.sendErrorMessage(player, "Invalid " + objectType.getLowerName() + " id!");
                        return;
                    }

                    MapElement mapElement = mapElements.get(id - 1);
                    mapElement.getSetting(this.name).setCurrent(c.get(this.getCommandArgument()));
                    yaml.updateYaml();
                    mapElement.openBook(player, id);
                }));
    }

    @Override
    public @NonNull CommandArgument<CommandSender, Integer> getArgument() {
        return IntegerArgument.
            <CommandSender>newBuilder(this.getCommandArgument())
            .withMin(this.min)
            .withMax(this.max)
            .asOptionalWithDefault(this.defaultValue)
            .build();
    }

    public int getCurrentValue() {
        return this.current;
    }

    @Override
    public MapNumberSetting copy() {
        return new MapNumberSetting(
            this.baseCommand,
            this.name,
            this.descriptionRaw,
            this.suffix,
            this.min,
            this.max,
            this.increment,
            this.plural,
            this.defaultValue
        );
    }

}
