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
//import org.bukkit.Location;
//import org.bukkit.World;
//import org.bukkit.command.CommandSender;
//import org.bukkit.entity.Player;
//import org.ocpsoft.prettytime.PrettyTime;
//
//public class JumpPadCopyCommand implements CustomCommand {
//
//    private final PrettyTime pt = new PrettyTime();
//
//    @Override
//    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
//        return mgr.commandBuilder("jumppad")
//            .senderType(Player.class)
//            .literal("copy")
//            .argument(IntegerArgument.of("id"))
//            .handler(c -> {
//                Player player = (Player) c.getSender();
//                int id = c.get("id");
//                World world = player.getWorld();
//                Location pLocation = player.getLocation();
//                Location location = new Location(world, pLocation.getBlockX(), pLocation.getBlockY() - 1, pLocation.getBlockZ());
//
//                if (!CommandUtils.hasPermission(player, PermissionType.MAP_DEV, true)) {
//                    return;
//                }
//
//                if (world == Commons.getLobby()) {
//                    CommandUtils.sendErrorMessage(player, "Please join the map you want to edit the config of!");
//                } else {
//                    MapYaml yaml = MapManager.getMap(player.getWorld().getName()).getYaml();
//
//                    if (id < 1 || id > yaml.getJumpPads().size()) {
//                        CommandUtils.sendErrorMessage(player, "Invalid jump pad id!");
//                        return;
//                    }
//
//                    JumpPad toCopy = yaml.getJumpPads().get(id - 1);
//
//                    JumpPad jumpPad = new JumpPad(location, toCopy.getPower(), toCopy.getAngle(), toCopy.isDestructive());
//                    yaml.addJumpPad(jumpPad);
//                    jumpPad.openBook(player, yaml.getSpawners().size());
//                    CommandUtils.sendSuccessMessage(player, "You successfully added a jump pad!");
//                    jumpPad.preview(player);
//                }
//            });
//    }
//
//}
