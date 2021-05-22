package net.purelic.CGC.commands.world;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.standard.IntegerArgument;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.commons.Commons;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.commands.parsers.Permission;
import net.purelic.commons.utils.CommandUtils;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TimeCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("time")
            .senderType(Player.class)
            .permission(Permission.isMapDev(true))
            .argument(IntegerArgument.of("time"))
            .handler(c -> {
                Player player = (Player) c.getSender();
                int time = c.get("time");
                World world = player.getWorld();

                if (world == Commons.getLobby()) {
                    CommandUtils.sendErrorMessage(player, "Please join the map you want to change the time of!");
                    return;
                }

                world.setTime(time);
                world.setGameRuleValue("doDaylightCycle", "false");
            });
    }

}
