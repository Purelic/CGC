package net.purelic.CGC.modules;

import net.purelic.commons.events.OpStatusChangeEvent;
import net.purelic.commons.modules.Module;
import net.purelic.commons.utils.CommandUtils;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.*;

public class GriefProtectionModule implements Module {

    // Prevent block placement
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!CommandUtils.isOp(event.getPlayer())) event.setCancelled(true);
    }

    // Prevent block breaking
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (!CommandUtils.isOp(event.getPlayer())) event.setCancelled(true);
    }

    // Prevent interacting with items
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!CommandUtils.isOp(event.getPlayer())) event.setCancelled(true);
    }

    // Prevent damaging entities
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();

        if (damager instanceof Player) {
            if (!CommandUtils.isOp((Player) damager)) event.setCancelled(true);
        }
    }

    // Prevent interacting with entities
    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (!CommandUtils.isOp(event.getPlayer())) event.setCancelled(true);
    }

    // Prevent dropping items
    @EventHandler
    public void onPlayerDropItemEvent(PlayerDropItemEvent event) {
        if (!CommandUtils.isOp(event.getPlayer())) event.setCancelled(true);
    }

    // Prevent picking up items
    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        if (!CommandUtils.isOp(event.getPlayer())) event.setCancelled(true);
    }

    // Prevent colliding with entities and affecting spawners
    @EventHandler
    public void onOpStatusChangedEvent(OpStatusChangeEvent event) {
        Player player = event.getPlayer();
        player.spigot().setCollidesWithEntities(CommandUtils.isOp(player));
        player.spigot().setAffectsSpawning(CommandUtils.isOp(player));
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        player.spigot().setCollidesWithEntities(CommandUtils.isOp(player));
        player.spigot().setAffectsSpawning(CommandUtils.isOp(player));
    }

    // Prevent entity explosions
    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        event.setCancelled(true);
    }

    // Prevent block explosions
    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        event.setCancelled(true);
    }

    // Prevent non-player entities from damaging each other
    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) event.setCancelled(true);
    }

}
