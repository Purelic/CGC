package net.purelic.CGC.commands.map;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.flags.CommandFlag;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.CGC.listeners.MapLoad;
import net.purelic.CGC.managers.MapManager;
import net.purelic.CGC.maps.CustomMap;
import net.purelic.commons.Commons;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.commands.parsers.Permission;
import net.purelic.commons.commands.parsers.ProfileArgument;
import net.purelic.commons.profile.Profile;
import net.purelic.commons.runnables.MapLoader;
import net.purelic.commons.utils.CommandUtils;
import net.purelic.commons.utils.MapUtils;
import net.purelic.commons.utils.TaskUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class PullCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("pull")
            .senderType(Player.class)
            .argument(StringArgument.quoted("map"))
            .argument(ProfileArgument.optional("player"))
            .flag(CommandFlag.newBuilder("lobby").withAliases("l"))
            .handler(context -> mgr.taskRecipe().begin(context).synchronous(c -> {
                Player player = (Player) c.getSender();
                String map = c.get("map");
                Optional<Profile> profileOptional = c.getOptional("player");
                boolean lobby = c.flags().contains("lobby");

                if (Permission.notPremium(c, "Only Premium players can pull maps!")) {
                    return;
                }

                Profile profile = Commons.getProfile(player);
                long totalSlots = 3 + profile.getMapSlots();

                if (MapManager.getMapNames().size() >= totalSlots
                    && !profile.isDonator(true)) {
                    CommandUtils.sendErrorMessage(player, "You've hit the limit of " + totalSlots + " custom maps! " +
                        "Consider buying Premium or Gold to bypass this limit.");
                    return;
                }

                if (profileOptional.isPresent()
                    && !Commons.getProfile(player).isMapDev()
                    && !profileOptional.get().getUniqueId().toString().equals(player.getUniqueId().toString())) {
                    CommandUtils.sendErrorMessage(player, "You can only pull your own published maps!");
                    return;
                }

                // Previously this required a --force flag to override local copies
                // This flag was removed in favor of the optional player argument which confuses the parsing
                // Any code past this check that has MapManger.hasMap() will be redundant
                if (MapManager.hasMap(map)) {
                    CommandUtils.sendErrorMessage(player, "You currently have a map with the same name! " +
                        "Please /delete or /rename your draft copy before pulling");
                    return;
                }

                if (MapManager.isPending(map)) {
                    CommandUtils.sendErrorMessage(player, "Please wait, that map is currently being modified!");
                    return;
                }

                CommandUtils.sendAlertMessage(player, "Pulling map \"" + map + "\"...");
                if (MapManager.hasMap(map)) MapManager.setPending(map, true);

                String downloadedName;

                if (lobby) {
                    downloadedName = MapUtils.downloadLobbyMap(map);
                } else if (profileOptional.isPresent()) {
                    downloadedName = MapUtils.downloadPublishedMap(profileOptional.get().getUniqueId(), map);
                } else {
                    downloadedName = MapUtils.downloadPublicMap(map);
                }

                if (downloadedName == null) {
                    CommandUtils.sendErrorMessage(player, "Could not find map \"" + map + "\"!");
                    if (MapManager.hasMap(map)) MapManager.setPending(map, false);
                    return;
                }

                if (MapManager.hasMap(map) && MapManager.isDownloaded(map)) {
                    World world = MapManager.getMap(map).getWorld();

                    for (Player online : Bukkit.getOnlinePlayers()) {
                        if (online.getWorld() == world) {
                            online.teleport(Commons.getLobby().getSpawnLocation());
                            online.setGameMode(GameMode.ADVENTURE);
                        }
                    }
                }

                if (MapManager.hasMap(map)) {
                    if (MapManager.isDownloaded(map)) MapUtils.deleteWorld(MapManager.getMap(map).getWorld());
                    MapManager.removeMap(map);
                }

                MapLoad.cache(player, downloadedName);
                MapManager.addMap(downloadedName, new CustomMap(downloadedName));
                TaskUtils.runAsync(new MapLoader(downloadedName, false));
            }).execute());
    }

}
