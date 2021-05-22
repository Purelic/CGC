//package net.purelic.CGC.commands.archive.hill;
//
//import cloud.commandframework.Command;
//import cloud.commandframework.arguments.standard.BooleanArgument;
//import cloud.commandframework.arguments.standard.EnumArgument;
//import cloud.commandframework.arguments.standard.IntegerArgument;
//import cloud.commandframework.arguments.standard.StringArgument;
//import cloud.commandframework.bukkit.BukkitCommandManager;
//import cloud.commandframework.bukkit.parsers.MaterialArgument;
//import net.purelic.CGC.core.maps.CustomMap;
//import net.purelic.CGC.core.maps.Hill;
//import net.purelic.CGC.managers.MapManager;
//import net.purelic.CGC.core.maps.MapYaml;
//import net.purelic.CGC.core.maps.constants.HillType;
//import net.purelic.CGC.core.maps.constants.MatchTeam;
//import net.purelic.commons.Commons;
//import net.purelic.commons.commands.parsers.CustomCommand;
//import net.purelic.commons.utils.constants.PermissionType;
//import net.purelic.commons.utils.CommandUtils;
//import org.bukkit.Location;
//import org.bukkit.Material;
//import org.bukkit.World;
//import org.bukkit.command.CommandSender;
//import org.bukkit.entity.Player;
//
//public class HillAddCommand implements CustomCommand {
//
//    @Override
//    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
//        return mgr.commandBuilder("hill")
//                .senderType(Player.class)
//                .literal("add")
//                .argument(StringArgument.quoted("name"))
//                .argument(EnumArgument.optional(HillType.class, "type", HillType.KOTH_HILL))
//                .argument(StringArgument.optional("owner", "Neutral"))
//                .argument(MaterialArgument.optional("material", Material.WOOL))
//                .argument(IntegerArgument.optional("radius", 3))
//                .argument(BooleanArgument.optional("circle", true))
//                .handler(c -> {
//                    Player player = (Player) c.getSender();
//                    World world = player.getWorld();
//                    String name = c.get("name");
//                    Location pLocation = player.getLocation();
//                    Location location = new Location(world, pLocation.getBlockX(), pLocation.getBlockY() - 1, pLocation.getBlockZ());
//                    String ownerArg = c.get("owner");
//                    if (ownerArg.equalsIgnoreCase("neutral")) ownerArg = "solo";
//
//                    if (!MatchTeam.contains(ownerArg)) {
//                        CommandUtils.sendErrorMessage(player, "Cannot find team \"" + ownerArg + "\"!");
//                        return;
//                    }
//
//                    HillType type = c.get("type");
//                    MatchTeam owner = MatchTeam.valueOf(ownerArg.toUpperCase());
//                    Material material = c.get("material");
//                    int radius = c.get("radius");
//                    boolean circle = c.get("circle");
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
//                        Hill hill = new Hill(location, name, type, owner, material, radius, circle);
//                        yaml.addHill(hill);
//                        hill.openBook(player, yaml.getHills().size());
//                        CommandUtils.sendSuccessMessage(player, "You successfully added a hill!");
//                        hill.preview(player);
//                    }
//                });
//    }
//
//}
