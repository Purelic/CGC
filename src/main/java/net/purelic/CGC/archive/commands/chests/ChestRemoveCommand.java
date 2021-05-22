//package net.purelic.CGC.commands.archive.chests;
//
//import cloud.commandframework.Command;
//import cloud.commandframework.arguments.standard.EnumArgument;
//import cloud.commandframework.arguments.standard.IntegerArgument;
//import cloud.commandframework.bukkit.BukkitCommandManager;
//import net.purelic.CGC.managers.MapManager;
//import net.purelic.CGC.core.maps.MapYaml;
//import net.purelic.CGC.core.maps.constants.ChestTier;
//import net.purelic.CGC.utils.YamlUtils;
//import net.purelic.commons.Commons;
//import net.purelic.commons.commands.parsers.CustomCommand;
//import net.purelic.commons.utils.constants.PermissionType;
//import net.purelic.commons.utils.CommandUtils;
//import org.bukkit.World;
//import org.bukkit.command.CommandSender;
//import org.bukkit.entity.Player;
//
//public class ChestRemoveCommand implements CustomCommand {
//
//    @Override
//    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
//        return mgr.commandBuilder("chest")
//                .senderType(Player.class)
//                .literal("remove")
//                .argument(EnumArgument.of(ChestTier.class, "tier"))
//                .argument(IntegerArgument.of("id"))
//                .handler(c -> {
//                    Player player = (Player) c.getSender();
//                    ChestTier tier = c.get("tier");
//                    int id = c.get("id");
//                    World world = player.getWorld();
//
//                    if (!CommandUtils.hasPermission(player, PermissionType.MAP_DEV, true)) {
//                        return;
//                    }
//
//                    if (world == Commons.getLobby()) {
//                        CommandUtils.sendErrorMessage(player, "Please join the map you want to edit the config of!");
//                    } else {
//                        MapYaml yaml = MapManager.getMap(player.getWorld().getName()).getYaml();
//
//                        if (id < 1 || id > yaml.getChests(tier).size()) {
//                            CommandUtils.sendErrorMessage(player, "Invalid chest id!");
//                            return;
//                        }
//
//                        yaml.removeChest(tier, id - 1);
//                        CommandUtils.sendSuccessMessage(player, "You successfully removed the " + YamlUtils.formatEnumString(tier.name()) + " chest!");
//                    }
//                });
//    }
//
//}
