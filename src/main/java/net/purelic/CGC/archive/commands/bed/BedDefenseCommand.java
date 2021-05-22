//package net.purelic.CGC.commands.archive.bed;
//
//import cloud.commandframework.Command;
//import cloud.commandframework.arguments.standard.EnumArgument;
//import cloud.commandframework.arguments.standard.IntegerArgument;
//import cloud.commandframework.bukkit.BukkitCommandManager;
//import net.purelic.CGC.core.maps.CustomBed;
//import net.purelic.CGC.managers.MapManager;
//import net.purelic.CGC.core.maps.MapYaml;
//import net.purelic.CGC.core.maps.constants.BedDefenseType;
//import net.purelic.commons.Commons;
//import net.purelic.commons.commands.parsers.CustomCommand;
//import net.purelic.commons.utils.CommandUtils;
//import net.purelic.commons.utils.constants.PermissionType;
//import org.bukkit.World;
//import org.bukkit.command.CommandSender;
//import org.bukkit.entity.Player;
//
//public class BedDefenseCommand implements CustomCommand {
//
//    @Override
//    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
//        return mgr.commandBuilder("bed")
//                .senderType(Player.class)
//                .literal("defense")
//                .argument(IntegerArgument.of("id"))
//                .argument(EnumArgument.of(BedDefenseType.class, "defense"))
//                .handler(c -> {
//                    Player player = (Player) c.getSender();
//                    int id = c.get("id");
//                    BedDefenseType defense = c.get("defense");
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
//                        if (id < 1 || id > yaml.getBeds().size()) {
//                            CommandUtils.sendErrorMessage(player, "Invalid bed id!");
//                            return;
//                        }
//
//                        CustomBed bed = yaml.getBeds().get(id - 1);
//                        bed.setDefense(defense);
//                        yaml.updateYaml();
//
//                        bed.openBook(player, id);
//                    }
//                });
//    }
//
//}
