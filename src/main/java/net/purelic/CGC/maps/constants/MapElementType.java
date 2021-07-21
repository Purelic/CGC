package net.purelic.CGC.maps.constants;

import net.purelic.CGC.maps.GenericMapElement;
import net.purelic.CGC.maps.MapElement;
import net.purelic.CGC.maps.NestedMapElement;
import net.purelic.CGC.maps.elements.*;
import net.purelic.CGC.maps.settings.MapSetting;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum MapElementType {

    SETTING(GeneralSetting.class, GeneralSetting.SETTINGS, "general", "General Setting", 0),
    SPAWN(SpawnPoint.class, "spawns", "Spawn"),
    SPAWNER(Spawner.class, Spawner.SETTINGS, "spawners", "Spawner", -1),
    HILL(Hill.class, Hill.SETTINGS, "hills", "Hill", -1),
    FLAG(Flag.class, Flag.SETTINGS, "flags", "Flag", 0),
    BED(Bed.class, Bed.SETTINGS, "beds", "Bed", 0),
    JUMP_PAD(JumpPad.class, JumpPad.SETTINGS, "jump_pads", "Jump Pad", -1),
    CHEST(Chest.class, "chests", "Chest"),
    MONUMENT(Monument.class, Monument.SETTINGS, "monuments", "Monument", 0),
    REGION(Region.class, Region.SETTINGS, "regions", "Region", 0),
    ;

    private final Class<? extends MapElement> clazz;
    private final MapSetting[] settings;
    private final String yamlKey;
    private final String name;
    private final String baseCommand;
    private final String listCommand;
    private final int offsetY;

    MapElementType(Class<? extends MapElement> clazz, String yamlKey, String name) {
        this(clazz, new MapSetting[]{}, yamlKey, name, 0);
    }

    MapElementType(Class<? extends MapElement> clazz, MapSetting[] settings, String yamlKey, String name, int offsetY) {
        this.clazz = clazz;
        this.settings = settings;
        this.yamlKey = yamlKey;
        this.name = name;
        this.baseCommand = this.name().replaceAll("_", "").toLowerCase();
        this.listCommand = this.baseCommand + "s";
        this.offsetY = offsetY;
    }

    public MapElement create(Map<String, Object> yaml) {
        try {
            return this.clazz.getConstructor(Map.class).newInstance(yaml);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getYamlKey() {
        return this.yamlKey;
    }

    public String getName() {
        return this.name;
    }

    public String getLowerName() {
        return this.getName().toLowerCase();
    }

    public String getBaseCommand(boolean prefixed) {
        return (prefixed ? "/" : "") + this.baseCommand;
    }

    public String getListCommand() {
        return this.listCommand;
    }

    public boolean isNested() {
        return this.clazz.getSuperclass().equals(NestedMapElement.class)
            || this.clazz.getSuperclass().equals(GenericMapElement.class);
    }

    public int getOffsetY() {
        return this.offsetY;
    }

    @SuppressWarnings("all")
    public void registerCommands() {
        Arrays.asList(this.settings).forEach(setting -> setting.registerCommand(this));
        this.create(new HashMap<>()).registerCommands(this.baseCommand, this.settings);
    }

}
