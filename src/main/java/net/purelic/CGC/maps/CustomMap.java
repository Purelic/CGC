package net.purelic.CGC.maps;

import net.purelic.CGC.managers.MapManager;
import net.purelic.CGC.maps.constants.MapElementType;
import net.purelic.commons.Commons;
import net.purelic.commons.utils.CommandUtils;
import net.purelic.commons.utils.MapUtils;
import net.purelic.commons.utils.YamlUtils;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomMap implements Listener {

    private String name;
    private World world;
    private boolean unsavedChanges;
    private final List<Player> players;
    private MapYaml yaml;

    public CustomMap(String name) {
        this(name, null);
    }

    public CustomMap(String name, World world) {
        this.name = name;
        this.world = world;
        this.unsavedChanges = false;
        this.players = new ArrayList<>();
        Commons.registerListener(this);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setWorld(World world) {
        this.world = world;
        this.yaml = this.loadYaml();
    }

    public World getWorld() {
        return this.world;
    }

    public boolean hasWorld() {
        return this.world != null;
    }

    public void teleport(Player player) {
        YamlUtils.teleportToCoords(player, this.world, this.getYaml().getObsSpawn());
    }

    public boolean hasUnsavedChanges() {
        return this.unsavedChanges;
    }

    public void setUnsavedChanges(boolean unsavedChanges) {
        this.unsavedChanges = unsavedChanges;
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public void clearPreviews() {
        for (MapElementType elementType : MapElementType.values()) {
            this.yaml.getMapElements(elementType).stream()
                .filter(MapElement::isPreviewing)
                .forEach(MapElement::destroyPreview);
        }
    }

    public void createYaml(Player player) {
        this.yaml = new MapYaml(this, player);
    }

    @SuppressWarnings("unchecked")
    private MapYaml loadYaml() {
        try {
            InputStream inputStream = new FileInputStream(Commons.getRoot() + this.name + "/map.yml");
            Yaml yaml = new Yaml();
            MapYaml mapYaml = new MapYaml(this, (Map<String, Object>) yaml.load(inputStream));
            inputStream.close();
            return mapYaml;
        } catch (IOException e) {
            System.out.println("No map.yml file found. Creating a blank one...");
            return new MapYaml(this, new HashMap<>());
        }
    }

    public MapYaml getYaml() {
        return this.yaml;
    }

    public void save() {
        CommandUtils.broadcastAlertMessage("Saving map \"" + this.name + "\"...");
        MapManager.setPending(this.name, true);

        this.clearPreviews();
        this.yaml.updateYaml();
        MapUtils.saveDraft(this.world, Commons.getOwnerId());

        this.setUnsavedChanges(false);
        MapManager.setPending(this.name, false);
        CommandUtils.broadcastSuccessMessage("Map \"" + this.name + "\" successfully saved!");
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (this.world != null && event.getBlock().getWorld() == this.world) {
            this.unsavedChanges = true;
            Player player = event.getPlayer();
            if (!this.players.contains(player)) this.players.add(player);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (this.world != null && event.getBlock().getWorld() == this.world) {
            this.unsavedChanges = true;
            Player player = event.getPlayer();
            if (!this.players.contains(player)) this.players.add(player);
        }
    }

}
