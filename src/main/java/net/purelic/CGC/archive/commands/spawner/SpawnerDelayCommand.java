//package net.purelic.CGC.commands.archive.spawner;
//
//import cloud.commandframework.Command;
//import cloud.commandframework.arguments.standard.IntegerArgument;
//import cloud.commandframework.bukkit.BukkitCommandManager;
//import net.purelic.CGC.managers.MapManager;
//import net.purelic.CGC.core.maps.MapYaml;
//import net.purelic.CGC.core.maps.Spawner;
//import net.purelic.commons.Commons;
//import net.purelic.commons.commands.parsers.CustomCommand;
//import net.purelic.commons.utils.constants.PermissionType;
//import net.purelic.commons.utils.CommandUtils;
//import org.bukkit.World;
//import org.bukkit.command.CommandSender;
//import org.bukkit.entity.Player;
//
//public class SpawnerDelayCommand implements CustomCommand {
//
//    @Override
//    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
//        return mgr.commandBuilder("spawner")
//                .senderType(Player.class)
//                .literal("delay")
//                .argument(IntegerArgument.of("id"))
//                .argument(IntegerArgument.<CommandSender>newBuilder("delay").withMin(1).withMax(60).asRequired())
//                .handler(c -> {
//                    Player player = (Player) c.getSender();
//                    int id = c.get("id");
//                    int delay = c.get("delay");
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
//                        Spawner spawner = yaml.getSpawners().get(id - 1);
//                        spawner.setDelay(delay);
//                        yaml.updateYaml();
//
//                        spawner.openBook(player, id);
//                    }
//                });
//    }
//
//}
