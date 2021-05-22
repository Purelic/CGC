package net.purelic.CGC.maps.previews;

import net.purelic.commons.utils.TaskUtils;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class JumpPadPreview extends Preview {

    private final boolean destructive;
    private final BukkitRunnable launchRunnable;
    private final BukkitRunnable particleRunnable;

    public JumpPadPreview(Player player, String location, int angle, int power, boolean destructive) {
        super(player, location);
        this.destructive = destructive;
        this.launchRunnable = this.getLaunchRunnable(this.block.getLocation(), angle, power);
        this.particleRunnable = this.getParticleRunnable(this.location.clone().add(0.5, 1, 0.5));
    }

    @Override
    public void run() {
        // Set the jump pad block if destructive
        if (this.destructive) {
            this.block.setType(Material.SLIME_BLOCK);
        }

        // Start the runnables
        TaskUtils.runTimerAsync(this.launchRunnable, 0L, 1L);
        TaskUtils.runTimerAsync(this.particleRunnable, 0L, 5L);

        // Teleport player to jump pad
        Location teleport = this.location.clone();
        Location playerLoc = this.player.getLocation();

        teleport.setPitch(playerLoc.getPitch());
        teleport.setYaw(playerLoc.getYaw());

        this.player.teleport(this.getCenter(2));
    }

    @Override
    public Runnable destroy() {
        return () -> {
            this.blockState.update(true);
            this.particleRunnable.cancel();
            this.launchRunnable.cancel();
        };
    }

    private BukkitRunnable getLaunchRunnable(Location jumpPadLoc, int angle, int power) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.isSneaking()) continue;

                    Location playerLoc = player.getLocation().getBlock().getRelative(BlockFace.DOWN).getLocation();

                    if (playerLoc.equals(jumpPadLoc)) {
                        Location launchLocation = player.getLocation().clone();
                        launchLocation.setPitch((float) -angle);
                        player.setVelocity(launchLocation.getDirection().normalize().multiply((power * 5D) / 15D));
                        player.getWorld().playSound(playerLoc, Sound.WITHER_SHOOT, 1F, 1F);
                        player.getWorld().playEffect(playerLoc.add(0.5, 1.25, 0.5), Effect.EXPLOSION_LARGE, 1, 1);
                    }
                }
            }
        };
    }

    private BukkitRunnable getParticleRunnable(Location location) {
        return new BukkitRunnable() {
            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.spigot().playEffect(
                        location,
                        Effect.INSTANT_SPELL,
                        0,
                        0,
                        0.2F,
                        0.25F,
                        0.20F,
                        100F,
                        10,
                        50
                    );
                }
            }
        };
    }

}
