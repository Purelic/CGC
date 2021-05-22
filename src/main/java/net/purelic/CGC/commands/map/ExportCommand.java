package net.purelic.CGC.commands.map;

import cloud.commandframework.Command;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.purelic.CGC.managers.MapManager;
import net.purelic.CGC.maps.CustomMap;
import net.purelic.commons.Commons;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.commands.parsers.Permission;
import net.purelic.commons.utils.CommandUtils;
import net.purelic.commons.utils.MapUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ExportCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("export")
            .senderType(Player.class)
            .handler(c -> {
                Player player = (Player) c.getSender();

                if (player.getWorld() == Commons.getLobby()) {
                    CommandUtils.sendErrorMessage(player, "Please join the map you want to export!");
                    return;
                }

                if (!Commons.getProfile(player).isMapDev()) {
                    if (Permission.notPremium(c, "Only Premium players can export their maps!")) {
                        return;
                    }

                    CustomMap map = MapManager.getMap(player.getWorld().getName());

                    if (!map.getYaml().hasAuthor(player)) {
                        CommandUtils.sendErrorMessage(player, "Only the original map author(s) can export this map!");
                        return;
                    }
                }

                this.sendDownloadLink(player, player.getWorld().getName());
            });
    }

    private void sendDownloadLink(Player player, String map) {
        CustomMap customMap = MapManager.getMap(map);

        if (customMap.hasUnsavedChanges()) {
            CommandUtils.sendErrorMessage(player, "Your map has unsaved changes! Please save before exporting");
            return;
        } else {
            CommandUtils.sendAlertMessage(player, "Generating download link for \"" + map + "\"");
        }

        String url = MapUtils.getDownloadLink(player.getUniqueId(), map);

        if (url == null) {
            CommandUtils.sendErrorMessage(player, "There was an error generating your download link!");
            return;
        }

        CommandUtils.sendSuccessMessage(
            player,
            new ComponentBuilder("Download your map ")
                .color(ChatColor.GREEN)
                .append("here")
                .color(ChatColor.AQUA)
                .underlined(true)
                .event(new ClickEvent(ClickEvent.Action.OPEN_URL, url))
                .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to Download").create()))
                .create());
    }

}
