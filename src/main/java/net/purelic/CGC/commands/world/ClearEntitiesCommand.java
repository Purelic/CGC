package net.purelic.CGC.commands.world;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.flags.CommandFlag;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.commons.Commons;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.commands.parsers.Permission;
import net.purelic.commons.utils.CommandUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;

public class ClearEntitiesCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("clearentities", "ce", "butcher")
            .senderType(Player.class)
            .permission(Permission.isMapDev(true))
            .flag(CommandFlag.newBuilder("force").withAliases("f").build())
            .handler(c -> {
                Player player = (Player) c.getSender();

                if (player.getWorld() == Commons.getLobby()) {
                    CommandUtils.sendErrorMessage(player, "Please join the map you want to edit the config of!");
                    return;
                }

                int count = 0;

                for (Entity entity : player.getWorld().getEntities()) {
                    if (entity instanceof Player) continue;

                    if (!c.flags().isPresent("force")) {
                        if (entity instanceof ArmorStand
                            || entity instanceof ItemFrame
                            || entity instanceof Painting) continue;
                    }

                    entity.remove();
                    count++;
                }

                CommandUtils.sendSuccessMessage(player, "Cleared a total of " + count + " " + (count == 1 ? "entity" : "entities") +
                    "! To remove armor stands, paintings, and item frames use /butcher -f");
            });
    }

}
