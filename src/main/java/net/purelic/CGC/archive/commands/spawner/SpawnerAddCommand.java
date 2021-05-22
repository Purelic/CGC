//package net.purelic.CGC.commands.archive.spawner;
//
//import cloud.commandframework.Command;
//import cloud.commandframework.arguments.standard.BooleanArgument;
//import cloud.commandframework.arguments.standard.IntegerArgument;
//import cloud.commandframework.bukkit.BukkitCommandManager;
//import cloud.commandframework.bukkit.parsers.MaterialArgument;
//import net.purelic.CGC.core.maps.CustomMap;
//import net.purelic.CGC.managers.MapManager;
//import net.purelic.CGC.core.maps.MapYaml;
//import net.purelic.CGC.core.maps.Spawner;
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
//public class SpawnerAddCommand implements CustomCommand {
//
//    @Override
//    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
//        return mgr.commandBuilder("spawner")
//            .senderType(Player.class)
//            .literal("add")
//            .argument(MaterialArgument.optional("material", Material.GOLDEN_APPLE))
//            .argument(IntegerArgument.optional("delay", 20))
//            .argument(IntegerArgument.optional("amount", 1))
//            .argument(BooleanArgument.optional("infinite", false))
//            .argument(BooleanArgument.optional("hologram", true))
//            .argument(BooleanArgument.optional("start spawned", false))
//            .argument(BooleanArgument.optional("destructive", true))
//            .handler(c -> {
//                Player player = (Player) c.getSender();
//                World world = player.getWorld();
//                Location pLocation = player.getLocation();
//                Location location = new Location(world, pLocation.getBlockX(), pLocation.getBlockY() - 1, pLocation.getBlockZ());
//                Material material = c.get("material");
//                int delay = c.get("delay");
//                int amount = c.get("amount");
//                boolean infinite = c.get("infinite");
//                boolean hologram = c.get("hologram");
//                boolean startSpawned = c.get("start spawned");
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
//                    Spawner spawner = new Spawner(location, material, delay, amount, infinite, hologram, startSpawned, destructive);
//                    yaml.addSpawner(spawner);
//                    spawner.openBook(player, yaml.getSpawners().size());
//                    CommandUtils.sendSuccessMessage(player, "You successfully added a spawner!");
//                    spawner.preview(player);
//                }
//            });
//    }
//
//}
