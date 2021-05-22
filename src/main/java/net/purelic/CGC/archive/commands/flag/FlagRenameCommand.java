//package net.purelic.CGC.commands.archive.flag;
//
//import cloud.commandframework.Command;
//import cloud.commandframework.arguments.standard.IntegerArgument;
//import cloud.commandframework.arguments.standard.StringArgument;
//import cloud.commandframework.bukkit.BukkitCommandManager;
//import net.purelic.CGC.core.maps.Flag;
//import net.purelic.CGC.managers.MapManager;
//import net.purelic.CGC.core.maps.MapYaml;
//import net.purelic.commons.Commons;
//import net.purelic.commons.commands.parsers.CustomCommand;
//import net.purelic.commons.utils.constants.PermissionType;
//import net.purelic.commons.utils.CommandUtils;
//import org.bukkit.World;
//import org.bukkit.command.CommandSender;
//import org.bukkit.entity.Player;
//
//public class FlagRenameCommand implements CustomCommand {
//
//    @Override
//    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
//        return mgr.commandBuilder("flag")
//                .senderType(Player.class)
//                .literal("rename")
//                .argument(IntegerArgument.of("id"))
//                .argument(StringArgument.greedy("name"))
//                .handler(c -> {
//                    Player player = (Player) c.getSender();
//                    int id = c.get("id");
//                    String name = c.get("name");
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
//                        if (id < 1 || id > yaml.getFlags().size()) {
//                            CommandUtils.sendErrorMessage(player, "Invalid flag id!");
//                            return;
//                        }
//
//                        Flag flag = yaml.getFlags().get(id - 1);
//                        flag.setName(name);
//                        yaml.updateYaml();
//                        flag.openBook(player, id);
//
//                        CommandUtils.sendSuccessMessage(player, "Successfully renamed flag to \"" + name + "\"!");
//                    }
//                });
//    }
//
//}
