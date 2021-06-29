package net.purelic.CGC.maps;

import cloud.commandframework.Command;
import cloud.commandframework.arguments.standard.IntegerArgument;
import cloud.commandframework.arguments.standard.StringArgument;
import net.purelic.CGC.managers.MapManager;
import net.purelic.CGC.maps.constants.MapElementType;
import net.purelic.CGC.maps.settings.MapSetting;
import net.purelic.commons.Commons;
import net.purelic.commons.utils.CommandUtils;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Map;

public abstract class NamedMapElement extends MapElement {

    protected String name;

    public NamedMapElement(MapElementType objectType, Map<String, Object> yaml, MapSetting... settings) {
        super(objectType, yaml, settings);
        this.name = (String) yaml.get("name");
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getBookName() {
        return this.getName();
    }

    protected void registerRenameCommand(String base) {
        Command.@NonNull Builder<CommandSender> cmdBuilder = this.getCommandBuilder(base, "rename")
            .argument(IntegerArgument.<CommandSender>newBuilder("id").withMin(1))
            .argument(StringArgument.greedy("name"));

        this.registerCommand(cmdBuilder.handler(c -> {
            Player player = (Player) c.getSender();
            World world = player.getWorld();

            if (world == Commons.getLobby()) {
                CommandUtils.sendErrorMessage(player, "Please join the map you want to edit the config of!");
                return;
            }

            int id = c.get("id");

            MapYaml yaml = MapManager.getMap(player.getWorld().getName()).getYaml();
            List<MapElement> mapElements = yaml.getMapElements(this.elementType);

            if (id > mapElements.size()) {
                CommandUtils.sendErrorMessage(player, "Invalid " + this.elementType.getLowerName() + " id!");
                return;
            }

            String name = c.get("name");

            if (name.length() > 24) {
                CommandUtils.sendErrorMessage(player, this.elementType.getName() + " names can only be 24 characters or less!");
                return;
            }

            NamedMapElement namedMapObject = (NamedMapElement) yaml.getMapElements(this.elementType).get(id - 1);
            namedMapObject.setName(name);
            yaml.updateYaml();
            namedMapObject.openBook(player, id);

            CommandUtils.sendSuccessMessage(player, "Successfully renamed " + this.elementType.getLowerName() + " to \"" + name + "\"!");
        }));
    }

}
