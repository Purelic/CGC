package net.purelic.CGC.commands.world;

import cloud.commandframework.Command;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.commons.Commons;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.commands.parsers.PlayerArgument;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("tp")
            .senderType(Player.class)
            .argument(PlayerArgument.of("player"))
            .handler(context -> mgr.taskRecipe().begin(context).synchronous(c -> {
                Player player = (Player) c.getSender();
                Player target = c.get("player");

                if (target.getWorld() == Commons.getLobby()) {
                    player.setGameMode(GameMode.ADVENTURE);
                } else {
                    player.setGameMode(GameMode.CREATIVE);
                    player.setFlying(true);
                }

                player.teleport(target);
            }).execute());
    }

}
