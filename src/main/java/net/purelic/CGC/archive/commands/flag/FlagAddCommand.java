//package net.purelic.CGC.commands.archive.flag;
//
//import cloud.commandframework.Command;
//import cloud.commandframework.arguments.standard.EnumArgument;
//import cloud.commandframework.arguments.standard.StringArgument;
//import cloud.commandframework.bukkit.BukkitCommandManager;
//import net.purelic.CGC.core.maps.CustomMap;
//import net.purelic.CGC.core.maps.Flag;
//import net.purelic.CGC.managers.MapManager;
//import net.purelic.CGC.core.maps.MapYaml;
//import net.purelic.CGC.core.maps.constants.FlagDirection;
//import net.purelic.CGC.core.maps.constants.MatchTeam;
//import net.purelic.commons.Commons;
//import net.purelic.commons.commands.parsers.CustomCommand;
//import net.purelic.commons.utils.constants.PermissionType;
//import net.purelic.commons.utils.CommandUtils;
//import org.bukkit.Location;
//import org.bukkit.World;
//import org.bukkit.command.CommandSender;
//import org.bukkit.entity.Player;
//
//public class FlagAddCommand implements CustomCommand {
//
//    @Override
//    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
//        return mgr.commandBuilder("flag")
//                .senderType(Player.class)
//                .literal("add")
//                .argument(StringArgument.quoted("name"))
//                .argument(StringArgument.optional("owner", "Neutral"))
//                .argument(EnumArgument.optional(FlagDirection.class, "direction", FlagDirection.NORTH))
//                .handler(c -> {
//                    Player player = (Player) c.getSender();
//                    World world = player.getWorld();
//                    String name = c.get("name");
//                    Location pLocation = player.getLocation();
//                    Location location = new Location(world, pLocation.getBlockX(), pLocation.getBlockY(), pLocation.getBlockZ());
//                    String ownerArg = c.get("owner");
//                    if (ownerArg.equalsIgnoreCase("neutral")) ownerArg = "solo";
//
//                    if (!MatchTeam.contains(ownerArg)) {
//                        CommandUtils.sendErrorMessage(player, "Cannot find team \"" + ownerArg + "\"!");
//                        return;
//                    }
//
//                    MatchTeam owner = MatchTeam.valueOf(ownerArg.toUpperCase());
//                    FlagDirection direction = c.get("direction");
//
//                    if (!CommandUtils.hasPermission(player, PermissionType.MAP_DEV, true)) {
//                        return;
//                    }
//
//                    if (world == Commons.getLobby()) {
//                        CommandUtils.sendErrorMessage(player, "Please join the map you want to edit the config of!");
//                    } else {
//                        CustomMap map = MapManager.getMap(player.getWorld().getName());
//                        MapYaml yaml = map.getYaml();
//                        Flag flag = new Flag(location, name, owner, direction);
//                        yaml.addFlag(flag);
//                        flag.openBook(player, yaml.getFlags().size());
//                        CommandUtils.sendSuccessMessage(player, "You successfully added a flag!");
//                        flag.preview(player);
//                    }
//                });
//    }
//
//}
