package net.purelic.CGC.commands.map;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.flags.CommandFlag;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.purelic.CGC.managers.MapManager;
import net.purelic.commons.Commons;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.utils.CommandUtils;
import net.purelic.commons.utils.NickUtils;
import net.purelic.commons.utils.book.BookBuilder;
import net.purelic.commons.utils.book.PageBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MapsCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("maps")
            .senderType(Player.class)
            .flag(CommandFlag.newBuilder("published").withAliases("p"))
            .handler(c -> {
                Player player = (Player) c.getSender();
                boolean published = c.flags().isPresent("published");
                Set<String> maps = published ? MapManager.getPublishedMaps() : MapManager.getMapNames();
                int total = maps.size();

                if (total == 0) {
                    if (published) {
                        CommandUtils.sendErrorMessage(player, "You haven't published any maps yet! Use /publish");
                    } else {
                        CommandUtils.sendErrorMessage(player, "You haven't created any maps yet! Use /create <map name>");
                    }
                    return;
                }

                List<BaseComponent[]> pages = new ArrayList<>();
                PageBuilder page = new PageBuilder();
                int i = 0;

                for (String map : maps) {
                    if (i % 10 == 0) {
                        if (i != 0) pages.add(page.build());
                        page = new PageBuilder();
                        page.add(total + " " + (published ? "published " : "") + "map" + (total == 1 ? "" : "s")).newLine();
                    }

                    boolean downloaded = !published && MapManager.isDownloaded(map);
                    String hover = downloaded ? "Teleport to" : (published ? "Pull" : "Download");
                    String command = downloaded ? "map " + map : (published ?
                        "pull \"" + map + "\" " + NickUtils.getRealName(player) : "download " + map);
                    ChatColor color = downloaded ? ChatColor.DARK_GREEN : ChatColor.BLACK;

                    page.newLine()
                        .add(
                            new ComponentBuilder((i + 1) + ". ")
                                .append(map.length() >= 14 ? map.substring(0, 13) + ".." : map)
                                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hover + " " + map).create()))
                                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + command))
                                .color(color)
                                .create()
                        );

                    if (Commons.isOwner(player)) {
                        page.add(
                            new ComponentBuilder("  ")
                                .append("✗")
                                .color(ChatColor.DARK_RED)
                                .bold(true)
                                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("DELETE").color(ChatColor.RED).create()))
                                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/delete-confirm map \"" + map + "\"" + (published ? " --published" : "")))
                                .create()
                        );
                    }

                    i++;
                }

                pages.add(page.build());

                new BookBuilder().pages(pages).open(player);
            });
    }

}
