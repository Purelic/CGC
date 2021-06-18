package net.purelic.CGC.maps;

import net.purelic.CGC.maps.constants.ChestTier;
import net.purelic.CGC.maps.constants.MapElementType;
import net.purelic.CGC.maps.constants.MatchTeam;
import net.purelic.commons.Commons;
import net.purelic.commons.utils.YamlUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.Yaml;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

@SuppressWarnings("unchecked")
public class MapYaml {

    private final CustomMap map;
    private final Map<String, Object> yaml;
    private final Date created;
    private Date updated;
    private final List<String> authors;
    private String obsSpawn;
    private final Map<MapElementType, List<MapElement>> mapElements;

    public MapYaml(CustomMap map, Player player) {
        this.map = map;
        this.created = new Date();
        this.updated = this.created;
        this.authors = new ArrayList<>(Collections.singletonList(player.getUniqueId().toString()));
        this.obsSpawn = YamlUtils.locationToString(map.getWorld().getSpawnLocation());
        this.mapElements = new HashMap<>();
        this.yaml = this.updateYaml();
    }

    public MapYaml(CustomMap map, Map<String, Object> yaml) {
        this.map = map;
        this.yaml = yaml;
        this.created = (Date) yaml.getOrDefault("created", new Date());
        this.updated = (Date) yaml.getOrDefault("updated", this.created);
        this.authors = new ArrayList<>((List<String>) this.yaml.getOrDefault("authors", new ArrayList<>(Collections.singletonList(Commons.getOwnerId().toString()))));
        this.obsSpawn = (String) ((Map<String, Object>) this.yaml.getOrDefault("spawns", new HashMap<>())).getOrDefault("obs", "0.5,50,0.5,0");
        this.mapElements = new HashMap<>();
        this.loadMapElements();
    }

    public Date getCreated() {
        return this.created;
    }

    public Date getUpdated() {
        return this.updated;
    }

    public List<String> getAuthors() {
        return this.authors;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean hasAuthor(Player player) {
        return this.hasAuthor(player.getUniqueId());
    }

    public boolean hasAuthor(UUID uuid) {
        return this.authors.contains(uuid.toString());
    }

    public void addAuthor(UUID uuid) {
        this.authors.add(uuid.toString());
        this.map.setUnsavedChanges(true);
    }

    public void removeAuthor(UUID uuid) {
        this.authors.remove(uuid.toString());
        this.map.setUnsavedChanges(true);
    }

    public String getObsSpawn() {
        return this.obsSpawn;
    }

    public void setObsSpawn(Location loc) {
        this.obsSpawn = YamlUtils.locationToString(loc);
        this.map.setUnsavedChanges(true);
    }

    public List<MapElement> getMapElements(MapElementType mapElementType) {
        return this.mapElements.getOrDefault(mapElementType, new ArrayList<>());
    }

    public boolean hasNestedMapElement(MapElementType objectType) {
        return this.mapElements.containsKey(objectType);
    }

    public <E extends Enum<E>> NestedMapElement<E> getNestedMapElement(MapElementType objectType) {
        List<MapElement> mapElements = this.getMapElements(objectType);
        return mapElements.isEmpty() ? null : (NestedMapElement<E>) mapElements.get(0);
    }

    public void addMapElement(MapElementType mapElementType, MapElement mapElement) {
        List<MapElement> mapElements = this.mapElements.getOrDefault(mapElementType, new ArrayList<>());

        if (mapElement instanceof NestedMapElement) {
            if (mapElements.isEmpty()) mapElements.add(mapElement);
        } else {
            mapElements.add(mapElement);
        }

        this.mapElements.put(mapElementType, mapElements);
        this.map.setUnsavedChanges(true);
    }

    public void removeMapElement(MapElementType mapElementType, int id) {
        this.mapElements.get(mapElementType).remove(id);
    }

    private void loadMapElements() {
        for (MapElementType elementType : MapElementType.values()) {
            List<MapElement> mapElements = new ArrayList<>();

            if (!elementType.isNested()) {
                List<Map<String, Object>> yaml = (List<Map<String, Object>>) this.yaml.getOrDefault(elementType.getYamlKey(), new ArrayList<>());
                yaml.forEach(data -> mapElements.add(elementType.create(data)));
            } else {
                Map<String, Object> yaml = (Map<String, Object>) this.yaml.get(elementType.getYamlKey());
                if (yaml != null) mapElements.add(elementType.create(yaml));
            }

            if (!mapElements.isEmpty()) this.mapElements.put(elementType, mapElements);
        }
    }

    public Map<String, Object> updateYaml() {
        this.updated = new Date();

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("created", this.created);
        data.put("updated", this.updated);
        data.put("authors", this.authors);

        for (MapElementType elementType : MapElementType.values()) {
            if (elementType == MapElementType.CHEST && this.hasNestedMapElement(elementType)) {
                this.cleanChests();
            }

            if (elementType.isNested()) {
                Map<String, Object> yaml = new HashMap<>();
                List<MapElement> elements = this.mapElements.getOrDefault(elementType, new ArrayList<>());
                if (elements.size() == 1) yaml = elements.get(0).toYaml();
                if (!yaml.isEmpty()) data.put(elementType.getYamlKey(), yaml);
            } else {
                List<Map<String, Object>> yaml = new ArrayList<>();
                this.mapElements.getOrDefault(elementType, new ArrayList<>()).forEach(element -> yaml.add(element.toYaml()));
                if (!yaml.isEmpty()) data.put(elementType.getYamlKey(), yaml);
            }
        }

        Map<String, Object> spawns = (Map<String, Object>) data.getOrDefault("spawns", new HashMap<>());
        spawns.put("obs", this.obsSpawn);
        data.put("spawns", spawns);

        try {
            Yaml yaml = new Yaml();
            FileWriter writer = new FileWriter(Commons.getRoot() + this.map.getName() + "/map.yml");
            yaml.dump(data, writer);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data;
    }

    public void cleanChests() {
        World world = this.map.getWorld();
        NestedMapElement<ChestTier> nestedMapElement = this.<ChestTier>getNestedMapElement(MapElementType.CHEST);

        if (nestedMapElement == null) return;

        Map<String, Object> yaml = nestedMapElement.toYaml();
        Map<ChestTier, List<String>> toRemove = new HashMap<>();

        for (ChestTier tier : ChestTier.values()) {
            List<String> chests = (List<String>) yaml.getOrDefault(tier.name().toLowerCase(), new ArrayList<>());
            toRemove.put(tier, new ArrayList<>());

            for (String coords : chests) {
                Block block = world.getBlockAt(YamlUtils.getLocationFromCoords(world, coords));

                if (block == null || block.getType() != Material.CHEST) {
                    toRemove.get(tier).add(coords);
                }
            }
        }

        for (Map.Entry<ChestTier, List<String>> entry : toRemove.entrySet()) {
            for (String coords : entry.getValue()) {
                nestedMapElement.getLocations(entry.getKey()).remove(coords);
            }
        }
    }

    public boolean meetsMinimumRequirements() {
        boolean passes = this.hasNestedMapElement(MapElementType.SPAWN);

        if (passes) {
            NestedMapElement<MatchTeam> element = this.getNestedMapElement(MapElementType.SPAWN);

            passes = element.getLocations(MatchTeam.SOLO).size() > 0 ||
                (element.getLocations(MatchTeam.BLUE).size() > 0 && element.getLocations(MatchTeam.RED).size() > 0);
        }

        return passes;
    }

}
