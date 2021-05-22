//package net.purelic.CGC.core.maps;
//
//import net.purelic.CGC.core.maps.constants.ChestTier;
//import net.purelic.CGC.core.maps.objects.MapObject;
//import net.purelic.CGC.core.maps.constants.MapObjectType;
//import net.purelic.CGC.core.maps.objects.NestedMapObject;
//import net.purelic.commons.Commons;
//import net.purelic.commons.utils.YamlUtils;
//import org.bukkit.Location;
//import org.bukkit.Material;
//import org.bukkit.World;
//import org.bukkit.block.Block;
//import org.bukkit.entity.Player;
//import org.yaml.snakeyaml.Yaml;
//
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.*;
//
//@SuppressWarnings("unchecked")
//public class MapYaml {
//
//    private final CustomMap map;
//    private final Map<String, Object> yaml;
//    private final Date created;
//    private Date updated;
//    private final List<String> authors;
//    private String obsSpawn;
////    private final List<String> soloSpawns;
////    private final List<String> blueSpawns;
////    private final List<String> redSpawns;
////    private final List<String> greenSpawns;
////    private final List<String> yellowSpawns;
////    private final List<String> aquaSpawns;
////    private final List<String> pinkSpawns;
////    private final List<String> whiteSpawns;
////    private final List<String> graySpawns;
//    // private final List<Spawner> spawners;
//    // private final List<Hill> hills;
//    // private final List<CustomBed> beds;
//    // private final List<Flag> flags;
//    // private final List<JumpPad> jumpPads;
////    private final HashMap<ChestTier, List<String>> chests;
//    // private final GeneralSettings generalSettings;
//
//    public MapYaml(CustomMap map, Player player) {
//        this.map = map;
//        this.created = new Date();
//        this.updated = this.created;
//        this.authors = new ArrayList<>(Collections.singletonList(player.getUniqueId().toString()));
//        this.obsSpawn = YamlUtils.locationToString(map.getWorld().getSpawnLocation());
////        this.soloSpawns = new ArrayList<>();
////        this.blueSpawns = new ArrayList<>();
////        this.redSpawns = new ArrayList<>();
////        this.greenSpawns = new ArrayList<>();
////        this.yellowSpawns = new ArrayList<>();
////        this.aquaSpawns = new ArrayList<>();
////        this.pinkSpawns = new ArrayList<>();
////        this.whiteSpawns = new ArrayList<>();
////        this.graySpawns = new ArrayList<>();
//        // this.spawners = new ArrayList<>();
//        // this.hills = new ArrayList<>();
//        // this.beds = new ArrayList<>();
//        // this.flags = new ArrayList<>();
//        // this.jumpPads = new ArrayList<>();
//        // this.chests = this.loadChests(new HashMap<>());
//        // this.generalSettings = new GeneralSettings();
//        this.yaml = this.updateYaml();
//    }
//
//    public MapYaml(CustomMap map, Map<String, Object> yaml) {
//        this.map = map;
//        this.yaml = yaml;
//        this.created = (Date) yaml.getOrDefault("created", new Date());
//        this.updated = (Date) yaml.getOrDefault("updated", this.created);
//        this.authors = new ArrayList<>((List<String>) this.yaml.getOrDefault("authors", new ArrayList<>(Collections.singletonList(Commons.getOwnerId()))));
//        Map<String, Object> spawns = (Map<String, Object>) this.yaml.getOrDefault("spawns", new LinkedHashMap<>());
//        this.obsSpawn = (String) spawns.getOrDefault("obs", "0.5,50,0.5,0");
////        this.soloSpawns = new ArrayList<>((List<String>) spawns.getOrDefault("solo", new ArrayList<>()));
////        this.blueSpawns = new ArrayList<>((List<String>) spawns.getOrDefault("blue", new ArrayList<>()));
////        this.redSpawns = new ArrayList<>((List<String>) spawns.getOrDefault("red", new ArrayList<>()));
////        this.greenSpawns = new ArrayList<>((List<String>) spawns.getOrDefault("green", new ArrayList<>()));
////        this.yellowSpawns = new ArrayList<>((List<String>) spawns.getOrDefault("yellow", new ArrayList<>()));
////        this.aquaSpawns = new ArrayList<>((List<String>) spawns.getOrDefault("aqua", new ArrayList<>()));
////        this.pinkSpawns = new ArrayList<>((List<String>) spawns.getOrDefault("pink", new ArrayList<>()));
////        this.whiteSpawns = new ArrayList<>((List<String>) spawns.getOrDefault("white", new ArrayList<>()));
////        this.graySpawns = new ArrayList<>((List<String>) spawns.getOrDefault("gray", new ArrayList<>()));
////        this.spawners = new ArrayList<>();
////        ((List<Map<String, Object>>) this.yaml.getOrDefault("spawners", new ArrayList<>())).forEach(data -> this.spawners.add(new Spawner(data)));
////        this.hills = new ArrayList<>();
////        ((List<Map<String, Object>>) this.yaml.getOrDefault("hills", new ArrayList<>())).forEach(data -> this.hills.add(new Hill(data)));
////        this.beds = new ArrayList<>();
////        ((List<Map<String, Object>>) this.yaml.getOrDefault("beds", new ArrayList<>())).forEach(data -> this.beds.add(new CustomBed(data)));
////        this.flags = new ArrayList<>();
////        ((List<Map<String, Object>>) this.yaml.getOrDefault("flags", new ArrayList<>())).forEach(data -> this.flags.add(new Flag(data)));
////        this.jumpPads = new ArrayList<>();
////        ((List<Map<String, Object>>) this.yaml.getOrDefault("jump_pads", new ArrayList<>())).forEach(data -> this.jumpPads.add(new JumpPad(data)));
////        this.chests = this.loadChests((Map<String, Object>) this.yaml.getOrDefault("chests", new LinkedHashMap<>()));
////        this.generalSettings = new GeneralSettings((Map<String, Object>) this.yaml.getOrDefault("general", new HashMap<>()));
//    }
//
//    private HashMap<ChestTier, List<String>> loadChests(Map<String, Object> data) {
//        HashMap<ChestTier, List<String>> chests = new HashMap<>();
//
//        for (ChestTier tier : ChestTier.values()) {
//            chests.put(tier, new ArrayList<>((List<String>) data.getOrDefault(tier.name().toLowerCase(), new ArrayList<>())));
//        }
//
//        return chests;
//    }
//
//    public Date getCreated() {
//        return this.created;
//    }
//
//    public Date getUpdated() {
//        return this.updated;
//    }
//
//    public List<String> getAuthors() {
//        return this.authors;
//    }
//
//    public boolean hasAuthor(Player player) {
//        return this.hasAuthor(player.getUniqueId());
//    }
//
//    public boolean hasAuthor(UUID uuid) {
//        return this.authors.contains(uuid.toString());
//    }
//
//    public void addAuthor(UUID uuid) {
//        this.authors.add(uuid.toString());
//        this.map.setUnsavedChanges(true);
//    }
//
//    public void removeAuthor(UUID uuid) {
//        this.authors.remove(uuid.toString());
//        this.map.setUnsavedChanges(true);
//    }
//
//    public String getObsSpawn() {
//        return this.obsSpawn;
//    }
//
//    public void setObsSpawn(Location loc) {
//        this.obsSpawn = YamlUtils.locationToString(loc);
//        this.map.setUnsavedChanges(true);
//    }
//
////    public List<String> getChests(ChestTier tier) {
////        return this.chests.get(tier);
////    }
////
////    public void addChest(ChestTier tier, Location location) {
////        String loc = YamlUtils.locationToCoords(location, false);
////        this.chests.get(tier).add(loc);
////        this.map.setUnsavedChanges(true);
////    }
////
////    public void removeChest(ChestTier tier, int index) {
////        this.chests.get(tier).remove(index);
////        this.map.setUnsavedChanges(true);
////    }
//
//    public void cleanChests(World world) {
//        HashMap<ChestTier, List<String>> toRemove = new HashMap<>();
//
//        for (ChestTier tier : ChestTier.values()) {
//            List<String> chests = this.chests.get(tier);
//            toRemove.put(tier, new ArrayList<>());
//
//            for (String coords : chests) {
//                Block block = world.getBlockAt(YamlUtils.getLocationFromCoords(world, coords));
//
//                if (block == null || block.getType() != Material.CHEST) {
//                    toRemove.get(tier).add(coords);
//                }
//            }
//        }
//
//        for (Map.Entry<ChestTier, List<String>> entry : toRemove.entrySet()) {
//            for (String coords : entry.getValue()) {
//                this.chests.get(entry.getKey()).remove(coords);
//            }
//        }
//    }
//
////    public List<String> getSpawns(MatchTeam team) {
////        switch (team) {
////            case SOLO:
////                return this.soloSpawns;
////            case BLUE:
////                return this.blueSpawns;
////            case RED:
////                return this.redSpawns;
////            case GREEN:
////                return this.greenSpawns;
////            case YELLOW:
////                return this.yellowSpawns;
////            case AQUA:
////                return this.aquaSpawns;
////            case PINK:
////                return this.pinkSpawns;
////            case WHITE:
////                return this.whiteSpawns;
////            case GRAY:
////                return this.graySpawns;
////        }
////
////        return new ArrayList<>();
////    }
////
////    public void addSpawn(MatchTeam team, Location location) {
////        String loc = YamlUtils.locationToString(location);
////
////        switch (team) {
////            case SOLO:
////                this.soloSpawns.add(loc);
////                break;
////            case BLUE:
////                this.blueSpawns.add(loc);
////                break;
////            case RED:
////                this.redSpawns.add(loc);
////                break;
////            case GREEN:
////                this.greenSpawns.add(loc);
////                break;
////            case YELLOW:
////                this.yellowSpawns.add(loc);
////                break;
////            case AQUA:
////                this.aquaSpawns.add(loc);
////                break;
////            case PINK:
////                this.pinkSpawns.add(loc);
////                break;
////            case WHITE:
////                this.whiteSpawns.add(loc);
////                break;
////            case GRAY:
////                this.graySpawns.add(loc);
////                break;
////        }
////
////        this.map.setUnsavedChanges(true);
////    }
////
////    public void removeSpawn(MatchTeam team, int index) {
////        switch (team) {
////            case SOLO:
////                this.soloSpawns.remove(index);
////                break;
////            case BLUE:
////                this.blueSpawns.remove(index);
////                break;
////            case RED:
////                this.redSpawns.remove(index);
////                break;
////            case GREEN:
////                this.greenSpawns.remove(index);
////                break;
////            case YELLOW:
////                this.yellowSpawns.remove(index);
////                break;
////            case AQUA:
////                this.aquaSpawns.remove(index);
////                break;
////            case PINK:
////                this.pinkSpawns.remove(index);
////                break;
////            case WHITE:
////                this.whiteSpawns.remove(index);
////                break;
////            case GRAY:
////                this.graySpawns.remove(index);
////                break;
////        }
////
////        this.map.setUnsavedChanges(true);
////    }
//
////    public List<Spawner> getSpawners() {
////        return this.spawners;
////    }
////
////    public void addSpawner(Spawner spawner) {
////        this.spawners.add(spawner);
////        this.map.setUnsavedChanges(true);
////    }
////
////    public void removeSpawner(int index) {
////        this.spawners.remove(index);
////        this.map.setUnsavedChanges(true);
////    }
////
////    public List<Hill> getHills() {
////        return this.hills;
////    }
////
////    public void addHill(Hill region) {
////        this.hills.add(region);
////        this.map.setUnsavedChanges(true);
////    }
////
////    public void removeHill(int index) {
////        this.hills.remove(index);
////        this.map.setUnsavedChanges(true);
////    }
////
////    public List<CustomBed> getBeds() {
////        return this.beds;
////    }
////
////    public void addBed(CustomBed bed) {
////        this.beds.add(bed);
////        this.map.setUnsavedChanges(true);
////    }
////
////    public void removeBed(int index) {
////        this.beds.remove(index);
////        this.map.setUnsavedChanges(true);
////    }
////
////    public List<Flag> getFlags() {
////        return this.flags;
////    }
////
////    public void addFlag(Flag flag) {
////        this.flags.add(flag);
////        this.map.setUnsavedChanges(true);
////    }
////
////    public void removeFlag(int index) {
////        this.flags.remove(index);
////        this.map.setUnsavedChanges(true);
////    }
////
////    public List<JumpPad> getJumpPads() {
////        return this.jumpPads;
////    }
////
////    public void addJumpPad(JumpPad jumpPad) {
////        this.jumpPads.add(jumpPad);
////        this.map.setUnsavedChanges(true);
////    }
////
////    public void removeJumpPad(int index) {
////        this.jumpPads.remove(index);
////        this.map.setUnsavedChanges(true);
////    }
////
////    public GeneralSettings getGeneralSettings() {
////        return this.generalSettings;
////    }
//
//    public Map<String, Object> updateYaml() {
//        this.updated = new Date();
//
//        Map<String, Object> data = new LinkedHashMap<>();
//        data.put("created", this.created);
//        data.put("updated", this.updated);
//        data.put("authors", this.authors);
//
//        if (!this.generalSettings.toYaml().isEmpty()) data.put("general", this.generalSettings.toYaml());
//
//        Map<String, Object> spawns = new LinkedHashMap<>();
//        spawns.put("obs", this.obsSpawn);
//        if (this.soloSpawns.size() > 0) spawns.put("solo", this.soloSpawns);
//        if (this.blueSpawns.size() > 0) spawns.put("blue", this.blueSpawns);
//        if (this.redSpawns.size() > 0) spawns.put("red", this.redSpawns);
//        if (this.greenSpawns.size() > 0) spawns.put("green", this.greenSpawns);
//        if (this.yellowSpawns.size() > 0) spawns.put("yellow", this.yellowSpawns);
//        if (this.aquaSpawns.size() > 0) spawns.put("aqua", this.aquaSpawns);
//        if (this.pinkSpawns.size() > 0) spawns.put("pink", this.pinkSpawns);
//        if (this.whiteSpawns.size() > 0) spawns.put("white", this.whiteSpawns);
//        if (this.graySpawns.size() > 0) spawns.put("gray", this.graySpawns);
//
//        data.put("spawns", spawns);
//
//        if (this.spawners.size() > 0) {
//            List<Map<String, Object>> spawners = new ArrayList<>();
//            this.spawners.forEach(spawner -> spawners.add(spawner.toYaml()));
//            data.put("spawners", spawners);
//        }
//
//        if (this.hills.size() > 0) {
//            List<Map<String, Object>> hills = new ArrayList<>();
//            this.hills.forEach(hill -> hills.add(hill.toYaml()));
//            data.put("hills", hills);
//        }
//
//        if (this.flags.size() > 0) {
//            List<Map<String, Object>> flags = new ArrayList<>();
//            this.flags.forEach(flag -> flags.add(flag.toYaml()));
//            data.put("flags", flags);
//        }
//
//        if (this.beds.size() > 0) {
//            List<Map<String, Object>> beds = new ArrayList<>();
//            this.beds.forEach(bed -> beds.add(bed.toYaml()));
//            data.put("beds", beds);
//        }
//
//        if (this.jumpPads.size() > 0) {
//            List<Map<String, Object>> jumpPads = new ArrayList<>();
//            this.jumpPads.forEach(jumpPad -> jumpPads.add(jumpPad.toYaml()));
//            data.put("jump_pads", jumpPads);
//        }
//
//        Map<String, Object> chests = new LinkedHashMap<>();
//
//        this.cleanChests(this.map.getWorld());
//
//        for (ChestTier tier : ChestTier.values()) {
//            if (!this.chests.get(tier).isEmpty()) {
//                chests.put(tier.name().toLowerCase(), this.chests.get(tier));
//            }
//        }
//
//        if (!chests.isEmpty()) data.put("chests", chests);
//
//        try {
//            Yaml yaml = new Yaml();
//            FileWriter writer = new FileWriter(Commons.getRoot() + this.map.getName() + "/map.yml");
//            yaml.dump(data, writer);
//            writer.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        return data;
//    }
//
//    private final Map<MapObjectType, List<MapObject>> mapObjects = new HashMap<>();
//
//    public List<MapObject> getMapElements(MapObjectType mapObjectType) {
//        return this.mapObjects.get(mapObjectType);
//    }
//
//    public void addMapElement(MapObjectType mapObjectType, MapObject mapObject) {
//        List<MapObject> mapObjects = this.mapObjects.getOrDefault(mapObjectType, new ArrayList<>());
//
//        if (mapObject instanceof NestedMapObject) {
//            if (mapObjects.isEmpty()) mapObjects.add(mapObject);
//        } else {
//            mapObjects.add(mapObject);
//        }
//
//        this.mapObjects.put(mapObjectType, mapObjects);
//        this.map.setUnsavedChanges(true);
//    }
//
//    public void removeMapElement(MapObjectType mapObjectType, int id) {
//        this.mapObjects.get(mapObjectType).remove(id);
//    }
//
//    private void loadMapObjects() {
//        for (MapObjectType objectType : MapObjectType.values()) {
//
//        }
//    }
//
//    public NestedMapObject getNestedMapElement(MapObjectType objectType) {
//        return (NestedMapObject) this.getMapElements(objectType).get(0);
//    }
//
//}
