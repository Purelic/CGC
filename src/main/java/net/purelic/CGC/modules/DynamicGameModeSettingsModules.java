package net.purelic.CGC.modules;

import net.purelic.CGC.gamemodes.CustomGameMode;
import net.purelic.CGC.gamemodes.constants.GameType;
import net.purelic.CGC.gamemodes.settings.constants.*;
import net.purelic.CGC.events.GameModeOpenEvent;
import net.purelic.CGC.events.GameModeSettingChangeEvent;
import net.purelic.commons.modules.Module;
import org.bukkit.event.EventHandler;

public class DynamicGameModeSettingsModules implements Module {

    @EventHandler
    public void onGameModeSettingChange(GameModeSettingChangeEvent event) {
        CustomGameMode gameMode = event.getGameMode();

        if (!event.isInitialLoad()) gameMode.setUnsavedChanges(true);

        if (event.getSetting() == GameModeEnumSetting.GAME_TYPE) {
            GameType gameType = GameType.valueOf((String) event.getValue());
            gameMode.setGameType(gameType);
        }

        this.updateGameSettings(gameMode.getGameType());
    }

    @EventHandler
    public void onGameModeOpen(GameModeOpenEvent event) {
        this.updateGameSettings(event.getGameType());
    }

    // Disable certain settings for some game types
    // and rename some sections to be more user friendly
    private void updateGameSettings(GameType gameType) {
        this.resetAllSettings();

        switch (gameType) {
            case HEAD_HUNTER:
                GameModeSettingType.DEATHMATCH.setName("Kill Scoring");
                GameModeSettingType.KING_OF_THE_HILL.setName("Collection Hills");
                GameModeNumberSetting.HILL_CAPTURE_POINTS.setDisabled(true);
                break;
            case KING_OF_THE_HILL:
                GameModeSettingType.DEATHMATCH.setName("Kill Scoring");
                break;
            case CAPTURE_THE_FLAG:
                GameModeSettingType.KING_OF_THE_HILL.setName("Flag Goals");
                break;
        }
    }

    // Enables and resets all settings to their default name
    private void resetAllSettings() {
        for (GameModeSetting setting : GameModeSettingType.values()) setting.reset();
        for (GameModeSetting setting : GameModeEnumSetting.values()) setting.reset();
        for (GameModeSetting setting : GameModeNumberSetting.values()) setting.reset();
        for (GameModeSetting setting : GameModeToggleSetting.values()) setting.reset();
    }

}
