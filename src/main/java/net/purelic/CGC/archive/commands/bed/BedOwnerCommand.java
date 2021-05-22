//package net.purelic.CGC.commands.archive.bed;
//
//import cloud.commandframework.Command;
//import cloud.commandframework.arguments.standard.IntegerArgument;
//import cloud.commandframework.arguments.standard.StringArgument;
//import cloud.commandframework.bukkit.BukkitCommandManager;
//import net.purelic.CGC.core.maps.CustomBed;
//import net.purelic.CGC.managers.MapManager;
//import net.purelic.CGC.core.maps.MapYaml;
//import net.purelic.CGC.core.maps.constants.MatchTeam;
//import net.purelic.commons.Commons;
//import net.purelic.commons.commands.parsers.CustomCommand;
//import net.purelic.commons.utils.constants.PermissionType;
//import net.purelic.commons.utils.CommandUtils;
//import org.bukkit.World;
//import org.bukkit.command.CommandSender;
//import org.bukkit.entity.Player;
//
//public class BedOwnerCommand implements CustomCommand {
//
//    @Override
//    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
//        return mgr.commandBuilder("bed")
//                .senderType(Player.class)
//                .literal("owner")
//                .argument(IntegerArgument.of("id"))
//                .argument(StringArgument.of("owner"))
//                .handler(c -> {
//                    Player player = (Player) c.getSender();
//                    int id = c.get("id");
//                    String ownerArg = c.get("owner");
//                    if (ownerArg.equalsIgnoreCase("neutral")) ownerArg = "solo";
//
//                    if (!MatchTeam.contains(ownerArg)) {
//                        CommandUtils.sendErrorMessage(player, "Cannot find team \"" + ownerArg + "\"!");
//                        return;
//                    }
//
//                    MatchTeam owner = MatchTeam.valueOf(ownerArg.toUpperCase());
//                    World world = player.getWorld();
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
//                        MapYaml yaml = MapManager.getMap(player.getWorld().getName()).getYaml();
//
//                        if (id < 1 || id > yaml.getBeds().size()) {
//                            CommandUtils.sendErrorMessage(player, "Invalid bed id!");
//                            return;
//                        }
//
//                        CustomBed bed = yaml.getBeds().get(id - 1);
//                        bed.setOwner(owner);
//                        yaml.updateYaml();
//
//                        bed.openBook(player, id);
//                    }
//                });
//    }
//
//}
