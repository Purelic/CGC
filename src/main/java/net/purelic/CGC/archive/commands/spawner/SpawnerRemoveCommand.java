//package net.purelic.CGC.commands.archive.spawner;
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
//import org.ocpsoft.prettytime.PrettyTime;
//
//public class SpawnerRemoveCommand implements CustomCommand {
//
//    private final PrettyTime pt = new PrettyTime();
//
//    @Override
//    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
//        return mgr.commandBuilder("spawner")
//                .senderType(Player.class)
//                .literal("remove")
//                .argument(IntegerArgument.of("id"))
//                .handler(c -> {
//                    Player player = (Player) c.getSender();
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
//                        if (id < 1 || id > yaml.getSpawners().size()) {
//                            CommandUtils.sendErrorMessage(player, "Invalid spawner id!");
//                            return;
//                        }
//
//                        yaml.removeSpawner(id - 1);
//                        CommandUtils.sendSuccessMessage(player, "You successfully removed a spawner!");
//                    }
//                });
//    }
//
//}
