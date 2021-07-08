package net.purelic.CGC.maps.previews;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.regions.RegionSelector;
import com.sk89q.worldedit.regions.selector.CuboidRegionSelector;
import com.sk89q.worldedit.regions.selector.CylinderRegionSelector;
import com.sk89q.worldedit.regions.selector.EllipsoidRegionSelector;
import com.sk89q.worldedit.world.World;
import net.purelic.CGC.maps.constants.RegionType;
import net.purelic.CGC.maps.elements.Region;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RegionPreview extends Preview {

    // TODO this preview doesn't work - can't figure out how to set the player's current selection

    private final Region region;

    public RegionPreview(Player player, String location, Region region) {
        super(player, location);
        this.region = region;
    }

    @Override
    public void run() {
        LocalSession session = WorldEdit.getInstance()
            .getSessionManager()
            .findByName(this.player.getName());

        if (session == null) {
            this.player.teleport(this.getCenter());
            return;
        }

        World world = session.getSelectionWorld();
        RegionSelector selector = null;

        if (this.region.getType() == RegionType.CUBOID) {
            selector = new CuboidRegionSelector(world, this.region.getMin(), this.region.getMax());
        } else if (this.region.getType() == RegionType.CYLINDER) {
            selector = new CylinderRegionSelector(world, this.region.getCenter().toVector2D(), this.region.getRadius().toVector2D(),
                this.region.getMinY(), this.region.getMaxY());
        } else if (this.region.getType() == RegionType.ELLIPSOID) {
            selector = new EllipsoidRegionSelector(world, this.region.getCenter(), this.region.getRadius());
        }

        if (selector != null && world != null) session.setRegionSelector(session.getSelectionWorld(), selector);

        // Teleport player to center of region
        this.player.teleport(this.getCenter());
        // Wand required for the selection visualization to show
        this.player.setItemInHand(new ItemStack(Material.WOOD_AXE));
    }

    @Override
    public void destroy() { }

}
