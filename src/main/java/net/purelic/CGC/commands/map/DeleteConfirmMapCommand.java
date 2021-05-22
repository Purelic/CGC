package net.purelic.CGC.commands.map;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.flags.CommandFlag;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.purelic.CGC.managers.MapManager;
import net.purelic.commons.Commons;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.utils.CommandUtils;
import net.purelic.commons.utils.book.BookBuilder;
import net.purelic.commons.utils.book.PageBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeleteConfirmMapCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("delete-confirm")
            .senderType(Player.class)
            .literal("map")
            .argument(StringArgument.quoted("map"))
            .flag(CommandFlag.newBuilder("published").withAliases("p"))
            .handler(c -> {
                Player player = (Player) c.getSender();
                boolean published = c.flags().isPresent("published");
                String map = c.get("map");

                if (!Commons.isOwner(player)) {
                    CommandUtils.sendErrorMessage(player, "Only the server owner can delete maps!");
                    return;
                }

                if ((!published && !MapManager.hasMap(map))
                    || (published && !MapManager.hasPublishedMap(map))) {
                    CommandUtils.sendErrorMessage(player, "Can't find map \"" + map + "\"");
                    return;
                }

                String mapName = published ? MapManager.getPublishedMap(map) : MapManager.getMap(map).getName();

                new BookBuilder()
                    .pages(
                        new PageBuilder()
                            .add("Are you sure you want to delete \"" + mapName + "\"?").newLine().newLine()
                            .add(new ComponentBuilder("✗ DELETE")
                                .color(ChatColor.DARK_RED)
                                .bold(true)
                                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Cannot be undone!").color(ChatColor.RED).create()))
                                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/delete map \"" + map + "\"" + (published ? " --published" : "")))
                                .create()).newLine().newLine()
                            .add(new ComponentBuilder("✔ KEEP")
                                .color(ChatColor.DARK_GREEN)
                                .bold(true)
                                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to Cancel").create()))
                                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/maps"))
                                .create())
                            .build()
                    )
                    .open(player);
            });
    }

}
