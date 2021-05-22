package net.purelic.CGC.commands.map;

import cloud.commandframework.Command;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.CGC.runnables.AutoSave;
import net.purelic.commons.commands.parsers.BooleanArgument;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.commands.parsers.Permission;
import net.purelic.commons.utils.CommandUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class AutoSaveCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("auto-save", "as")
            .senderType(Player.class)
            .permission(Permission.isMapDev(true))
            .argument(BooleanArgument.optional("enabled"))
            .handler(c -> {
                Player player = (Player) c.getSender();
                Optional<Boolean> optional = c.getOptional("enabled");

                if (optional.isPresent()) {
                    boolean enabled = optional.get();

                    if (AutoSave.isEnabled() == enabled) {
                        CommandUtils.sendAlertMessage(player, "Auto-save is already turned " + AutoSave.getStatus());
                    } else {
                        AutoSave.setEnabled(enabled);
                    }
                } else {
                    CommandUtils.sendAlertMessage(player, "Auto-save is currently turned " + AutoSave.getStatus());
                }
            });
    }


}
