package net.purelic.CGC.maps.previews;

import net.purelic.CGC.maps.constants.HillType;
import net.purelic.CGC.maps.constants.MatchTeam;
import net.purelic.CGC.maps.constants.WaypointVisibility;
import net.purelic.commons.modules.BlockPhysicsModule;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import java.util.ArrayList;
import java.util.List;

public class HillPreview extends Preview {

    private final String name;
    private final MatchTeam owner;
    private final int radius;
    private final boolean circle;
    private final Material material;
    private final HillType type;
    private final boolean destructive;
    private final List<BlockState> blockStates;
    private final WaypointVisibility waypointVisibility;
    private Waypoint waypoint;

    public HillPreview(Player player, String location, String name, MatchTeam owner, int radius,
                       boolean circle, Material material, HillType type, boolean destructive, WaypointVisibility waypointVisibility) {
        super(player, location);
        this.name = name;
        this.owner = owner;
        this.radius = radius;
        this.circle = circle;
        this.material = material;
        this.type = type;
        this.destructive = destructive;
        this.waypointVisibility = waypointVisibility;
        this.blockStates = new ArrayList<>();
    }

    @Override
    public void run() {
        if (this.waypointVisibility == WaypointVisibility.EVERYONE) {
            this.waypoint = new Waypoint(this.getCenter(), this.name, this.owner, 3, this.type != HillType.CTF_GOAL);
        }

        // Disable block physic updates
        BlockPhysicsModule.setBlockPhysics(false);

        // Create the hill preview
        this.createHill(this.block, this.owner, this.radius, this.circle, this.material);

        // Teleport player to the hill
        this.player.teleport(this.getCenter(1));
    }

    @Override
    public void destroy() {
        this.blockStates.forEach(state -> state.update(true));
        this.blockStates.clear();
        if (this.waypoint != null) this.waypoint.destroy();
        BlockPhysicsModule.setBlockPhysics(true);
    }

    private void createHill(Block center, MatchTeam owner, int radius, boolean circle, Material material) {
        DyeColor color = owner.getDyeColor();
        World world = center.getWorld();

        for (int xPoint = center.getX() - radius; xPoint <= center.getX() + radius; xPoint++) {
            for (int zPoint = center.getZ() - radius; zPoint <= center.getZ() + radius; zPoint++) {
                Block block = world.getBlockAt(xPoint, center.getY(), zPoint);

                // If the hill is not destructive, only replace blocks
                // that are the same type of material as the hill
                if (!this.destructive) {
                    Material type = block.getType();

                    if ((material == Material.STAINED_GLASS && type != Material.GLASS)
                        || (material == Material.STAINED_CLAY && type != Material.HARD_CLAY)
                        || (material == Material.WOOL && type != Material.WOOL)) {
                        continue;
                    }
                }

                this.blockStates.add(block.getState());

                if (circle) {
                    if (this.insideCircle(center, block, radius)) this.updateBlockColor(block, color, material);
                } else {
                    this.updateBlockColor(block, color, material);
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void updateBlockColor(Block block, DyeColor color, Material material) {
        if (block.getType() != material) block.setType(material);
        BlockState state = block.getState();
        MaterialData data = state.getData();
        data.setData(color.getData());
        state.update();
    }

    private boolean insideCircle(Block center, Block block, int radius) {
        return this.insideCircle(center.getLocation(), block.getLocation(), radius);
    }

    private boolean insideCircle(Location center, Location location, int radius) {
        return center.distance(location) <= (radius + 0.5);
    }

}
