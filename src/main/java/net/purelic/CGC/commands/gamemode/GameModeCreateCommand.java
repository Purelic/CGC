package net.purelic.CGC.commands.gamemode;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.standard.EnumArgument;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.CGC.gamemodes.CustomGameMode;
import net.purelic.CGC.gamemodes.constants.GameType;
import net.purelic.CGC.managers.GameModeManager;
import net.purelic.commons.Commons;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.utils.CommandUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.regex.Pattern;

public class GameModeCreateCommand implements CustomCommand {

    private static final String REGEX = "^[a-zA-Z0-9 ]+$";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("gamemode", "gm")
            .senderType(Player.class)
            .literal("create")
            .argument(StringArgument.quoted("name"))
            .argument(StringArgument.of("alias"))
            .argument(EnumArgument.optional(GameType.class, "game type", GameType.DEATHMATCH))
            .handler(c -> {
                Player player = (Player) c.getSender();
                String name = c.get("name");
                String alias = c.get("alias");
                GameType gameType = c.get("game type");

                if (!Commons.isOwner(player)) {
                    CommandUtils.sendErrorMessage(player, "Only the server owner can create game modes!");
                    return;
                }

                if (GameModeManager.getGameModes().size() >= 3
                    && !Commons.getProfile(player).isDonator(true)) {
                    CommandUtils.sendErrorMessage(player, "You've hit the limit of 3 custom game modes! " +
                        "Consider buying Premium to bypassa this limit.");
                    return;
                }

                CustomGameMode gameModeByName = GameModeManager.getGameModeByNameOrAlias(name);
                CustomGameMode gameModeByAlias = GameModeManager.getGameModeByNameOrAlias(alias);

                if (gameModeByName != null) {
                    CommandUtils.sendErrorMessage(player, "Game mode with the name \"" + name + "\" already exists!");
                    return;
                }

                if (gameModeByAlias != null) {
                    CommandUtils.sendErrorMessage(player, "Game mode with the alias \"" + alias + "\" already exists!");
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

                if (!PATTERN.matcher(alias).matches()) {
                    CommandUtils.sendErrorMessage(player, "Aliases can only contain alphanumeric values!");
                    return;
                }

                if (alias.length() > 16) {
                    CommandUtils.sendErrorMessage(player, "Aliases can only be 16 characters or less!");
                    return;
                }

                CustomGameMode gameMode = new CustomGameMode(player, name, alias, gameType);
                GameModeManager.addGameMode(gameMode);
                gameMode.openBook(player);
                CommandUtils.sendSuccessMessage(player, "Game mode successfully created!");
            });
    }

}
