package net.purelic.CGC.events;

import net.purelic.CGC.gamemodes.CustomGameMode;
import net.purelic.CGC.gamemodes.settings.constants.GameModeSetting;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameModeSettingChangeEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final CustomGameMode gameMode;
    private final GameModeSetting setting;
    private final Object value;
    private final boolean initialLoad;

    public GameModeSettingChangeEvent(CustomGameMode gameMode, GameModeSetting setting, Object value) {
        this(gameMode, setting, value, false);
    }

    public GameModeSettingChangeEvent(CustomGameMode gameMode, GameModeSetting setting, Object value, boolean initialLoad) {
        this.gameMode = gameMode;
        this.setting = setting;
        this.value = value;
        this.initialLoad = initialLoad;
    }

    public CustomGameMode getGameMode() {
        return this.gameMode;
    }

    public GameModeSetting getSetting() {
        return this.setting;
    }

    public Object getValue() {
        return this.value;
    }

    public boolean isInitialLoad() {
        return this.initialLoad;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
