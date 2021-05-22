package net.purelic.CGC.commands.map;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.standard.StringArgument;
import cloud.commandframework.bukkit.BukkitCommandManager;
import net.purelic.CGC.managers.MapManager;
import net.purelic.CGC.maps.CustomMap;
import net.purelic.commons.Commons;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.commands.parsers.Permission;
import net.purelic.commons.utils.CommandUtils;
import net.purelic.commons.utils.MapUtils;
import net.purelic.commons.utils.NullChunkGenerator;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.regex.Pattern;

public class CopyCommand implements CustomCommand {

    private static final String REGEX = "^[a-zA-Z0-9 -]+$";
    private static final Pattern PATTERN = Pattern.compile(REGEX);

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("copy")
            .senderType(Player.class)
            .permission(Permission.isMapDev(true))
            .argument(StringArgument.greedy("copy name"))
            .handler(context -> mgr.taskRecipe().begin(context).synchronous(c -> {
                Player player = (Player) c.getSender();
                String copyName = c.get("copy name");

                if (player.getWorld() == Commons.getLobby()) {
                    CommandUtils.sendErrorMessage(player, "Please join the map you want to copy!");
                    return;
                }

                if (MapManager.getMapNames().size() >= 3
                    && !Commons.getProfile(player).isDonator(true)) {
                    CommandUtils.sendErrorMessage(player, "You've hit the limit of 3 custom maps! " +
                        "Consider buying Premium to bypass this limit.");
                    return;
                }

                if (MapManager.hasMap(copyName)) {
                    CommandUtils.sendErrorMessage(player, "A map named \"" + copyName + "\" already exists!");
                    return;
                } else if (!PATTERN.matcher(copyName).matches()) {
                    CommandUtils.sendErrorMessage(player, "Map names can only contain alphanumeric values!");
                    return;
                } else if (copyName.length() > 32) {
                    CommandUtils.sendErrorMessage(player, "Map names can only be 32 characters or less!");
                    return;
                } else if (MapUtils.isReservedMapName(copyName)) {
                    CommandUtils.sendErrorMessage(player, "You can't use this as a map name!");
                    return;
                } else if (MapUtils.isPublicMapName(copyName)) {
                    CommandUtils.sendErrorMessage(player, "A map with this name is already in the public playlists! Consider renaming to avoid issue with downloading your own maps on game servers.");
                    return;
                }

                String name = player.getWorld().getName();
                CustomMap customMap = MapManager.getMap(name);

                if (MapManager.isPending(name)) {
                    CommandUtils.sendErrorMessage(player, "Please wait, this map is currently being modified!");
                    return;
                }

                if (customMap.hasUnsavedChanges()) {
                    CommandUtils.sendErrorMessage(player, "This map has unsaved changes, please save your map before copying!");
                    return;
                }

                CommandUtils.broadcastAlertMessage("Copying map \"" + name + "\" as \"" + copyName + "\"...");
                MapManager.setPending(name, true);

                MapUtils.copyMap(name, copyName, false); // copy map
                new File(Commons.getRoot() + copyName + "/uid.dat").delete();
                MapUtils.saveDraft(copyName, Commons.getOwnerId());

                World world = (new WorldCreator(copyName)).generator(new NullChunkGenerator()).createWorld();

                player.setGameMode(GameMode.CREATIVE);
                player.setFlying(true);
                player.teleport(world.getSpawnLocation().clone().add(0.5, 0, 0.5));

                CustomMap newMap = new CustomMap(copyName);
                newMap.setWorld(world);
                MapManager.addMap(copyName, newMap);

                MapManager.setPending(name, false);
                CommandUtils.broadcastSuccessMessage("Map \"" + name + "\" successfully copied to \"" + copyName + "\"!");
            }).execute());
    }

}
