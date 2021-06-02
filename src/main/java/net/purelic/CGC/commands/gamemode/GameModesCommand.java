package net.purelic.CGC.commands.gamemode;

import cloud.commandframework.Command;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.purelic.CGC.gamemodes.CustomGameMode;
import net.purelic.CGC.managers.GameModeManager;
import net.purelic.commons.Commons;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.commands.parsers.Permission;
import net.purelic.commons.utils.CommandUtils;
import net.purelic.commons.utils.book.BookBuilder;
import net.purelic.commons.utils.book.PageBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameModesCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("gamemodes", "gms")
            .senderType(Player.class)
            .permission(Permission.isMapDev(true))
            .handler(c -> {
                Player player = (Player) c.getSender();
                Map<String, CustomGameMode> gameModes = GameModeManager.getGameModes();
                int total = gameModes.size();

                if (total == 0) {
                    CommandUtils.sendErrorMessage(player, "You haven't created any game modes yet! Use /gamemode create <name> <alias>");
                    return;
                }

                List<BaseComponent[]> pages = new ArrayList<>();
                PageBuilder page = new PageBuilder();
                int i = 0;

                for (Map.Entry<String, CustomGameMode> entry : gameModes.entrySet()) {
                    if (i % 10 == 0) {
                        if (i != 0) pages.add(page.build());
                        page = new PageBuilder();
                        page.add(total + " total game mode" + (total == 1 ? "" : "s")).newLine();
                    }

                    CustomGameMode gameMode = entry.getValue();
                    String name = gameMode.getName();
                    String alias = gameMode.getAlias();
                    String fullName = "(" + alias + ") " + name;
                    String hover = "Edit " + fullName;
                    String command = "/gamemode edit " + alias;
                    ChatColor color = gameMode.isPublic() ? ChatColor.DARK_GREEN : ChatColor.BLACK;

                    page.newLine()
                        .add(
                            new ComponentBuilder((i + 1) + ". ")
                                .append(fullName.length() >= 11 ? fullName.substring(0, 10) + ".." : fullName)
                                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                    new ComponentBuilder(hover + "\n")
                                        .append(gameMode.isPublic() ? "Published" : "Unpublished")
                                        .color(gameMode.isPublic() ? ChatColor.GREEN : ChatColor.GRAY)
                                        .create()))
                                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command))
                                .color(color)
                                .create()
                        )
                        .add(
                            new ComponentBuilder(" ")
                                .append("✔")
                                .color(ChatColor.DARK_GREEN)
                                .bold(true)
                                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(gameMode.isPublic() ? "SAVE" : "PUBLISH").color(ChatColor.GREEN).create()))
                                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/gamemode " + (gameMode.isPublic() ? "save" : "publish") + " " + alias))
                                .create()
                        );

                    if (Commons.isOwner(player)) {
                        page.add(
                            new ComponentBuilder(" ")
                                .append("✗")
                                .color(ChatColor.DARK_RED)
                                .bold(true)
                                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("DELETE").color(ChatColor.RED).create()))
                                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/delete-confirm gamemode " + alias))
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
