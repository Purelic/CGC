//package net.purelic.CGC.commands.archive.spawn;
//
//import cloud.commandframework.Command;
//import cloud.commandframework.arguments.standard.EnumArgument;
//import cloud.commandframework.arguments.standard.IntegerArgument;
//import cloud.commandframework.bukkit.BukkitCommandManager;
//import net.purelic.CGC.managers.MapManager;
//import net.purelic.CGC.core.maps.MapYaml;
//import net.purelic.CGC.core.maps.constants.MatchTeam;
//import net.purelic.CGC.utils.YamlUtils;
//import net.purelic.commons.Commons;
//import net.purelic.commons.commands.parsers.CustomCommand;
//import net.purelic.commons.utils.constants.PermissionType;
//import net.purelic.commons.utils.CommandUtils;
//import org.bukkit.World;
//import org.bukkit.command.CommandSender;
//import org.bukkit.entity.Player;
//
//public class SpawnInitialCommand implements CustomCommand {
//
//    @Override
//    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
//        return mgr.commandBuilder("spawn")
//                .senderType(Player.class)
//                .literal("initial")
//                .argument(EnumArgument.of(MatchTeam.class, "team"))
//                .argument(IntegerArgument.of("id"))
//                .handler(c -> {
//                    Player player = (Player) c.getSender();
//                    MatchTeam team = c.get("team");
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
//                        if (id < 1 || id > yaml.getSpawns(team).size()) {
//                            CommandUtils.sendErrorMessage(player, "Invalid spawn point id!");
//                            return;
//                        }
//
//                        yaml.getSpawns(team).add(0, yaml.getSpawns(team).remove(id - 1));
//                        yaml.updateYaml();
//                        SpawnsCommand.openBook(player, team, yaml);
//
//                        CommandUtils.sendSuccessMessage(player, "Successfully updated " + YamlUtils.formatEnumString(team.name()) + "'s initial spawn point!");
//                    }
//                });
//    }
//
//}
