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
import net.purelic.commons.utils.MapUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.regex.Pattern;

public class RenameCommand implements CustomCommand {

    private static final String REGEX = "^[a-zA-Z0-9 -]+$";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("rename")
            .senderType(Player.class)
            .permission(Permission.isMapDev(true))
            .argument(StringArgument.greedy("name"))
            .handler(context -> mgr.taskRecipe().begin(context).synchronous(c -> {
                Player player = (Player) c.getSender();
                String newName = c.get("name");

                if (player.getWorld() == Commons.getLobby()) {
                    CommandUtils.sendErrorMessage(player, "Please join the map you want to rename!");
                    return;
                }

                if (MapManager.hasMap(newName)) {
                    CommandUtils.sendErrorMessage(player, "A map named \"" + newName + "\" already exists!");
                    return;
                } else if (!PATTERN.matcher(newName).matches()) {
                    CommandUtils.sendErrorMessage(player, "Map names can only contain alphanumeric values!");
                    return;
                } else if (newName.length() > 32) {
                    CommandUtils.sendErrorMessage(player, "Map names can only be 32 characters or less!");
                    return;
                } else if (MapUtils.isReservedMapName(newName)) {
                    CommandUtils.sendErrorMessage(player, "You can't use this as a map name!");
                    return;
                } else if (MapUtils.isPublicMapName(newName)) {
                    CommandUtils.sendErrorMessage(player, "A map with this name is already in the public playlists! Consider renaming to avoid issue with downloading your own maps on game servers.");
                    return;
                }

                String currentName = player.getWorld().getName();
                CustomMap map = MapManager.getMap(currentName);

                if (MapManager.isPending(currentName)) {
                    CommandUtils.sendErrorMessage(player, "Please wait, this map is currently being modified!");
                    return;
                }

                if (map.hasUnsavedChanges()) {
                    CommandUtils.sendErrorMessage(player, "That map has unsaved changes, please save your map before renaming!");
                    return;
                }

                for (Player online : Bukkit.getOnlinePlayers()) {
                    if (online.getWorld() == map.getWorld()) {
                        online.teleport(Commons.getLobby().getSpawnLocation());
                        online.setGameMode(GameMode.ADVENTURE);
                    }
                }

                CommandUtils.broadcastAlertMessage("Renaming map \"" + currentName + "\"...");
                MapManager.setPending(currentName, true);
                MapUtils.copyMap(currentName, newName, false); // copy map
                new File(Commons.getRoot() + newName + "/uid.dat").delete();
                MapUtils.saveDraft(newName, Commons.getOwnerId());
                MapUtils.deleteMap(Commons.getOwnerId(), currentName, false); // delete old map

                MapManager.renameMap(currentName, newName); // rename map
                MapManager.setPending(currentName, false);
                CommandUtils.broadcastSuccessMessage("Map \"" + currentName + "\" successfully renamed to \"" + newName + "\"!");
            }).execute());
    }

}
