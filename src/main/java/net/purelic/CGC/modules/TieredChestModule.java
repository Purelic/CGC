package net.purelic.CGC.modules;

import net.purelic.CGC.managers.MapManager;
import net.purelic.CGC.maps.CustomMap;
import net.purelic.CGC.maps.NestedMapElement;
import net.purelic.CGC.maps.constants.ChestTier;
import net.purelic.CGC.maps.constants.MapElementType;
import net.purelic.commons.modules.Module;
import net.purelic.commons.utils.CommandUtils;
import net.purelic.commons.utils.YamlUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TieredChestModule implements Module {

    private final List<String> chestTierNames;

    public TieredChestModule() {
        this.chestTierNames = Stream.of(ChestTier.values())
            .map(Enum::name)
            .collect(Collectors.toList());
    }

    @SuppressWarnings("all")
    @EventHandler(ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();

        if (block.getType() != Material.CHEST) return;

        Chest chest = (Chest) block.getState();
        String invName = chest.getBlockInventory().getName();
        String tierValue = invName
            .replaceAll(" Chest", "")
            .replaceAll(" ", "_")
            .toUpperCase();

        // Check if the chest's name is a valid chest tier
        if (this.chestTierNames.contains(tierValue.toUpperCase())) {
            ChestTier tier = ChestTier.valueOf(tierValue);
            CustomMap map = MapManager.getMap(block.getWorld().getName());
            NestedMapElement<ChestTier> nestedElement = map.getYaml().getNestedMapElement(MapElementType.CHEST);

            // If no chest element exists, create one and add it to the map yaml
            if (nestedElement == null) {
                nestedElement = (NestedMapElement<ChestTier>) MapElementType.CHEST.create(new HashMap<>());
                map.getYaml().addMapElement(MapElementType.CHEST, nestedElement);
            }

            nestedElement.addLocation(tier, block.getLocation());
            CommandUtils.sendSuccessMessage(event.getPlayer(),
                "Successfully created a " + YamlUtils.formatEnumString(tier.name()) + " Chest!");
        }
    }

}
