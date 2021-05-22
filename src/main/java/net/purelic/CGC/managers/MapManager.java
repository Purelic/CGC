package net.purelic.CGC.managers;

import net.purelic.CGC.maps.CustomMap;
import net.purelic.commons.utils.MapUtils;
import net.purelic.commons.utils.NullChunkGenerator;
import org.bukkit.World;
import org.bukkit.WorldCreator;

import java.util.*;
import java.util.stream.Collectors;

public class MapManager {

    private static final Map<String, CustomMap> DRAFT_MAPS = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private static final Set<String> PUBLISHED_MAPS = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
    private static final Set<String> PENDING = new HashSet<>();

    public static void loadMaps(UUID uuid) {
        MapUtils.listDrafts(uuid).forEach(map -> addMap(map, new CustomMap(map)));
        MapUtils.listPublishedMaps(uuid).forEach(MapManager::addPublishedMap);
    }

    public static void addMap(String name, CustomMap map) {
        DRAFT_MAPS.put(name.toLowerCase(), map);
    }

    public static void addPublishedMap(String map) {
        PUBLISHED_MAPS.add(map);
    }

    public static void removeMap(String name) {
        DRAFT_MAPS.remove(name.toLowerCase());
        DRAFT_MAPS.keySet().remove(name.toLowerCase());
        PENDING.remove(name);
    }

    public static void removePublishedMap(String map) {
        PUBLISHED_MAPS.remove(map);
    }

    public static CustomMap getMap(String name) {
        return DRAFT_MAPS.get(name.toLowerCase());
    }

    public static String getPublishedMap(String name) {
        return PUBLISHED_MAPS.stream()
            .filter(map -> map.equalsIgnoreCase(name))
            .findFirst().orElse(null);
    }

    public static boolean isDownloaded(String name) {
        return getMap(name).hasWorld();
    }

    public static boolean hasMap(String name) {
        return DRAFT_MAPS.containsKey(name.toLowerCase());
    }

    public static boolean hasPublishedMap(String name) {
        return PUBLISHED_MAPS.stream().anyMatch(map -> map.equalsIgnoreCase(name));
    }

    public static void setPending(String name, boolean pending) {
        if (pending) PENDING.add(name);
        else PENDING.remove(name);
    }

    public static boolean isPending(String name) {
        return PENDING.contains(name);
    }

    public static Set<String> getMapNames() {
        Set<String> names = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        DRAFT_MAPS.forEach((key, value) -> names.add(value.getName()));
        return names;
    }

    public static Set<String> getPublishedMaps() {
        return PUBLISHED_MAPS;
    }

    public static List<String> getDownloadedMaps() {
        return getMapNames().stream()
            .filter(MapManager::isDownloaded)
            .collect(Collectors.toList());
    }

    public static void renameMap(String oldName, String newName) {
        CustomMap map = getMap(oldName);
        map.setName(newName);

        World world = (new WorldCreator(newName)).generator(new NullChunkGenerator()).createWorld();
        map.setWorld(world);

        removeMap(oldName);
        addMap(newName, map);
    }

}
