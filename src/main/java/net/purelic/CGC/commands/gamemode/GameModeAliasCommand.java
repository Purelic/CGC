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

public class GameModeAliasCommand implements CustomCommand {

    private static final String REGEX = "^[a-zA-Z0-9]+$";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("gamemode", "gm")
            .senderType(Player.class)
            .literal("alias")
            .argument(StringArgument.quoted("game mode"))
            .argument(StringArgument.of("alias"))
            .handler(c -> {
                Player player = (Player) c.getSender();
                String gameModeArg = c.get("game mode");
                String alias = c.get("alias");

                if (!Commons.isOwner(player)) {
                    CommandUtils.sendErrorMessage(player, "Only the server owner can publish game modes!");
                    return;
                }

                CustomGameMode gameMode = GameModeManager.getGameModeByNameOrAlias(gameModeArg);

                if (gameMode == null) {
                    CommandUtils.sendErrorMessage(player, "Can't find game mode \"" + gameModeArg + "\"!");
                    return;
                }

                if (!PATTERN.matcher(alias).matches()) {
                    CommandUtils.sendErrorMessage(player, "Aliases can only contain alphanumeric values!");
                    return;
                }

                if (alias.length() > 16) {
                    CommandUtils.sendErrorMessage(player, "Aliases can only be 16 characters or less!");
                    return;
                }

                GameModeManager.removeGameMode(gameMode);
                gameMode.setAlias(alias.toUpperCase());
                GameModeManager.addGameMode(gameMode);

                DatabaseUtils.updateGameMode(gameMode.getId(), gameMode.toYaml());

                CommandUtils.sendSuccessMessage(player, "Game mode alias successfully changed!");
                PlayerUtils.performCommand(player, "gamemodes");
            });
    }

}
