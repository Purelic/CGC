package net.purelic.CGC.commands.map;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.CGC.managers.MapManager;
import net.purelic.CGC.maps.CustomMap;
import net.purelic.commons.Commons;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.utils.CommandUtils;
import net.purelic.commons.utils.MapUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class PublishCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("publish")
            .senderType(Player.class)
            .argument(StringArgument.optional("map", StringArgument.StringMode.GREEDY))
            .handler(c -> {
                Player player = (Player) c.getSender();
                Optional<String> mapArg = c.getOptional("map");

                if (!Commons.isOwner(player)) {
                    CommandUtils.sendErrorMessage(player, "Only the server owner can publish their maps!");
                    return;
                }

                CustomMap map;

                if (!mapArg.isPresent()) {
                    if (player.getWorld() == Commons.getLobby()) {
                        CommandUtils.sendErrorMessage(player, "Please join the map you want to publish!");
                        return;
                    }

                    map = MapManager.getMap(player.getWorld().getName());
                } else {
                    map = MapManager.getMap(mapArg.get());
                }

                if (map == null) {
                    CommandUtils.sendErrorMessage(player, "Could not find map \"" + mapArg + "\"!");
                    return;
                }

                if (!MapManager.isDownloaded(map.getName())) {
                    CommandUtils.sendErrorMessage(player, "That map hasn't been downloaded yet, please /download the map before publishing!");
                    return;
                }

                if (map.hasUnsavedChanges()) {
                    CommandUtils.sendErrorMessage(player, "That map has unsaved changes, please /save your map before publishing!");
                    return;
                }

                if (!map.getYaml().meetsMinimumRequirements()) {
                    CommandUtils.sendErrorMessage(player, "Your map doesn't meet the minimum spawn requirements! You must have at least 1 solo spawn OR at least 1 blue and red spawn.");
                    return;
                }

                String name = map.getName();

                CommandUtils.broadcastAlertMessage("Publishing map \"" + name + "\"...");
                MapManager.setPending(name, true);
                MapUtils.publishMap(name, Commons.getOwnerId());
                MapManager.addPublishedMap(name);
                MapManager.setPending(name, false);
                CommandUtils.broadcastSuccessMessage("Map \"" + name + "\" successfully published!");
            });
    }

}
