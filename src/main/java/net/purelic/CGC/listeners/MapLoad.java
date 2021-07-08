package net.purelic.CGC.listeners;

import net.purelic.CGC.managers.MapManager;
import net.purelic.CGC.maps.CustomMap;
import net.purelic.commons.events.MapLoadEvent;
import net.purelic.commons.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MapLoad implements Listener {

    private static final Map<String, UUID> CACHED_DOWNLOADS = new HashMap<>();

    @EventHandler
    public void onMapLoad(MapLoadEvent event) {
        String map = event.getMap();
        World world = event.getWorld();

        CustomMap customMap = MapManager.getMap(map);
        customMap.setWorld(world);

        MapManager.addMap(map, customMap);
        MapManager.setPending(map, false);

        CommandUtils.broadcastSuccessMessage("Successfully downloaded map \"" + map + "\"! Use /maps or /map " + map + " to teleport");

        // Auto-teleport the player who downloaded the map
        if (CACHED_DOWNLOADS.containsKey(map)) {
            Player player = Bukkit.getPlayer(CACHED_DOWNLOADS.get(map));

            if (player != null && player.isOnline()) {
                player.setGameMode(GameMode.CREATIVE);
                player.setFlying(true);
                customMap.teleport(player);
            }

            CACHED_DOWNLOADS.remove(map);
        }
    }

    public static void cache(Player player, String mapName) {
        CACHED_DOWNLOADS.put(mapName, player.getUniqueId());
    }

}
