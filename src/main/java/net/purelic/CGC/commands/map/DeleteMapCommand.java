package net.purelic.CGC.commands.map;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.flags.CommandFlag;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.CGC.managers.MapManager;
import net.purelic.CGC.maps.CustomMap;
import net.purelic.commons.Commons;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.utils.CommandUtils;
import net.purelic.commons.utils.MapUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DeleteMapCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("delete")
            .senderType(Player.class)
            .literal("map")
            .argument(StringArgument.quoted("map"))
            .flag(CommandFlag.newBuilder("published").withAliases("p"))
            .handler(context -> mgr.taskRecipe().begin(context).synchronous(c -> {
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

                if (published) {
                    String name = MapManager.getPublishedMap(map);
                    MapUtils.deleteMap(Commons.getOwnerId(), name, true);
                    MapManager.removePublishedMap(name);
                    CommandUtils.broadcastSuccessMessage("Published map \"" + name + "\" successfully deleted!");
                    return;
                }

                if (MapManager.isPending(map)) {
                    CommandUtils.sendErrorMessage(player, "Please wait, this map is currently being modified!");
                    return;
                }

                CustomMap customMap = MapManager.getMap(map);
                String mapName = customMap.getName();

                if (MapManager.isDownloaded(map)) {
                    World world = customMap.getWorld();

                    for (Player online : Bukkit.getOnlinePlayers()) {
                        if (online.getWorld() == world) {
                            online.teleport(Commons.getLobby().getSpawnLocation());
                            online.setGameMode(GameMode.ADVENTURE);
                        }
                    }
                }

                CommandUtils.broadcastAlertMessage("Deleting map \"" + mapName + "\"...");
                MapManager.setPending(mapName, true);
                MapUtils.deleteMap(Commons.getOwnerId(), mapName, false);
                MapManager.removeMap(mapName);
                CommandUtils.broadcastSuccessMessage("Map \"" + mapName + "\" successfully deleted!");
            }).execute());
    }

}
