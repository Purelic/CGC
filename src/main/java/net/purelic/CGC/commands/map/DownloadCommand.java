package net.purelic.CGC.commands.map;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.CGC.listeners.MapLoad;
import net.purelic.CGC.managers.MapManager;
import net.purelic.commons.Commons;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.commands.parsers.Permission;
import net.purelic.commons.runnables.MapLoader;
import net.purelic.commons.utils.CommandUtils;
import net.purelic.commons.utils.MapUtils;
import net.purelic.commons.utils.TaskUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DownloadCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("download")
            .senderType(Player.class)
            .permission(Permission.isMapDev(true))
            .argument(StringArgument.greedy("map"))
            .handler(c -> {
                Player player = (Player) c.getSender();
                String map = c.get("map");

                if (!MapManager.hasMap(map)) {
                    CommandUtils.sendErrorMessage(player, "Can't find map \"" + map + "\"");
                    return;
                }

                String mapName = MapManager.getMap(map).getName();

                if (MapManager.isPending(map)) {
                    CommandUtils.sendErrorMessage(player, "Please wait, this map is currently being modified!");
                    return;
                }

                if (MapManager.isDownloaded(map)) {
                    CommandUtils.sendErrorMessage(player, "\"" + mapName + "\" is already downloaded!");
                    return;
                }

                CommandUtils.broadcastAlertMessage("Downloading map \"" + mapName + "\"...");
                MapManager.setPending(mapName, true);
                MapLoad.cache(player, mapName);
                MapUtils.downloadDraftMap(Commons.getOwnerId(), mapName);
                TaskUtils.runAsync(new MapLoader(mapName, false));
            });
    }

}
