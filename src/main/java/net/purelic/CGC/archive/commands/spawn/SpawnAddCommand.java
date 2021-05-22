//package net.purelic.CGC.commands.archive.spawn;
//
//import cloud.commandframework.Command;
//import cloud.commandframework.arguments.standard.EnumArgument;
//import cloud.commandframework.bukkit.BukkitCommandManager;
//import net.purelic.CGC.managers.MapManager;
//import net.purelic.CGC.core.maps.MapYaml;
//import net.purelic.CGC.core.maps.constants.MatchTeam;
//import net.purelic.CGC.utils.YamlUtils;
//import net.purelic.commons.Commons;
//import net.purelic.commons.commands.parsers.CustomCommand;
//import net.purelic.commons.utils.constants.PermissionType;
//import net.purelic.commons.utils.CommandUtils;
//import org.bukkit.Location;
//import org.bukkit.World;
//import org.bukkit.command.CommandSender;
//import org.bukkit.entity.Player;
//
//public class SpawnAddCommand implements CustomCommand {
//
//    @Override
//    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
//        return mgr.commandBuilder("spawn")
//                .senderType(Player.class)
//                .literal("add")
//                .argument(EnumArgument.of(MatchTeam.class, "team"))
//                .handler(c -> {
//                    Player player = (Player) c.getSender();
//                    MatchTeam team = c.get("team");
//                    World world = player.getWorld();
//                    Location location = player.getLocation();
//
//                    if (!CommandUtils.hasPermission(player, PermissionType.MAP_DEV, true)) {
//                        return;
//                    }
//
//                    if (world == Commons.getLobby()) {
//                        CommandUtils.sendErrorMessage(player, "Please join the map you want to edit the config of!");
//                    } else {
//                        MapYaml yaml = MapManager.getMap(player.getWorld().getName()).getYaml();
//                        yaml.addSpawn(team, location);
//                        CommandUtils.sendSuccessMessage(player, "You successfully added a " + YamlUtils.formatEnumString(team.name()) + " spawn!");
//                        SpawnsCommand.openBook(player, team, yaml);
//                    }
//                });
//    }
//
//}
