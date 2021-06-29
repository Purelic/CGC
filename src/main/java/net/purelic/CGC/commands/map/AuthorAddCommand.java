package net.purelic.CGC.commands.map;

import cloud.commandframework.Command;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.CGC.managers.MapManager;
import net.purelic.CGC.maps.MapYaml;
import net.purelic.commons.Commons;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.commands.parsers.OfflinePlayerArgument;
import net.purelic.commons.utils.CommandUtils;
import net.purelic.commons.utils.Fetcher;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AuthorAddCommand implements CustomCommand {

    private static final int MAX_AUTHORS = 8;

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("author")
            .senderType(Player.class)
            .literal("add")
            .argument(OfflinePlayerArgument.of("player"))
            .handler(c -> {
                Player player = (Player) c.getSender();
                OfflinePlayer offlinePlayer = c.get("player");
                UUID uuid = offlinePlayer.getUniqueId();
                World world = player.getWorld();

                if (world == Commons.getLobby()) {
                    CommandUtils.sendErrorMessage(player, "Please join the map you want to edit the config of!");
                    return;
                }

                MapYaml yaml = MapManager.getMap(world.getName()).getYaml();

                if (!Commons.getProfile(player).isMod() && !yaml.hasAuthor(player)) {
                    CommandUtils.sendErrorMessage(player, "Only the original map author(s) can remove an author!");
                    return;
                }

                if (yaml.getAuthors().size() >= MAX_AUTHORS) {
                    CommandUtils.sendErrorMessage(player, "You can only have up to " + MAX_AUTHORS + " map authors!");
                } else if (yaml.hasAuthor(uuid)) {
                    CommandUtils.sendErrorMessage(player, Fetcher.getBasicName(uuid) + ChatColor.RED + " is already an author!");
                } else {
                    yaml.addAuthor(uuid);
                    CommandUtils.sendSuccessMessage(player, "You successfully added " + Fetcher.getBasicName(uuid) + ChatColor.GREEN + " as an author!");
                }
            });
    }

}
