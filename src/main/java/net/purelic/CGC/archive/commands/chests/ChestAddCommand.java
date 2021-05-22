//package net.purelic.CGC.commands.archive.chests;
//
//import cloud.commandframework.Command;
//import cloud.commandframework.arguments.standard.EnumArgument;
//import cloud.commandframework.bukkit.BukkitCommandManager;
//import net.purelic.CGC.core.maps.constants.ChestTier;
//import net.purelic.CGC.utils.YamlUtils;
//import net.purelic.commons.Commons;
//import net.purelic.commons.commands.parsers.CustomCommand;
//import net.purelic.commons.utils.constants.PermissionType;
//import net.purelic.commons.utils.CommandUtils;
//import org.bukkit.Material;
//import org.bukkit.World;
//import org.bukkit.command.CommandSender;
//import org.bukkit.entity.Player;
//import org.bukkit.inventory.ItemStack;
//import org.bukkit.inventory.meta.ItemMeta;
//
//public class ChestAddCommand implements CustomCommand {
//
//    @Override
//    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
//        return mgr.commandBuilder("chest")
//                .senderType(Player.class)
//                .literal("add")
//                .argument(EnumArgument.of(ChestTier.class, "tier"))
//                .handler(c -> {
//                    Player player = (Player) c.getSender();
//                    ChestTier tier = c.get("tier");
//                    World world = player.getWorld();
//
//                    if (!CommandUtils.hasPermission(player, PermissionType.MAP_DEV, true)) {
//                        return;
//                    }
//
//                    if (world == Commons.getLobby()) {
//                        CommandUtils.sendErrorMessage(player, "Please join the map you want to edit the config of!");
//                    } else {
//                        ItemStack chest = new ItemStack(Material.CHEST);
//                        ItemMeta meta = chest.getItemMeta();
//                        meta.setDisplayName(YamlUtils.formatEnumString(tier.name()) + " Chest");
//                        chest.setItemMeta(meta);
//
//                        player.getInventory().addItem(chest);
//                        CommandUtils.sendAlertMessage(player, "Place this " + YamlUtils.formatEnumString(tier.name()) + " Chest anywhere - you don't need to fill it with items");
//                    }
//                });
//    }
//
//}
