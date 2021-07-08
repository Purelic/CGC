package net.purelic.CGC.maps;

import cloud.commandframework.Command;
import net.purelic.CGC.managers.MapManager;
import net.purelic.CGC.maps.constants.MapElementType;
import net.purelic.CGC.maps.settings.MapSetting;
import net.purelic.commons.Commons;
import net.purelic.commons.commands.parsers.Permission;
import net.purelic.commons.utils.CommandUtils;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
public abstract class GenericMapElement extends MapElement {

    public GenericMapElement(MapElementType objectType, Map<String, Object> yaml, MapSetting... settings) {
        super(objectType, yaml, settings);
    }

    @Override
    public void registerListCommand(String base) {
        Command.@NonNull Builder<CommandSender> cmdBuilder = Commons.getPlugin().getCommandManager()
            .commandBuilder(base + "s")
            .senderType(Player.class)
            .permission(Permission.isMapDev(true));

        this.registerCommand(cmdBuilder.handler(c -> {
            Player player = (Player) c.getSender();
            World world = player.getWorld();

            if (world == Commons.getLobby()) {
                CommandUtils.sendErrorMessage(player, "Please join the map you want to edit the config of!");
                return;
            }

            MapYaml yaml = MapManager.getMap(player.getWorld().getName()).getYaml();
            List<MapElement> mapElements = yaml.getMapElements(this.elementType);
            MapElement mapElement;

            if (mapElements.size() == 0) {
                mapElement = this.elementType.create(new HashMap<>());
                yaml.addMapElement(this.elementType, mapElement);
            } else {
                mapElement = mapElements.get(0);
            }

            mapElement.openBook(player, 1);
        }));
    }

}
