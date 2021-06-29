package net.purelic.CGC.maps.previews;

import net.purelic.CGC.maps.constants.FlagDirection;
import net.purelic.CGC.maps.constants.MatchTeam;
import net.purelic.CGC.maps.constants.WaypointVisibility;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.Banner;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.material.Directional;

public class FlagPreview extends Preview {

    private final String name;
    private final BlockFace blockFace;
    private final MatchTeam owner;
    private final DyeColor dyeColor;
    private final WaypointVisibility waypointVisibility;
    private Waypoint waypoint;

    public FlagPreview(Player player, String location, String name, FlagDirection direction, MatchTeam owner, WaypointVisibility waypointVisibility) {
        super(player, location);
        this.name = name;
        this.blockFace = direction.getBlockFace();
        this.owner = owner;
        this.dyeColor = owner.getDyeColor();
        this.waypointVisibility = waypointVisibility;
    }

    @Override
    public void run() {
        // Create the waypoint
        if (this.waypointVisibility == WaypointVisibility.EVERYONE) {
            this.waypoint = new Waypoint(this.getCenter(), this.name, this.owner, 2, true);
        }

        // Create the banner preview
        this.block.setType(Material.STANDING_BANNER);
        Banner banner = (Banner) this.block.getState();
        ((Directional) banner.getData()).setFacingDirection(this.blockFace);
        banner.setBaseColor(this.dyeColor);
        banner.update();

        // Teleport player to flag
        this.player.teleport(this.getCenter());
    }

    @Override
    public void destroy() {
        this.block.setType(Material.AIR);
        if (this.waypoint != null) this.waypoint.destroy();
    }

}
