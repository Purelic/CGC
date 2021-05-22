//package net.purelic.CGC.commands.map;
//
//import cloud.commandframework.Command;
//import cloud.commandframework.bukkit.BukkitCommandManager;
//import net.md_5.bungee.api.chat.ComponentBuilder;
//import net.md_5.bungee.api.chat.HoverEvent;
//import net.purelic.CGC.maps.CustomMap;
//import net.purelic.CGC.maps.MapYaml;
//import net.purelic.CGC.managers.MapManager;
//import net.purelic.commons.Commons;
//import net.purelic.commons.commands.parsers.CustomCommand;
//import net.purelic.commons.commands.parsers.Permission;
//import net.purelic.commons.utils.CommandUtils;
//import net.purelic.commons.utils.book.BookBuilder;
//import net.purelic.commons.utils.book.PageBuilder;
//import org.bukkit.command.CommandSender;
//import org.bukkit.entity.Player;
//import org.ocpsoft.prettytime.PrettyTime;
//
//public class InfoCommand implements CustomCommand {
//
//    private final PrettyTime pt = new PrettyTime();
//
//    @Override
//    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
//        return mgr.commandBuilder("info")
//            .senderType(Player.class)
//            .permission(Permission.isMapDev(true))
//            .handler(c -> {
//                Player player = (Player) c.getSender();
//
//                if (player.getWorld() == Commons.getLobby()) {
//                    CommandUtils.sendErrorMessage(player, "Please join the map you want to edit the config of!");
//                } else {
//                    CustomMap map = MapManager.getMap(player.getWorld().getName());
//                    MapYaml yaml = map.getYaml();
//
//                    new BookBuilder()
//                        .pages(
//                            new PageBuilder()
//                                .add("Name: ").newLine()
//                                .add(map.getName()).newLine().newLine()
//                                .add("Created: ").newLine()
//                                .add(new ComponentBuilder(this.pt.format(yaml.getCreated()))
//                                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(yaml.getCreated().toString()).create()))
//                                    .create()).newLine()
//                                .add("Updated: ").newLine()
//                                .add(new ComponentBuilder(this.pt.format(yaml.getUpdated()))
//                                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(yaml.getUpdated().toString()).create()))
//                                    .create()).newLine()
//                                .build()
//                        )
//                        .open(player);
//                }
//            });
//    }
//
//}
