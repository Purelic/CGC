package net.purelic.CGC.commands.map;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.CGC.managers.MapManager;
import net.purelic.CGC.maps.CustomMap;
import net.purelic.commons.Commons;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.commands.parsers.Permission;
import net.purelic.commons.utils.CommandUtils;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class SaveCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("save")
            .senderType(Player.class)
            .permission(Permission.isMapDev(true))
            .argument(StringArgument.optional("map", StringArgument.StringMode.GREEDY))
            .handler(context -> mgr.taskRecipe().begin(context).synchronous(c -> {
                Player player = (Player) c.getSender();
                Optional<String> mapArg = c.getOptional("map");

                World world = player.getWorld();
                String mapName = world.getName();

                if (mapArg.isPresent()) {
                    String name = mapArg.get();

                    if (MapManager.hasMap(name)) {
                        CustomMap map = MapManager.getMap(name);
                        mapName = map.getName();

                        if (MapManager.isPending(name)) {
                            CommandUtils.sendErrorMessage(player, "Please wait, this map is currently being modified!");
                            return;
                        }

                        if (!MapManager.isDownloaded(name)) {
                            CommandUtils.sendErrorMessage(player, "Map not downloaded! Use /download " + mapName);
                            return;
                        }
                    } else {
                        CommandUtils.sendErrorMessage(player, "Can't find map \"" + name + "\"");
                        return;
                    }
                }

                if (world == Commons.getLobby()) {
                    CommandUtils.sendErrorMessage(player, "You cannot save the Lobby!");
                    return;
                }

                String finalMapName = mapName;
                CustomMap map = MapManager.getMap(finalMapName);

                if (MapManager.isPending(finalMapName)) {
                    CommandUtils.sendErrorMessage(player, "Please wait, this map is currently being modified!");
                    return;
                }

                map.save();
            }).execute());
    }

}
