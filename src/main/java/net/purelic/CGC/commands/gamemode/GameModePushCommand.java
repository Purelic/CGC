package net.purelic.CGC.commands.gamemode;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.CGC.gamemodes.CustomGameMode;
import net.purelic.CGC.managers.GameModeManager;
import net.purelic.CGC.managers.MapManager;
import net.purelic.CGC.maps.CustomMap;
import net.purelic.commons.Commons;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.commands.parsers.Permission;
import net.purelic.commons.utils.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import shaded.com.google.cloud.firestore.QueryDocumentSnapshot;

import java.util.Optional;
import java.util.UUID;

public class GameModePushCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("gamemode", "gm")
            .senderType(Player.class)
            .permission(Permission.isMapDev())
            .literal("push")
            .argument(StringArgument.greedy("game mode"))
            .handler(c -> {
                Player player = (Player) c.getSender();
                String gameModeArg = c.get("game mode");
                CustomGameMode gameMode = GameModeManager.getGameModeByNameOrAlias(gameModeArg);

                if (gameMode == null) {
                    CommandUtils.sendErrorMessage(player, "Can't find game mode \"" + gameModeArg + "\"!");
                    return;
                }

                QueryDocumentSnapshot documentSnapshot = DatabaseUtils.getGameMode(
                    Fetcher.PURELIC_UUID.toString(), gameMode.getName(), false);

                if (documentSnapshot == null) {
                    // Couldn't find game mode based on name (name was changed)
                    // Try again, but with the alias
                    documentSnapshot = DatabaseUtils.getGameMode(
                        Fetcher.PURELIC_UUID.toString(), gameMode.getAlias(), true);
                }

                CustomGameMode replacement;

                if (documentSnapshot == null) {
                    // Couldn't find game mode based on name or alias
                    // Create a new game mode
                    replacement = new CustomGameMode(UUID.randomUUID().toString(), gameMode.toYaml());
                } else {
                    CustomGameMode current = new CustomGameMode(documentSnapshot);
                    replacement = new CustomGameMode(documentSnapshot.getId(), gameMode.toYaml());
                    replacement.setDownloads(current.getDownloads());
                }

                replacement.setAuthor(Fetcher.PURELIC_UUID);
                replacement.setPublic(true);
                DatabaseUtils.updateGameMode(replacement.getId(), replacement.toYaml());

                CommandUtils.sendSuccessMessage(player, "Game mode successfully pushed!");
                PlayerUtils.performCommand(player, "gamemodes");
            });
    }

}
