package net.purelic.CGC.commands.map;

import cloud.commandframework.Command;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.md_5.bungee.api.ChatColor;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.utils.ChatUtils;
import net.purelic.commons.utils.text.TextBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandsCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("commands")
            .senderType(Player.class)
            .handler(c -> {
                Player player = (Player) c.getSender();
                player.sendMessage(ChatUtils.getHeader("Basic Commands"));
                player.sendMessage(TextBuilder.bullet().build(), TextBuilder.of("/create <map name>").color(ChatColor.AQUA).build(), TextBuilder.of(" - Create a new map draft/void world").build());
                player.sendMessage(TextBuilder.bullet().build(), TextBuilder.of("/maps").color(ChatColor.AQUA).build(), TextBuilder.of(" - List, download, and teleport to your map drafts").build());
                player.sendMessage(TextBuilder.bullet().build(), TextBuilder.of("/edit").color(ChatColor.AQUA).build(), TextBuilder.of(" - Edit the config of your map (spawn points, spawners, etc.)").build());
                player.sendMessage(TextBuilder.bullet().build(), TextBuilder.of("/publish").color(ChatColor.AQUA).build(), TextBuilder.of(" - Make your map public for anyone to download").build());
                player.sendMessage(TextBuilder.bullet().build(), TextBuilder.of("/published").color(ChatColor.AQUA).build(), TextBuilder.of(" - List your published maps").build());
            });
    }

}
