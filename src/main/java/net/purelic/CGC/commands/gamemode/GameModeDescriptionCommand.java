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

public class GameModeDescriptionCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("gamemode", "gm")
            .senderType(Player.class)
            .literal("description")
            .argument(StringArgument.quoted("game mode"))
            .argument(StringArgument.greedy("description"))
            .handler(c -> {
                Player player = (Player) c.getSender();
                String gameModeArg = c.get("game mode");
                String description = c.get("description");

                if (!Commons.isOwner(player)) {
                    CommandUtils.sendErrorMessage(player, "Only the server owner can change game mode descriptions!");
                    return;
                }

                CustomGameMode gameMode = GameModeManager.getGameModeByNameOrAlias(gameModeArg);

                if (gameMode == null) {
                    CommandUtils.sendErrorMessage(player, "Can't find game mode \"" + gameModeArg + "\"!");
                    return;
                }

                gameMode.setDescription(description);
                DatabaseUtils.updateGameMode(gameMode.getId(), gameMode.toYaml());
                CommandUtils.sendSuccessMessage(player, "Game mode description successfully updated!");
                PlayerUtils.performCommand(player, "gamemodes");
            });
    }

}
