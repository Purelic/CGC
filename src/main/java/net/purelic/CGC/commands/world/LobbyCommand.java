package net.purelic.CGC.commands.world;

import cloud.commandframework.Command;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.commons.Commons;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.utils.CommandUtils;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LobbyCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("lobby")
            .senderType(Player.class)
            .handler(c -> {
                Player player = (Player) c.getSender();

                if (player.getWorld() == Commons.getLobby()) {
                    CommandUtils.sendErrorMessage(player, "You're already in the Lobby!");
                    return;
                }

                player.teleport(Commons.getLobby().getSpawnLocation());
                player.setGameMode(GameMode.ADVENTURE);
                CommandUtils.sendSuccessMessage(player, "Teleported to the Lobby!");
            });
    }

}
