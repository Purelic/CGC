package net.purelic.CGC.commands.gamemode;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.CGC.gamemodes.CustomGameMode;
import net.purelic.CGC.managers.GameModeManager;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.commands.parsers.Permission;
import net.purelic.commons.utils.CommandUtils;
import net.purelic.commons.utils.DatabaseUtils;
import net.purelic.commons.utils.PlayerUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GameModeSaveCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("gamemode", "gm")
            .senderType(Player.class)
            .permission(Permission.isMapDev(true))
            .literal("save")
            .argument(StringArgument.greedy("game mode"))
            .handler(c -> {
                Player player = (Player) c.getSender();
                String gameModeArg = c.get("game mode");
                CustomGameMode gameMode = GameModeManager.getGameModeByNameOrAlias(gameModeArg);

                if (gameMode == null) {
                    CommandUtils.sendErrorMessage(player, "Can't find game mode \"" + gameModeArg + "\"!");
                    return;
                }

                DatabaseUtils.updateGameMode(gameMode.getId(), gameMode.toYaml());
                CommandUtils.sendSuccessMessage(player, "Game mode successfully saved!");
                PlayerUtils.performCommand(player, "gamemodes");
            });
    }

}
