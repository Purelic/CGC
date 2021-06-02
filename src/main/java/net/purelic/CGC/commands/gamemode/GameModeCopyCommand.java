package net.purelic.CGC.commands.gamemode;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.CGC.gamemodes.CustomGameMode;
import net.purelic.CGC.managers.GameModeManager;
import net.purelic.commons.Commons;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.commands.parsers.ProfileArgument;
import net.purelic.commons.profile.Profile;
import net.purelic.commons.utils.CommandUtils;
import net.purelic.commons.utils.DatabaseUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import shaded.com.google.cloud.firestore.QueryDocumentSnapshot;

import java.util.UUID;

public class GameModeCopyCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("gamemode", "gm")
            .senderType(Player.class)
            .literal("copy")
            .argument(ProfileArgument.of("player"))
            .argument(StringArgument.quoted("game mode"))
            .argument(StringArgument.quoted("name"))
            .argument(StringArgument.of("alias"))
            .handler(c -> {
                Player sender = (Player) c.getSender();
                Profile profile = c.get("player");
                String gameModeArg = c.get("game mode");
                String name = c.get("name");
                String alias = c.get("alias");

                if (!Commons.isOwner(sender)) {
                    CommandUtils.sendErrorMessage(sender, "Only the server owner can create game modes!");
                    return;
                }

                if (GameModeManager.getGameModes().size() >= 3
                    && !Commons.getProfile(sender).isDonator(true)) {
                    CommandUtils.sendErrorMessage(sender, "You've hit the limit of 3 custom game modes! " +
                        "Consider buying Premium to bypass this limit.");
                    return;
                }

                CustomGameMode gameModeByName = GameModeManager.getGameModeByNameOrAlias(name);
                CustomGameMode gameModeByAlias = GameModeManager.getGameModeByNameOrAlias(alias);

                if (gameModeByName != null) {
                    CommandUtils.sendErrorMessage(sender, "Game mode with the name \"" + name + "\" already exists!");
                    return;
                }

                if (gameModeByAlias != null) {
                    CommandUtils.sendErrorMessage(sender, "Game mode with the alias \"" + alias + "\" already exists!");
                    return;
                }

                if (profile == Commons.getProfile(sender)) {
                    CustomGameMode gameModeToCopy = GameModeManager.getGameModeByName(gameModeArg);

                    if (gameModeToCopy == null) {
                        CommandUtils.sendErrorMessage(sender, "Can't find game mode \"" + gameModeArg + "\"!");
                        return;
                    }

                    CustomGameMode gameMode = new CustomGameMode(sender, name, alias, gameModeToCopy.toYaml());
                    this.createGameMode(sender, gameMode);
                    return;
                }

                UUID author = profile.getUniqueId();
                String authorName = profile.getName();

                QueryDocumentSnapshot doc = DatabaseUtils.getGameMode(author.toString(), gameModeArg, false);

                if (doc == null) {
                    doc = DatabaseUtils.getGameMode(author.toString(), gameModeArg, true);
                }

                if (doc == null) {
                    CommandUtils.sendErrorMessage(sender, "Can't find game mode \"" + gameModeArg + "\" by " + authorName + "!");
                    return;
                }

                CustomGameMode gameMode = new CustomGameMode(sender, name, alias, doc.getData());

                if (!gameMode.isPublic()) {
                    CommandUtils.sendErrorMessage(sender, authorName + " has not made that game mode public!");
                    return;
                }

                this.createGameMode(sender, gameMode);
            });
    }

    private void createGameMode(Player player, CustomGameMode gameMode) {
        gameMode.setPublic(false);
        GameModeManager.addGameMode(gameMode);
        gameMode.openBook(player);
        CommandUtils.sendSuccessMessage(player, "Game mode successfully created!");
    }

}
