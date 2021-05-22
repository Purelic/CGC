package net.purelic.CGC.maps.elements;

import net.purelic.CGC.maps.NamedMapElement;
import net.purelic.CGC.maps.constants.HillType;
import net.purelic.CGC.maps.constants.MapElementType;
import net.purelic.CGC.maps.constants.MatchTeam;
import net.purelic.CGC.maps.previews.HillPreview;
import net.purelic.CGC.maps.previews.Preview;
import net.purelic.CGC.maps.settings.MapEnumSetting;
import net.purelic.CGC.maps.settings.MapNumberSetting;
import net.purelic.CGC.maps.settings.MapSetting;
import net.purelic.CGC.maps.settings.MapToggleSetting;
import net.purelic.commons.utils.YamlUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Map;

public class Hill extends NamedMapElement {

    private static final String BASE_COMMAND = "hill";
    private static final String TYPE_SETTING = "Type";
    private static final String OWNER_SETTING = "Owner";
    private static final String MATERIAL_SETTING = "Material";
    private static final String RADIUS_SETTING = "Radius";
    private static final String CIRCLE_SETTING = "Circle";
    private static final String DESTRUCTIVE_SETTING = "Destructive";
    public static final MapSetting[] SETTINGS = new MapSetting[]{
        new MapEnumSetting<>(
            BASE_COMMAND,
            HillType.class,
            TYPE_SETTING,
            "Type of hill"
        ),
        new MapEnumSetting<>(
            BASE_COMMAND,
            MatchTeam.class,
            OWNER_SETTING,
            "Hill can't be captured by this team unless neutral"
        ),
        new MapEnumSetting<>(
            BASE_COMMAND,
            Material.class,
            MATERIAL_SETTING,
            "Material of the hill",
            Material.WOOL, Material.STAINED_CLAY, Material.STAINED_GLASS
        ),
        new MapNumberSetting(
            BASE_COMMAND,
            RADIUS_SETTING,
            "Radius of the hill",
            " block",
            0,
            8,
            1,
            true,
            3
        ),
        new MapToggleSetting(
            BASE_COMMAND,
            CIRCLE_SETTING,
            "If the hill is a circle or square",
            true
        ),
        new MapToggleSetting(
            BASE_COMMAND,
            DESTRUCTIVE_SETTING,
            "If the hill will destroy blocks that are not the same material",
            true
        )
    };

    public Hill(Map<String, Object> yaml) {
        super(MapElementType.HILL, yaml, SETTINGS);
    }

    public MatchTeam getOwner() {
        return this.getEnumSetting(OWNER_SETTING);
    }

    public boolean isCircle() {
        return this.getToggleSetting(CIRCLE_SETTING);
    }

    public int getRadius() {
        return this.getNumberSetting(RADIUS_SETTING);
    }

    public Material getMaterial() {
        return this.getEnumSetting(MATERIAL_SETTING);
    }

    public HillType getType() {
        return this.getEnumSetting(TYPE_SETTING);
    }

    @Override
    public String getBookHover() {
        return "\n\n" + this.getName() +
            "\n" + YamlUtils.formatEnumString(this.getType().name()) +
            "\nOwned by " + this.getOwner().getChatColor() + YamlUtils.formatEnumString(this.getOwner().name()) +
            "\n" + (this.isCircle() ? "Circle" : "Square") + " (" + this.getRadius() + " Radius)" +
            "\nMade of " + YamlUtils.formatEnumString(this.getMaterial().name()) +
            "\n" + YamlUtils.formatCoords(this.getLocation(), true, false);
    }

    @Override
    public Preview getPreview(Player player) {
        return new HillPreview(
            player,
            this.location,
            this.name,
            this.getOwner(),
            this.getRadius(),
            this.isCircle(),
            this.getMaterial(),
            this.getType(),
            this.getToggleSetting(DESTRUCTIVE_SETTING)
        );
    }

}
