package net.purelic.CGC.commands.gamemode;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.CGC.gamemodes.CustomGameMode;
import net.purelic.CGC.listeners.MapLoad;
import net.purelic.CGC.managers.GameModeManager;
import net.purelic.CGC.managers.MapManager;
import net.purelic.CGC.maps.CustomMap;
import net.purelic.commons.Commons;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.commands.parsers.Permission;
import net.purelic.commons.commands.parsers.ProfileArgument;
import net.purelic.commons.profile.Profile;
import net.purelic.commons.runnables.MapLoader;
import net.purelic.commons.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import shaded.com.google.cloud.firestore.QueryDocumentSnapshot;

import java.util.Optional;

public class GameModePullCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("gamemode", "gm")
            .senderType(Player.class)
            .permission(Permission.isMapDev(true))
            .literal("pull")
            .argument(ProfileArgument.of("player"))
            .argument(StringArgument.greedy("game mode"))
            .handler(context -> mgr.taskRecipe().begin(context).synchronous(c -> {
                Player player = (Player) c.getSender();
                Profile profile = c.get("player");
                String gameModeArg = c.get("game mode");

                if (Permission.notPremium(c, "Only Premium players can pull game modes!")) {
                    return;
                }

                if (GameModeManager.getGameModes().size() >= 3
                    && !Commons.getProfile(player).isDonator(true)) {
                    CommandUtils.sendErrorMessage(player, "You've hit the limit of 3 custom game modes! " +
                        "Consider buying Premium to bypass this limit.");
                    return;
                }

                if (!Commons.getProfile(player).isMapDev()
                    && !profile.getUniqueId().toString().equals(Fetcher.PURELIC_UUID.toString())) {
                    CommandUtils.sendErrorMessage(player, "You can only pull game modes published by PuRelic!");
                    return;
                }

                QueryDocumentSnapshot documentSnapshot = DatabaseUtils.getGameMode(
                    Fetcher.PURELIC_UUID.toString(), gameModeArg, false);

                if (documentSnapshot == null) {
                    // Couldn't find game mode based on name
                    // Try again, but with the alias
                    documentSnapshot = DatabaseUtils.getGameMode(
                        Fetcher.PURELIC_UUID.toString(), gameModeArg, true);
                }

                if (documentSnapshot == null) {
                    CommandUtils.sendErrorMessage(player, "Can't find game mode \"" + gameModeArg + "\" " +
                        "by " + profile.getName() + "!");
                    return;
                }

                CustomGameMode localGameMode = GameModeManager.getGameModeByNameOrAlias(gameModeArg);

                if (localGameMode != null) {
                    CommandUtils.sendErrorMessage(player, "You currently have a game mode with the same name or alias! " +
                        "Please /gm delete or /gm rename and /gm alias your personal copy before pulling.");
                    return;
                }

                CustomGameMode gameMode = new CustomGameMode(documentSnapshot);
                gameMode.setAuthor(player.getUniqueId());
                gameMode.setPublic(false);
                gameMode.setDownloads(0);

                GameModeManager.addGameMode(gameMode);
                CommandUtils.sendSuccessMessage(player, "Game mode successfully pulled!");
                PlayerUtils.performCommand(player, "gamemodes");
            }).execute());
    }

}
