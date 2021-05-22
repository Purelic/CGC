package net.purelic.CGC.commands.map;

import cloud.commandframework.Command;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.purelic.CGC.maps.constants.MapElementType;
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

public class EditCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("edit")
            .senderType(Player.class)
            .permission(Permission.isMapDev(true))
            .handler(c -> {
                Player player = (Player) c.getSender();

                if (player.getWorld() == Commons.getLobby()) {
                    CommandUtils.sendErrorMessage(player, "Please join the map you want to edit the config of!");
                    return;
                }

                List<BaseComponent[]> pages = new ArrayList<>();
                BookBuilder bookBuilder = new BookBuilder();
                PageBuilder pageBuilder = new PageBuilder();

                int count = 0;

                for (MapElementType elementType : MapElementType.values()) {
                    // every 7 elements we need a new page
                    if (count % 7 == 0 && count > 0) {
                        pages.add(pageBuilder.build());
                        pageBuilder = new PageBuilder();
                    }

                    pageBuilder.add(this.getComponent(elementType)).newLines(2);

                    count++;
                }

                pages.add(pageBuilder.build());
                bookBuilder.pages(pages).open(player);
            });
    }

    private BaseComponent[] getComponent(MapElementType elementType) {
        return new ComponentBuilder(elementType.getName() + "s ➜")
            .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(elementType.getBaseCommand(true) + "s").create()))
            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, elementType.getBaseCommand(true) + "s"))
            .create();
    }

}
