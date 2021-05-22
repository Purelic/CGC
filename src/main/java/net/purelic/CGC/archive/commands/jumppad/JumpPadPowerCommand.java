//package net.purelic.CGC.commands.archive.jumppad;
//
//import cloud.commandframework.Command;
//import cloud.commandframework.arguments.standard.IntegerArgument;
//import cloud.commandframework.bukkit.BukkitCommandManager;
//import net.purelic.CGC.core.maps.JumpPad;
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
//public class JumpPadPowerCommand implements CustomCommand {
//
//    @Override
//    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
//        return mgr.commandBuilder("jumppad")
//                .senderType(Player.class)
//                .literal("power")
//                .argument(IntegerArgument.of("id"))
//                .argument(IntegerArgument.<CommandSender>newBuilder("power").withMin(3).withMax(10).asRequired())
//                .handler(c -> {
//                    Player player = (Player) c.getSender();
//                    int id = c.get("id");
//                    int power = c.get("power");
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
//                        if (id < 1 || id > yaml.getJumpPads().size()) {
//                            CommandUtils.sendErrorMessage(player, "Invalid jump pad id!");
//                            return;
//                        }
//
//                        JumpPad jumpPad = yaml.getJumpPads().get(id - 1);
//                        jumpPad.setPower(power);
//                        yaml.updateYaml();
//
//                        jumpPad.openBook(player, id);
//                    }
//                });
//    }
//
//}
