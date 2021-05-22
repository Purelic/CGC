//package net.purelic.CGC.commands.archive.spawner;
//
//import cloud.commandframework.Command;
//import cloud.commandframework.bukkit.BukkitCommandManager;
//import net.md_5.bungee.api.ChatColor;
//import net.md_5.bungee.api.chat.BaseComponent;
//import net.md_5.bungee.api.chat.ClickEvent;
//import net.md_5.bungee.api.chat.ComponentBuilder;
//import net.md_5.bungee.api.chat.HoverEvent;
//import net.purelic.CGC.core.maps.CustomMap;
//import net.purelic.CGC.managers.MapManager;
//import net.purelic.CGC.core.maps.Spawner;
//import net.purelic.CGC.utils.YamlUtils;
//import net.purelic.commons.Commons;
//import net.purelic.commons.commands.parsers.CustomCommand;
//import net.purelic.commons.utils.constants.PermissionType;
//import net.purelic.commons.utils.CommandUtils;
//import org.bukkit.World;
//import org.bukkit.command.CommandSender;
//import org.bukkit.entity.Player;
//import org.bukkit.inventory.ItemStack;
//import xyz.upperlevel.spigot.book.BookUtil;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class SpawnersCommand implements CustomCommand {
//
//    @Override
//    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
//        return mgr.commandBuilder("spawners")
//                .senderType(Player.class)
//                .handler(c -> {
//                    Player player = (Player) c.getSender();
//                    World world = player.getWorld();
//
//                    if (!CommandUtils.hasPermission(player, PermissionType.MAP_DEV, true)) {
//                        return;
//                    }
//
//                    if (world == Commons.getLobby()) {
//                        CommandUtils.sendErrorMessage(player, "Please join the map you want to edit the config of!");
//                    } else {
//                        CustomMap map = MapManager.getMap(player.getWorld().getName());
//                        List<Spawner> spawners = map.getYaml().getSpawners();
//
//                        int total = spawners.size();
//
//                        if (total == 0) {
//                            this.openEmptyBook(player);
//                            return;
//                        }
//
//                        List<BaseComponent[]> pages = new ArrayList<>();
//                        BookUtil.PageBuilder page = new BookUtil.PageBuilder();
//
//                        int i = 0;
//
//                        for (Spawner spawner : spawners) {
//                            if (i % 10 == 0) {
//                                if (i != 0) pages.add(page.build());
//                                page = new BookUtil.PageBuilder();
//                                page
//                                        .add(new ComponentBuilder("⬅ ")
//                                                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Back").create()))
//                                                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/edit"))
//                                                .bold(true)
//                                                .create())
//                                        .add(total + " total spawner" + (total == 1 ? "" : "s")).newLine().newLine()
//                                        .add(new ComponentBuilder("+ ADD")
//                                                .color(ChatColor.DARK_GREEN)
//                                                .bold(true)
//                                                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Add Spawner\n/spawner add").create()))
//                                                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/spawner add"))
//                                                .create()).newLine();
//                            }
//
//                            int number = i + 1;
//                            String name = YamlUtils.formatEnumString(spawner.getMaterial().name());
//                            if (spawner.getAmount() > 1) name += " x" + spawner.getAmount();
//                            name += " (" + spawner.getDelay() + "s)";
//                            String command = "/spawner edit " + number;
//                            String hover = "Edit Spawner\n" + name + "\n" + command;
//
//                            page.newLine()
//                                    .add(
//                                        new ComponentBuilder(number + ". ")
//                                            .append(name.length() >= 15 ? name.substring(0, 15) + "..." : name)
//                                            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover).create()))
//                                            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command))
//                                            .create()
//                                    )
//                                    .add(
//                                        new ComponentBuilder("  ")
//                                            .append("✗")
//                                            .color(ChatColor.DARK_RED)
//                                            .bold(true)
//                                            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("DELETE").color(ChatColor.RED).create()))
//                                            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/spawner remove " + number))
//                                            .create()
//                                    );
//
//                            i++;
//                        }
//
//                        pages.add(page.build());
//
//                        ItemStack book = BookUtil.writtenBook().pages(pages).build();
//                        BookUtil.openPlayer(player, book);
//                    }
//                });
//    }
//
//    private void openEmptyBook(Player player) {
//        BookUtil.PageBuilder page = new BookUtil.PageBuilder()
//                .add(new ComponentBuilder("⬅ ")
//                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Back").create()))
//                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/edit"))
//                        .bold(true)
//                        .create())
//                .add("0 total spawners").newLine().newLine()
//                .add(new ComponentBuilder("+ ADD")
//                        .color(ChatColor.DARK_GREEN)
//                        .bold(true)
//                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Add Spawner\n/spawner add").create()))
//                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/spawner add"))
//                        .create()).newLine();
//
//        ItemStack book = BookUtil.writtenBook().pages(page.build()).build();
//        BookUtil.openPlayer(player, book);
//    }
//
//}
