package net.purelic.CGC.maps.settings;

import cloud.commandframework.arguments.CommandArgument;
import net.md_5.bungee.api.chat.BaseComponent;
import net.purelic.CGC.maps.constants.MapElementType;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface MapSetting extends Cloneable {

    String getSettingName();

    default String getSettingCommand(String baseCommand, String settingName) {
        return "/" + baseCommand + " " + settingName.toLowerCase().replaceAll(" ", "_").replaceAll("\\.", "") + " <id> <value>";
    }

    default String getCommandArgument() {
        return this.getSettingName().toLowerCase();
    }

    default String getCommandLiteral() {
        return this.getCommandArgument().replaceAll(" ", "_").replaceAll("\\.", "");
    }

    Object getDefaultValue();

    boolean isDefault();

    void setCurrent(Object value);

    String getYamlKey();

    Object getYamlValue();

    BaseComponent[] getName();

    BaseComponent[] getValue(int id);

    void registerCommand(MapElementType objectType);

    @NonNull CommandArgument<CommandSender, ?> getArgument();

    MapSetting copy();

}
