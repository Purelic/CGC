package net.purelic.CGC.maps.previews;

import net.purelic.commons.utils.YamlUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class Preview extends BukkitRunnable {

    protected final Player player;
    protected final World world;
    protected final Location location;
    protected final Block block;
    protected final BlockState blockState;
    private final Location center;

    protected Preview(Player player, String location) {
        this.player = player;
        this.world = player.getWorld();
        this.location = YamlUtils.getLocationFromCoords(this.world, location);
        this.block = this.location.getBlock();
        this.blockState = this.block.getState();
        this.center = this.block.getLocation().clone().add(0.5, 0, 0.5);
    }

    protected Location getCenter() {
        return this.getCenter(0);
    }

    protected Location getCenter(int offsetY) {
        return this.center.clone().add(0, offsetY, 0);
    }

    public abstract Runnable destroy();

}
