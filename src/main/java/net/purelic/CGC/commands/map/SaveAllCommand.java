package net.purelic.CGC.commands.map;

import cloud.commandframework.Command;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.CGC.runnables.AutoSave;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.commands.parsers.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SaveAllCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("save-all")
            .senderType(Player.class)
            .permission(Permission.isMapDev(true))
            .handler(context -> mgr.taskRecipe().begin(context).synchronous(c -> {
                AutoSave.saveAll(false, true);
            }).execute());
    }

}
