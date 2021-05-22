//package net.purelic.CGC.commands.archive.bed;
//
//import cloud.commandframework.Command;
//import cloud.commandframework.arguments.standard.EnumArgument;
//import cloud.commandframework.bukkit.BukkitCommandManager;
//import net.purelic.CGC.core.maps.CustomBed;
//import net.purelic.CGC.core.maps.CustomMap;
//import net.purelic.CGC.managers.MapManager;
//import net.purelic.CGC.core.maps.MapYaml;
//import net.purelic.CGC.core.maps.constants.BedDefenseType;
//import net.purelic.CGC.core.maps.constants.BedDirection;
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
//public class BedAddCommand implements CustomCommand {
//
//    @Override
//    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
//        return mgr.commandBuilder("bed")
//                .senderType(Player.class)
//                .literal("add")
//                .argument(EnumArgument.of(MatchTeam.class, "owner"))
//                .argument(EnumArgument.optional(BedDirection.class, "direction"))
//                .argument(EnumArgument.optional(BedDefenseType.class, "defense"))
//                .handler(c -> {
//                    Player player = (Player) c.getSender();
//                    World world = player.getWorld();
//                    Location pLocation = player.getLocation();
//                    Location location = new Location(world, pLocation.getBlockX(), pLocation.getBlockY(), pLocation.getBlockZ());
//                    MatchTeam owner = c.get("owner");
//                    BedDirection direction = c.getOrDefault("direction", BedDirection.getBedDirection(pLocation));
//                    BedDefenseType defense = c.getOrDefault("defense", BedDefenseType.NONE);
//
//                    if (!CommandUtils.hasPermission(player, PermissionType.MAP_DEV, true)) {
//                        return;
//                    }
//
//                    if (owner == MatchTeam.SOLO) {
//                        CommandUtils.sendErrorMessage(player, "Beds must be assigned an owning team!");
//                        return;
//                    }
//
//                    if (world == Commons.getLobby()) {
//                        CommandUtils.sendErrorMessage(player, "Please join the map you want to edit the config of!");
//                    } else {
//                        CustomMap map = MapManager.getMap(player.getWorld().getName());
//                        MapYaml yaml = map.getYaml();
//                        CustomBed bed = new CustomBed(location, owner, direction, defense);
//                        yaml.addBed(bed);
//                        bed.openBook(player, yaml.getBeds().size());
//                        CommandUtils.sendSuccessMessage(player, "You successfully added a bed!");
//                        bed.preview(player);
//                    }
//                });
//    }
//
//}
