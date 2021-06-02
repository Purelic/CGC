package net.purelic.CGC.maps.previews;

import net.purelic.CGC.maps.constants.BedDefenseType;
import net.purelic.CGC.maps.constants.BedDirection;
import net.purelic.CGC.maps.constants.MatchTeam;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BedPreview extends Preview {

    private final BedDefenseType defense;
    private final BedDirection direction;
    private final MatchTeam owner;
    private final List<BlockState> blockStates;

    public BedPreview(Player player, String location, BedDefenseType defense, BedDirection direction, MatchTeam owner) {
        super(player, location);
        this.defense = defense;
        this.direction = direction;
        this.owner = owner;
        this.blockStates = new ArrayList<>();
    }

    @Override
    public void run() {
        // Build the bed defense and save the block states
        this.blockStates.addAll(this.defense.build(this.location, this.direction, this.owner));

        // Teleport player to bed
        this.player.teleport(this.getCenter(3));
    }

    @Override
    public void destroy() {
        this.blockStates.forEach(state -> state.update(true));
        this.blockStates.clear();
    }

}
