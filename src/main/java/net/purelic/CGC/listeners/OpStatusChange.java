package net.purelic.CGC.listeners;

import net.purelic.CGC.managers.GameModeManager;
import net.purelic.CGC.managers.MapManager;
import net.purelic.CGC.runnables.AutoSave;
import net.purelic.commons.Commons;
import net.purelic.commons.events.OpStatusChangeEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class OpStatusChange implements Listener {

    @EventHandler
    public void onOpStatusChangedEvent(OpStatusChangeEvent event) {
        if (event.isOwnerJoin()) {
            AutoSave.setEnabled(true);
            MapManager.loadMaps(Commons.getOwnerId());
            GameModeManager.loadGameModes(Commons.getOwnerId());
        }

        Player player = event.getPlayer();

        if (Commons.getProfile(player).isMapDev()) {
            // Map devs can have vanilla op on game dev servers (to use command blocks)
            player.setOp(event.isOp());
        }
    }

}
