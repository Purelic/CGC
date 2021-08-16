package net.purelic.CGC.commands.map;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.flags.CommandFlag;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.CGC.managers.MapManager;
import net.purelic.CGC.maps.CustomMap;
import net.purelic.commons.Commons;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.commands.parsers.Permission;
import net.purelic.commons.utils.CommandUtils;
import net.purelic.commons.utils.MapUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class PushCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("push")
            .senderType(Player.class)
            .permission(Permission.isMapDev())
            .flag(CommandFlag.newBuilder("lobby").withAliases("l"))
            .handler(c -> {
                Player player = (Player) c.getSender();
                Optional<String> mapArg = c.getOptional("map");
                boolean lobby = c.flags().contains("lobby");

                if (player.getWorld() == Commons.getLobby()) {
                    CommandUtils.sendErrorMessage(player, "Please join the map you want to push!");
                    return;
                }

                CustomMap map = MapManager.getMap(player.getWorld().getName());

                if (map == null) {
                    CommandUtils.sendErrorMessage(player, "Could not find map \"" + mapArg + "\"!");
                    return;
                }

                if (!MapManager.isDownloaded(map.getName())) {
                    CommandUtils.sendErrorMessage(player, "That map hasn't been downloaded yet, please download the map before pushing!");
                    return;
                }

                if (map.hasUnsavedChanges()) {
                    CommandUtils.sendErrorMessage(player, "That map has unsaved changes, please save your map before pushing!");
                    return;
                }

                if (!map.getYaml().meetsMinimumRequirements()) {
                    CommandUtils.sendErrorMessage(player, "Your map doesn't meet the minimum spawn requirements! You must have at least 1 solo spawn OR at least 1 blue and red spawn.");
                    return;
                }

                String name = map.getName();

                CommandUtils.sendAlertMessage(player, "Pushing map \"" + name + "\"...");
                MapManager.setPending(name, true);
                MapUtils.pushMap(name, lobby);
                MapManager.setPending(name, false);
                CommandUtils.sendSuccessMessage(player, "Map \"" + name + "\" successfully pushed!");
            });
    }

}
