package net.purelic.CGC.commands.map;

import cloud.commandframework.ArgumentDescription;
import cloud.commandframework.Command;
import cloud.commandframework.bukkit.BukkitCommandManager;
import cloud.commandframework.types.tuples.Triplet;
import io.leangen.geantyref.TypeToken;
import net.purelic.CGC.managers.MapManager;
import net.purelic.CGC.maps.GenericMapElement;
import net.purelic.CGC.maps.MapElement;
import net.purelic.CGC.maps.MapYaml;
import net.purelic.CGC.maps.NestedMapElement;
import net.purelic.CGC.maps.constants.MapElementType;
import net.purelic.commons.Commons;
import net.purelic.commons.commands.parsers.CustomCommand;
import net.purelic.commons.commands.parsers.Permission;
import net.purelic.commons.utils.CommandUtils;
import net.purelic.commons.utils.YamlUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

public class OffsetCommand implements CustomCommand {

    @Override
    public Command.Builder<CommandSender> getCommandBuilder(BukkitCommandManager<CommandSender> mgr) {
        return mgr.commandBuilder("offset", "adjust")
            .senderType(Player.class)
            .permission(Permission.isMapDev(true))
            .argumentTriplet(
                "offset",
                TypeToken.get(Vector.class),
                Triplet.of("x", "y", "z"),
                Triplet.of(Integer.class, Integer.class, Integer.class),
                (sender, triplet) -> new Vector(triplet.getFirst(), triplet.getSecond(), triplet.getThird()),
                ArgumentDescription.of("Offset")
            )
            .handler(c -> {
                Player player = (Player) c.getSender();
                World world = player.getWorld();
                Vector offset = c.get("offset");

                if (world == Commons.getLobby()) {
                    CommandUtils.sendErrorMessage(player, "Please join the map you want to edit the config of!");
                    return;
                }

                MapYaml yaml = MapManager.getMap(player.getWorld().getName()).getYaml();

                for (MapElementType elementType : MapElementType.values()) {
                    List<MapElement> mapElements = yaml.getMapElements(elementType);

                    for (MapElement element : mapElements) {
                        if (element instanceof GenericMapElement) continue;

                        if (element instanceof NestedMapElement) {
                            ((NestedMapElement<?>) element).offsetLocations(world, offset);
                        } else {
                            Location location = YamlUtils.getLocationFromCoords(world, element.getLocation());
                            location.add(offset);
                            element.setLocation(location);
                        }
                    }
                }

                CommandUtils.sendSuccessMessage(player, "You successfully offset all the map elements!");
            });
    }

}
