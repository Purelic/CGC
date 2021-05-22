package net.purelic.CGC.runnables;

import net.purelic.CGC.gamemodes.CustomGameMode;
import net.purelic.CGC.managers.GameModeManager;
import net.purelic.CGC.managers.MapManager;
import net.purelic.CGC.maps.CustomMap;
import net.purelic.commons.Commons;
import net.purelic.commons.utils.CommandUtils;
import net.purelic.commons.utils.DatabaseUtils;
import net.purelic.commons.utils.TaskUtils;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class AutoSave extends BukkitRunnable {

    private static AutoSave instance;
    private static boolean enabled;

    public AutoSave() {
        instance = this;
        enabled = true;
    }

    @Override
    public void run() {
        saveAll(true, false);
    }

    public static void saveAll(boolean auto, boolean force) {
        saveMaps(auto, force);
        saveGameModes(auto);
    }

    private static void saveMaps(boolean auto, boolean force) {
        List<String> downloadedMaps = MapManager.getDownloadedMaps();
        int total = downloadedMaps.size();

        if (total == 0) return;

        CommandUtils.broadcastAlertMessage("Starting " + (auto ? "auto-save" : "manual save") + " for " + total + " map" + (total == 1 ? "" : "s"));

        for (String map : downloadedMaps) {
            CustomMap customMap = MapManager.getMap(map);

            if ((force || customMap.hasUnsavedChanges()) && !MapManager.isPending(map)) {
                customMap.save();
            }
        }

        CommandUtils.broadcastSuccessMessage(total + " map" + (total == 1 ? "" : "s") + " successfully saved!");
    }

    private static void saveGameModes(boolean auto) {
        List<CustomGameMode> unsavedGameModes = GameModeManager.getUnsavedGameModes();
        int total = unsavedGameModes.size();

        if (total == 0) return;

        CommandUtils.broadcastAlertMessage("Starting " + (auto ? "auto-save" : "manual save") + " for " + total + " game mode" + (total == 1 ? "" : "s"));

        for (CustomGameMode gameMode : unsavedGameModes) {
            DatabaseUtils.updateGameMode(gameMode.getId(), gameMode.toYaml());
            gameMode.setUnsavedChanges(false);
        }

        CommandUtils.broadcastSuccessMessage(total + " game mode" + (total == 1 ? "" : "s") + " successfully saved!");
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void setEnabled(boolean enabled) {
        AutoSave.enabled = enabled;

        if (!enabled && TaskUtils.isRunning(instance)) {
            CommandUtils.broadcastAlertMessage("Auto-save is now turned " + AutoSave.getStatus());
            instance.cancel();
        } else {
            CommandUtils.broadcastAlertMessage("Auto-save has been turned " + AutoSave.getStatus() + " and will save all downloaded maps every 10 minutes. Disable with /auto-save false");
            new AutoSave().runTaskTimer(Commons.getPlugin(), 20 * 60 * 10, 20 * 60 * 10);
        }
    }

    public static String getStatus() {
        return (AutoSave.isEnabled() ? ChatColor.GREEN + "On" : ChatColor.RED + "Off") + ChatColor.RESET;
    }

}
