package net.purelic.CGC.commands.gamemode;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.purelic.CGC.gamemodes.CustomGameMode;
import net.purelic.CGC.managers.GameModeManager;
import net.purelic.commons.Commons;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.utils.CommandUtils;
import net.purelic.commons.utils.book.BookBuilder;
import net.purelic.commons.utils.book.PageBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GameModeDeleteConfirmCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("delete-confirm")
            .senderType(Player.class)
            .literal("gamemode")
            .argument(StringArgument.greedy("game mode"))
            .handler(c -> {
                Player player = (Player) c.getSender();
                String gameModeArg = c.get("game mode");

                if (!Commons.isOwner(player)) {
                    CommandUtils.sendErrorMessage(player, "Only the server owner can delete game modes!");
                    return;
                }

                CustomGameMode gameMode = GameModeManager.getGameModeByNameOrAlias(gameModeArg);

                if (gameMode != null) {
                    new BookBuilder()
                        .pages(
                            new PageBuilder()
                                .add("Are you sure you want to delete \"" + gameMode.getName() + "\"?").newLine().newLine()
                                .add(new ComponentBuilder("✗ DELETE")
                                    .color(ChatColor.DARK_RED)
                                    .bold(true)
                                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Cannot be undone!").color(ChatColor.RED).create()))
                                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/gamemode delete " + gameMode.getAlias()))
                                    .create()).newLine().newLine()
                                .add(new ComponentBuilder("✔ KEEP")
                                    .color(ChatColor.DARK_GREEN)
                                    .bold(true)
                                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to Cancel").create()))
                                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/gamemodes"))
                                    .create())
                                .build()
                        )
                        .open(player);
                } else {
                    CommandUtils.sendErrorMessage(player, "Can't find map \"" + gameModeArg + "\"");
                }
            });
    }

}
