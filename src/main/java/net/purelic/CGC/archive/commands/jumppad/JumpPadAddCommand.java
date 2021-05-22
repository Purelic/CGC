//package net.purelic.CGC.commands.archive.jumppad;
//
//import cloud.commandframework.Command;
//import cloud.commandframework.arguments.standard.BooleanArgument;
//import cloud.commandframework.arguments.standard.IntegerArgument;
//import cloud.commandframework.bukkit.BukkitCommandManager;
//import net.purelic.CGC.core.maps.CustomMap;
//import net.purelic.CGC.core.maps.JumpPad;
//import net.purelic.CGC.managers.MapManager;
//import net.purelic.CGC.core.maps.MapYaml;
//import net.purelic.commons.Commons;
//import net.purelic.commons.commands.parsers.CustomCommand;
//import net.purelic.commons.utils.constants.PermissionType;
//import net.purelic.commons.utils.CommandUtils;
//import org.bukkit.Location;
//import org.bukkit.World;
//import org.bukkit.command.CommandSender;
//import org.bukkit.entity.Player;
//
//public class JumpPadAddCommand implements CustomCommand {
//
//    @Override
//    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
//        return mgr.commandBuilder("jumppad")
//            .senderType(Player.class)
//            .literal("add")
//            .argument(IntegerArgument.<CommandSender>newBuilder("power").withMin(3).withMax(10).asOptionalWithDefault("5"))
//            .argument(IntegerArgument.<CommandSender>newBuilder("angle").withMin(15).withMax(90).asOptionalWithDefault("45"))
//            .argument(BooleanArgument.optional("destructive", true))
//            .handler(c -> {
//                Player player = (Player) c.getSender();
//                World world = player.getWorld();
//                Location pLocation = player.getLocation();
//                Location location = new Location(world, pLocation.getBlockX(), pLocation.getBlockY() - 1, pLocation.getBlockZ());
//                int power = c.get("power");
//                int angle = c.get("angle");
//                boolean destructive = c.get("destructive");
//
//                if (!CommandUtils.hasPermission(player, PermissionType.MAP_DEV, true)) {
//                    return;
//                }
//
//                if (world == Commons.getLobby()) {
//                    CommandUtils.sendErrorMessage(player, "Please join the map you want to edit the config of!");
//                } else {
//                    CustomMap map = MapManager.getMap(player.getWorld().getName());
//                    MapYaml yaml = map.getYaml();
//                    JumpPad jumpPad = new JumpPad(location, power, angle, destructive);
//                    yaml.addJumpPad(jumpPad);
//                    jumpPad.openBook(player, yaml.getJumpPads().size());
//                    CommandUtils.sendSuccessMessage(player, "You successfully added a jump pad!");
//                    jumpPad.preview(player);
//                }
//            });
//    }
//
//}
