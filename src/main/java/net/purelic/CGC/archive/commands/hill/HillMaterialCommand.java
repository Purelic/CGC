//package net.purelic.CGC.commands.archive.hill;
//
//import cloud.commandframework.Command;
//import cloud.commandframework.arguments.standard.IntegerArgument;
//import cloud.commandframework.bukkit.BukkitCommandManager;
//import cloud.commandframework.bukkit.parsers.MaterialArgument;
//import net.purelic.CGC.managers.MapManager;
//import net.purelic.CGC.core.maps.MapYaml;
//import net.purelic.CGC.core.maps.Hill;
//import net.purelic.commons.Commons;
//import net.purelic.commons.commands.parsers.CustomCommand;
//import net.purelic.commons.utils.constants.PermissionType;
//import net.purelic.commons.utils.CommandUtils;
//import org.bukkit.Material;
//import org.bukkit.World;
//import org.bukkit.command.CommandSender;
//import org.bukkit.entity.Player;
//
//public class HillMaterialCommand implements CustomCommand {
//
//    @Override
//    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
//        return mgr.commandBuilder("hill")
//                .senderType(Player.class)
//                .literal("material")
//                .argument(IntegerArgument.of("id"))
//                .argument(MaterialArgument.of("material"))
//                .handler(c -> {
//                    Player player = (Player) c.getSender();
//                    int id = c.get("id");
//                    Material material = c.get("material");
//                    World world = player.getWorld();
//
//                    if (!CommandUtils.hasPermission(player, PermissionType.MAP_DEV, true)) {
//                        return;
//                    }
//
//                    if (material != Material.WOOL && material != Material.STAINED_CLAY && material != Material.STAINED_GLASS) {
//                        CommandUtils.sendErrorMessage(player, "That's not a valid material for hills!");
//                        return;
//                    }
//
//                    if (world == Commons.getLobby()) {
//                        CommandUtils.sendErrorMessage(player, "Please join the map you want to edit the config of!");
//                    } else {
//                        MapYaml yaml = MapManager.getMap(player.getWorld().getName()).getYaml();
//
//                        if (id < 1 || id > yaml.getHills().size()) {
//                            CommandUtils.sendErrorMessage(player, "Invalid hill id!");
//                            return;
//                        }
//
//                        Hill hill = yaml.getHills().get(id - 1);
//                        hill.setMaterial(material);
//                        yaml.updateYaml();
//
//                        hill.openBook(player, id);
//                    }
//                });
//    }
//
//}
