package net.purelic.CGC.maps.settings.generic;

import net.purelic.CGC.managers.MapManager;
import net.purelic.CGC.maps.MapElement;
import net.purelic.CGC.maps.MapYaml;
import net.purelic.CGC.maps.constants.MapElementType;
import net.purelic.CGC.maps.settings.MapToggleSetting;
import net.purelic.commons.Commons;
import net.purelic.commons.commands.parsers.Permission;
import net.purelic.commons.utils.CommandUtils;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

public class GenericMapToggleSetting extends MapToggleSetting {

    public GenericMapToggleSetting(String baseCommand, String name, String description, boolean defaultValue) {
        super(baseCommand, name, description, defaultValue);
    }

    @Override
    public void registerCommand(MapElementType objectType) {
        Commons.registerCommand(mgr ->
            mgr.commandBuilder(objectType.getBaseCommand(false))
                .senderType(Player.class)
                .permission(Permission.isMapDev(true))
                .literal(this.getCommandLiteral())
                .argument(this.getArgument())
                .handler(c -> {
                    Player player = (Player) c.getSender();
                    World world = player.getWorld();

                    if (world == Commons.getLobby()) {
                        CommandUtils.sendErrorMessage(player, "Please join the map you want to edit the config of!");
                        return;
                    }

                    MapYaml yaml = MapManager.getMap(player.getWorld().getName()).getYaml();
                    List<MapElement> mapElements = yaml.getMapElements(objectType);

                    MapElement mapElement = mapElements.get(0);
                    mapElement.getSetting(this.name).setCurrent(c.get(this.getCommandArgument()));
                    yaml.updateYaml();
                    mapElement.openBook(player, -1);
                }));
    }

}
