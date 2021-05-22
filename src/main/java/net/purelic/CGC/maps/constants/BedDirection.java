package net.purelic.CGC.maps.constants;

import org.bukkit.block.BlockFace;

public enum BedDirection {

    NORTH(BlockFace.NORTH),
    SOUTH(BlockFace.SOUTH),
    EAST(BlockFace.EAST),
    WEST(BlockFace.WEST);

    private final BlockFace blockFace;

    BedDirection(BlockFace blockFace) {
        this.blockFace = blockFace;
    }

    public BlockFace getBlockFace() {
        return this.blockFace;
    }

}
