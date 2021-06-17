package net.purelic.CGC.commands.map;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.CGC.managers.MapManager;
import net.purelic.CGC.maps.CustomMap;
import net.purelic.commons.Commons;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.utils.CommandUtils;
import net.purelic.commons.utils.FileUtils;
import net.purelic.commons.utils.MapUtils;
import net.purelic.commons.utils.NullChunkGenerator;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.regex.Pattern;

public class CreateCommand implements CustomCommand {

    private static final String REGEX = "^[a-zA-Z0-9 -]+$";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("create")
            .senderType(Player.class)
            .argument(StringArgument.greedy("name"))
            .handler(context -> mgr.taskRecipe().begin(context).synchronous(c -> {
                Player player = (Player) c.getSender();
                String name = c.get("name");

                if (!Commons.isOwner(player)) {
                    CommandUtils.sendErrorMessage(player, "Only the server owner can create new maps!");
                    return;
                }

                if (MapManager.getMapNames().size() >= 3
                    && !Commons.getProfile(player).isDonator(true)) {
                    CommandUtils.sendErrorMessage(player, "You've hit the limit of 3 custom maps! " +
                        "Consider buying Premium to bypass this limit.");
                    return;
                } else if (MapManager.hasMap(name)) {
                    CommandUtils.sendErrorMessage(player, "A map named \"" + MapManager.getMap(name).getName() + "\" already exists!");
                    return;
                } else if (!PATTERN.matcher(name).matches()) {
                    CommandUtils.sendErrorMessage(player, "Map names can only contain alphanumeric values!");
                    return;
                } else if (name.length() > 32) {
                    CommandUtils.sendErrorMessage(player, "Map names can only be 32 characters or less!");
                    return;
                } else if (MapUtils.isReservedMapName(name)) {
                    CommandUtils.sendErrorMessage(player, "You can't use this as a map name!");
                    return;
                } else if (MapUtils.isPublicMapName(name)) {
                    CommandUtils.sendErrorMessage(player, "A map with this name is already in the public playlists! Consider renaming to avoid issue with downloading your own maps on game servers.");
                    return;
                }

                File template = new File(Commons.getRoot() + "void");
                File map = new File(Commons.getRoot() + name);
                FileUtils.copyDirectory(template, map);

                World world = (new WorldCreator(name)).generator(new NullChunkGenerator()).createWorld();

                player.setGameMode(GameMode.CREATIVE);
                player.setFlying(true);
                player.teleport(world.getSpawnLocation().clone().add(0.5, 0, 0.5));

                CustomMap customMap = new CustomMap(name, world);
                customMap.createYaml(player);
                MapManager.addMap(name, customMap);
                CommandUtils.broadcastSuccessMessage("Successfully created map \"" + name + "\"!");
            }).execute());
    }

}
