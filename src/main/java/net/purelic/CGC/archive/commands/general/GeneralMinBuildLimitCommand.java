//package net.purelic.CGC.commands.archive.general;
//
//import cloud.commandframework.Command;
//import cloud.commandframework.arguments.standard.IntegerArgument;
//import cloud.commandframework.bukkit.BukkitCommandManager;
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
//public class GeneralMinBuildLimitCommand implements CustomCommand {
//
//    @Override
//    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
//        return mgr.commandBuilder("settings")
//                .senderType(Player.class)
//                .literal("min_build_limit")
//                .argument(IntegerArgument.<CommandSender>newBuilder("limit").withMin(0).withMax(250).asRequired())
//                .handler(c -> {
//                    Player player = (Player) c.getSender();
//                    int limit = c.get("limit");
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
//                        yaml.getGeneralSettings().setMinBuildLimit(limit);
//                        yaml.updateYaml();
//                        yaml.getGeneralSettings().openBook(player);
//                    }
//                });
//    }
//
//}
