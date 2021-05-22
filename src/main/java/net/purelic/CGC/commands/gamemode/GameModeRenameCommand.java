package net.purelic.CGC.commands.gamemode;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.CGC.gamemodes.CustomGameMode;
import net.purelic.CGC.managers.GameModeManager;
import net.purelic.commons.Commons;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.utils.CommandUtils;
import net.purelic.commons.utils.DatabaseUtils;
import net.purelic.commons.utils.PlayerUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

public class GameModeRenameCommand implements CustomCommand {

    private static final String REGEX = "^[a-zA-Z0-9 ]+$";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("gamemode", "gm")
            .senderType(Player.class)
            .literal("rename")
            .argument(StringArgument.quoted("game mode"))
            .argument(StringArgument.quoted("name"))
            .handler(c -> {
                Player player = (Player) c.getSender();
                String gameModeArg = c.get("game mode");
                String name = c.get("name");

                if (!Commons.isOwner(player)) {
                    CommandUtils.sendErrorMessage(player, "Only the server owner can rename game modes!");
                    return;
                }

                CustomGameMode gameMode = GameModeManager.getGameModeByNameOrAlias(gameModeArg);

                if (gameMode == null) {
                    CommandUtils.sendErrorMessage(player, "Can't find game mode \"" + gameModeArg + "\"!");
                    return;
                }

                if (!PATTERN.matcher(name).matches()) {
                    CommandUtils.sendErrorMessage(player, "Game mode names can only contain alphanumeric values!");
                    return;
                }

                if (name.length() > 32) {
                    CommandUtils.sendErrorMessage(player, "Game mode names can only be 32 characters or less!");
                    return;
                }

                GameModeManager.removeGameMode(gameMode);
                gameMode.setName(name);
                GameModeManager.addGameMode(gameMode);

                DatabaseUtils.updateGameMode(gameMode.getId(), gameMode.toYaml());
                CommandUtils.sendSuccessMessage(player, "Game mode successfully renamed!");
                PlayerUtils.performCommand(player, "gamemodes");
            });
    }

}
