package net.purelic.CGC.maps.previews;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;

public class SpawnerPreview extends Preview {

    private final Material material;
    private final boolean hologram;
    private final int delay;
    private final int amount;
    private final boolean destructive;
    private ArmorStand stand;

    public SpawnerPreview(Player player, String location, Material material, boolean hologram, int delay, int amount, boolean destructive) {
        super(player, location);
        this.material = material;
        this.hologram = hologram;
        this.delay = delay;
        this.amount = amount;
        this.destructive = destructive;
    }

    @Override
    public void run() {
        // Set the block to a spawner if destructive
        if (this.destructive) {
            this.block.setType(Material.MOB_SPAWNER);
            BlockState current = this.block.getState();
            CreatureSpawner spawner = (CreatureSpawner) current;
            spawner.setSpawnedType(EntityType.FALLING_BLOCK);
            spawner.setDelay(Integer.MAX_VALUE);
        }

        // Create the armor stand display
        this.stand = this.getArmorStand(this.getCenter(), this.material, this.hologram, this.delay, this.amount);

        // Teleport player to spawner
        this.player.teleport(this.getCenter(1));
    }

    @Override
    public Runnable destroy() {
        return () -> {
            this.blockState.update(true);
            this.stand.remove();
        };
    }

    private ArmorStand getArmorStand(Location location, Material material, boolean hologram, int delay, int amount) {
        ArmorStand stand = (ArmorStand) location.getWorld().spawnEntity(location.clone().add(0, 2, 0), EntityType.ARMOR_STAND);
        stand.setSmall(true);
        stand.setCanPickupItems(false);
        stand.setMarker(true);
        stand.setGravity(false);
        stand.setVisible(false);
        stand.setRemoveWhenFarAway(false);
        stand.setBasePlate(false);
        stand.setLeftLegPose(new EulerAngle(Math.PI, 0.0D, 0.0D));
        stand.setRightLegPose(new EulerAngle(Math.PI, 0.0D, 0.0D));

        if (hologram) {
            stand.setCustomName(this.getHologramColor(material) + "" + delay + "s" + (amount == 1 ? "" : " (x" + amount + ")"));
            stand.setCustomNameVisible(true);

            if (material == Material.ARROW) stand.setHelmet(new ItemStack(Material.IRON_BLOCK));
            else if (material == Material.GOLDEN_APPLE) stand.setHelmet(new ItemStack(Material.GOLD_BLOCK));
            else if (material == Material.DIAMOND) stand.setHelmet(new ItemStack(Material.DIAMOND_BLOCK));
            else if (material == Material.EMERALD) stand.setHelmet(new ItemStack(Material.EMERALD_BLOCK));
        }

        return stand;
    }

    private ChatColor getHologramColor(Material material) {
        if (material == Material.GOLDEN_APPLE) return ChatColor.YELLOW;
        else if (material == Material.DIAMOND) return ChatColor.AQUA;
        else if (material == Material.EMERALD) return ChatColor.GREEN;
        else return ChatColor.WHITE;
    }

}
