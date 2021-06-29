package net.purelic.CGC.maps.elements;

import net.purelic.CGC.maps.MapElement;
import net.purelic.CGC.maps.constants.MapElementType;
import net.purelic.CGC.maps.previews.Preview;
import net.purelic.CGC.maps.previews.SpawnerPreview;
import net.purelic.CGC.maps.settings.MapEnumSetting;
import net.purelic.CGC.maps.settings.MapNumberSetting;
import net.purelic.CGC.maps.settings.MapSetting;
import net.purelic.CGC.maps.settings.MapToggleSetting;
import net.purelic.commons.utils.YamlUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Map;

public class Spawner extends MapElement {

    private static final String BASE_COMMAND = "spawner";
    private static final String MATERIAL_SETTING = "Material";
    private static final String DELAY_SETTING = "Delay";
    private static final String AMOUNT_SETTING = "Amount";
    private static final String INFINITE_SETTING = "Infinite";
    private static final String HOLOGRAM_SETTING = "Hologram";
    private static final String START_SPAWNED_SETTING = "Start Spawned";
    private static final String DESTRUCTIVE_SETTING = "Destructive";
    public static final MapSetting[] SETTINGS = new MapSetting[]{
        new MapEnumSetting<>(
            BASE_COMMAND,
            Material.class,
            MATERIAL_SETTING,
            "Item to spawn",
            Material.GOLDEN_APPLE, Material.ARROW, Material.IRON_INGOT,
            Material.GOLD_INGOT, Material.DIAMOND, Material.EMERALD
        ),
        new MapNumberSetting(
            BASE_COMMAND,
            DELAY_SETTING,
            "Time between item spawns",
            "s",
            1,
            180,
            5,
            false,
            20
        ),
        new MapNumberSetting(
            BASE_COMMAND,
            AMOUNT_SETTING,
            "Number of items to spawn",
            " item",
            1,
            64,
            1,
            true,
            1
        ),
        new MapToggleSetting(
            BASE_COMMAND,
            INFINITE_SETTING,
            "Continuously spawn items",
            false
        ),
        new MapToggleSetting(
            BASE_COMMAND,
            HOLOGRAM_SETTING,
            "Show timer hologram above spawner",
            true
        ),
        new MapToggleSetting(
            BASE_COMMAND,
            START_SPAWNED_SETTING,
            "Spawner to start with the item(s) spawned",
            false
        ),
        new MapToggleSetting(
            BASE_COMMAND,
            DESTRUCTIVE_SETTING,
            "Spawner to replace itself with a spawner block",
            true
        )
    };

    public Spawner(Map<String, Object> yaml) {
        super(MapElementType.SPAWNER, yaml, SETTINGS);
    }

    public Material getMaterial() {
        return this.getEnumSetting(MATERIAL_SETTING);
    }

    public int getAmount() {
        return this.getNumberSetting(AMOUNT_SETTING);
    }

    public int getDelay() {
        return this.getNumberSetting(DELAY_SETTING);
    }

    @Override
    public String getBookName() {
        String name = YamlUtils.formatEnumString(this.getMaterial().name());
        if (this.getAmount() > 1) name += " x" + this.getAmount();
        return name + " (" + this.getDelay() + "s)";
    }

    @Override
    public String getBookHover() {
        return "\n\n" + YamlUtils.formatEnumString(this.getMaterial().name()) +
            "\n" + this.getAmount() + " x " + this.getDelay() + "s" +
            "\n" + YamlUtils.formatCoords(this.getLocation(), true, false);
    }

    @Override
    public Preview getPreview(Player player) {
        return new SpawnerPreview(
            player,
            this.location,
            this.getMaterial(),
            this.getToggleSetting(HOLOGRAM_SETTING),
            this.getDelay(),
            this.getAmount(),
            this.getToggleSetting(DESTRUCTIVE_SETTING)
        );
    }

}
