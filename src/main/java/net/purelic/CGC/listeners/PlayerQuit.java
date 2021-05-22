package net.purelic.CGC.listeners;

import net.purelic.CGC.runnables.AutoSave;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuit implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        // Auto-save when all players disconnect
        if (Bukkit.getOnlinePlayers().size() == 1 && AutoSave.isEnabled()) {
            AutoSave.saveAll(false, true);
        }
    }

}
