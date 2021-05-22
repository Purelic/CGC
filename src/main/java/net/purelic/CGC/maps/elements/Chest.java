package net.purelic.CGC.maps.elements;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.standard.EnumArgument;
import net.purelic.CGC.maps.constants.ChestTier;
import net.purelic.CGC.maps.constants.MapElementType;
import net.purelic.CGC.maps.NestedMapElement;
import net.purelic.CGC.maps.settings.MapSetting;
import net.purelic.commons.Commons;
import net.purelic.commons.utils.CommandUtils;
import net.purelic.commons.utils.YamlUtils;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Map;

@SuppressWarnings("unchecked")
public class Chest<E extends Enum<E>> extends NestedMapElement<E> {

    public Chest(Map<String, Object> yaml) {
        super(MapElementType.CHEST, (Class<E>) ChestTier.class, "tier", false, yaml);
    }

    @Override
    public String getBookHover() {
        return "\n\n" + YamlUtils.formatCoords(this.getLocation(), true, false);
    }

    @Override
    public void registerAddCommand(String base, MapSetting[] settings) {
        Command.@NonNull Builder<CommandSender> cmdBuilder = this.getCommandBuilder(base, "add")
            .argument(EnumArgument.of(ChestTier.class, "tier"));

        this.registerCommand(cmdBuilder.handler(c -> {
            Player player = (Player) c.getSender();
            World world = player.getWorld();

            if (world == Commons.getLobby()) {
                CommandUtils.sendErrorMessage(player, "Please join the map you want to edit the config of!");
                return;
            }

            ChestTier tier = c.get("tier");
            ItemStack chest = new ItemStack(Material.CHEST);
            ItemMeta meta = chest.getItemMeta();
            meta.setDisplayName(YamlUtils.formatEnumString(tier.name()) + " Chest");
            chest.setItemMeta(meta);

            player.getInventory().addItem(chest);
            CommandUtils.sendAlertMessage(player, "Place this " + YamlUtils.formatEnumString(tier.name()) + " Chest anywhere - you don't need to fill it with items");
        }));
    }

}
