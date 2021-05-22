package net.purelic.CGC.events;

import net.purelic.CGC.gamemodes.CustomGameMode;
import net.purelic.CGC.gamemodes.constants.GameType;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class GameModeOpenEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final CustomGameMode gameMode;
    private final GameType gameType;

    public GameModeOpenEvent(CustomGameMode gameMode) {
        this.gameMode = gameMode;
        this.gameType = gameMode.getGameType();
    }

    public CustomGameMode getGameMode() {
        return this.gameMode;
    }

    public GameType getGameType() {
        return this.gameType;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}
