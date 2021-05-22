package net.purelic.CGC.commands.map;

import cloud.commandframework.Command;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.commons.Commons;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.commands.parsers.Permission;
import net.purelic.commons.utils.CommandUtils;
import net.purelic.commons.utils.EntityUtils;
import net.purelic.commons.utils.YamlUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class ShopCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("shop")
            .senderType(Player.class)
            .permission(Permission.isMapDev(true))
            .handler(c -> {
                Player player = (Player) c.getSender();

                if (player.getWorld() == Commons.getLobby()) {
                    CommandUtils.sendErrorMessage(player, "Please join the map you want to edit the config of!");
                    return;
                }

                Entity villager = player.getWorld().spawnEntity(this.centerLocation(player.getLocation()), EntityType.VILLAGER);
                EntityUtils.setAi(villager, false);
                CommandUtils.sendSuccessMessage(player, "You spawned a shop villager! This villager will not move.");
            });
    }

    private Location centerLocation(Location loc) {
        return new Location(
            loc.getWorld(),
            loc.getBlockX() + 0.5,
            loc.getY(),
            loc.getBlockZ() + 0.5,
            YamlUtils.roundYaw(loc),
            0
        );
    }

}
