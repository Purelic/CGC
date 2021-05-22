package net.purelic.CGC.maps.previews;

import net.purelic.CGC.maps.constants.MatchTeam;
import net.purelic.commons.utils.EntityUtils;
import net.purelic.commons.utils.TaskUtils;
import org.bukkit.Color;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.scheduler.BukkitRunnable;

public class Waypoint {

    private final ArmorStand stand;
    private final BukkitRunnable runnable;

    public Waypoint(Location location, String name, MatchTeam team, int offset, boolean beam) {
        this.stand = EntityUtils.getInvisibleStand(location.clone().add(0, offset, 0), name);

        if (!beam) { // only show name of waypoint
            this.runnable = null;
            return;
        }

        this.runnable = new BukkitRunnable() {

            private final World world = location.getWorld();
            private final Color color = team.getColor();
            private final float r = this.rgbToParticle(this.color.getRed());
            private final float g = this.rgbToParticle(this.color.getGreen());
            private final float b = this.rgbToParticle(this.color.getBlue());

            private float rgbToParticle(int rgb) {
                return Math.max(0.001f, (rgb / 255.0f));
            }

            @Override
            public void run() {
                for (int i = (offset + 1); i <= (offset + 26); i++) {
                    Location particleLoc = location.clone().add(0, i, 0);
                    this.world.spigot().playEffect(
                        particleLoc,
                        Effect.COLOURED_DUST,
                        0,
                        0,
                        this.r,
                        this.g,
                        this.b,
                        1,
                        0,
                        64
                    );
                }
            }
        };

        TaskUtils.runTimerAsync(this.runnable, 0L, 2L);
    }

    public void destroy() {
        this.stand.remove();
        if (this.runnable != null) this.runnable.cancel();
    }

}
