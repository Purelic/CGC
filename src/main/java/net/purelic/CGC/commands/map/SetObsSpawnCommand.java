package net.purelic.CGC.commands.map;

import cloud.commandframework.Command;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.CGC.managers.MapManager;
import net.purelic.CGC.maps.CustomMap;
import net.purelic.CGC.maps.MapYaml;
import net.purelic.commons.Commons;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.commands.parsers.Permission;
import net.purelic.commons.utils.CommandUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetObsSpawnCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("setobsspawn", "setworldspawn")
            .senderType(Player.class)
            .permission(Permission.isMapDev(true))
            .handler(c -> {
                Player player = (Player) c.getSender();
                World world = player.getWorld();
                Location location = player.getLocation();

                if (world == Commons.getLobby()) {
                    CommandUtils.sendErrorMessage(player, "Please join the map you want to edit the config of!");
                } else {
                    CustomMap map = MapManager.getMap(player.getWorld().getName());
                    MapYaml yaml = map.getYaml();

                    world.setSpawnLocation(location.getBlockX(), location.getBlockY(), location.getBlockZ());
                    yaml.setObsSpawn(location);

                    CommandUtils.sendSuccessMessage(player, "You successfully updated the Observers spawn point!");
                }
            });
    }

}
