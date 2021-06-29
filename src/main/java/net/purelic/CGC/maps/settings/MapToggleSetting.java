package net.purelic.CGC.maps.settings;

import cloud.commandframework.arguments.CommandArgument;
import cloud.commandframework.arguments.standard.IntegerArgument;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.purelic.CGC.managers.MapManager;
import net.purelic.CGC.maps.MapElement;
import net.purelic.CGC.maps.MapYaml;
import net.purelic.CGC.maps.constants.MapElementType;
import net.purelic.commons.Commons;
import net.purelic.commons.commands.parsers.BooleanArgument;
import net.purelic.commons.commands.parsers.Permission;
import net.purelic.commons.utils.CommandUtils;
import net.purelic.commons.utils.YamlUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;

public class MapToggleSetting implements MapSetting {

    private final String baseCommand;
    protected final String name;
    private final String descriptionRaw;
    private final String description;
    private final String command;
    private final String yamlKey;
    private final boolean defaultValue;

    private boolean current;

    public MapToggleSetting(String baseCommand, String name, String description, boolean defaultValue) {
        this.baseCommand = baseCommand;
        this.name = name;
        this.descriptionRaw = description;
        this.description = WordUtils.wrap(description, 40, "\n", false);
        this.command = this.getSettingCommand(baseCommand, name);
        this.yamlKey = YamlUtils.toKey(name);
        this.defaultValue = defaultValue;
    }

    @Override
    public String getSettingName() {
        return this.name;
    }

    @Override
    public Boolean getDefaultValue() {
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
    public Boolean getYamlValue() {
        return this.current;
    }

    @Override
    public void setCurrent(Object current) {
        this.current = (boolean) current;
    }

    @Override
    public BaseComponent[] getName() {
        return new ComponentBuilder(this.name + ": ")
            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(this.description).create()))
            .create();
    }

    @Override
    public BaseComponent[] getValue(int id) {
        String cmd = this.command.replaceAll("<id>", id + "").replaceAll("<value>", !this.current + "");
        cmd = cmd.replaceAll(" -1 ", " ");

        return new ComponentBuilder(this.current ? "⦿" : "⦾")
            .color(this.current ? ChatColor.DARK_GREEN : ChatColor.DARK_RED)
            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Toggle " + (this.current ? "Off" : "On") + "\n" + cmd).create()))
            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd))
            .append(this.current ? " On" : " Off")
            .create();
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

    public boolean getCurrentValue() {
        return this.current;
    }

    @Override
    public @NonNull CommandArgument<CommandSender, Boolean> getArgument() {
        return BooleanArgument.optional(
            this.getCommandArgument(),
            this.defaultValue
        );
    }

    @Override
    public MapToggleSetting copy() {
        return new MapToggleSetting(
            this.baseCommand,
            this.name,
            this.descriptionRaw,
            this.defaultValue
        );
    }

}
