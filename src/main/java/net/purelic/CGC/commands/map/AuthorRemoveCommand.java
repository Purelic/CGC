package net.purelic.CGC.commands.map;

import cloud.commandframework.Command;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.CGC.managers.MapManager;
import net.purelic.CGC.maps.MapYaml;
import net.purelic.commons.Commons;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.commands.parsers.ProfileArgument;
import net.purelic.commons.profile.Profile;
import net.purelic.commons.utils.CommandUtils;
import net.purelic.commons.utils.Fetcher;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AuthorRemoveCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("author")
            .senderType(Player.class)
            .literal("remove")
            .argument(ProfileArgument.of("player"))
            .handler(c -> {
                Player player = (Player) c.getSender();
                Profile profile = c.get("player");
                World world = player.getWorld();

                if (world == Commons.getLobby()) {
                    CommandUtils.sendErrorMessage(player, "Please join the map you want to edit the config of!");
                    return;
                }

                MapYaml yaml = MapManager.getMap(world.getName()).getYaml();

                if (!Commons.getProfile(player).isMapDev() && !yaml.hasAuthor(player)) {
                    CommandUtils.sendErrorMessage(player, "Only the original map author(s) can remove an author!");
                    return;
                }

                if (yaml.getAuthors().size() == 1) {
                    CommandUtils.sendErrorMessage(player, "You have to have at least one author!");
                } else if (yaml.hasAuthor(profile.getUniqueId())) {
                    yaml.removeAuthor(profile.getUniqueId());
                    CommandUtils.sendSuccessMessage(player, "You successfully removed " + Fetcher.getBasicName(profile) + ChatColor.GREEN + " as an author!");
                } else {
                    CommandUtils.sendErrorMessage(player, Fetcher.getBasicName(profile) + ChatColor.RED + " is not currently an author!");
                }
            });
    }

}
