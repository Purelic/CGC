package net.purelic.CGC.gamemodes.settings.constants;

import net.md_5.bungee.api.chat.BaseComponent;
import net.purelic.CGC.gamemodes.CustomGameMode;

public interface GameModeSetting {

    String getKey();

    Object getDefaultValue();

    void registerCommand();

    int getLines();

    void setDisabled(boolean disabled);

    void setName(String name);

    void reset();

    BaseComponent[] getName(CustomGameMode gameMode);

    static void registerSettingCommands() {
        for (GameModeSetting setting : GameModeSettingType.values()) setting.registerCommand();
        for (GameModeSetting setting : GameModeEnumSetting.values()) setting.registerCommand();
        for (GameModeSetting setting : GameModeNumberSetting.values()) setting.registerCommand();
        for (GameModeSetting setting : GameModeToggleSetting.values()) setting.registerCommand();
    }

}
