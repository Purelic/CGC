package net.purelic.CGC.maps.constants;

import org.bukkit.block.BlockFace;

public enum FlagDirection {

    NORTH(BlockFace.NORTH),
    NORTH_EAST(BlockFace.NORTH_EAST),
    EAST(BlockFace.EAST),
    SOUTH_EAST(BlockFace.SOUTH_EAST),
    SOUTH(BlockFace.SOUTH),
    SOUTH_WEST(BlockFace.SOUTH_WEST),
    WEST(BlockFace.WEST),
    NORTH_WEST(BlockFace.NORTH_WEST),
    ;

    private final BlockFace blockFace;

    FlagDirection(BlockFace blockFace) {
        this.blockFace = blockFace;
    }

    public BlockFace getBlockFace() {
        return this.blockFace;
    }

}
