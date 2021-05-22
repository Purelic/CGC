package net.purelic.CGC.maps.elements;

import net.purelic.CGC.maps.constants.BedDefenseType;
import net.purelic.CGC.maps.constants.BedDirection;
import net.purelic.CGC.maps.constants.MapElementType;
import net.purelic.CGC.maps.constants.MatchTeam;
import net.purelic.CGC.maps.MapElement;
import net.purelic.CGC.maps.previews.BedPreview;
import net.purelic.CGC.maps.previews.Preview;
import net.purelic.CGC.maps.settings.MapEnumSetting;
import net.purelic.CGC.maps.settings.MapSetting;
import net.purelic.commons.utils.YamlUtils;
import org.bukkit.entity.Player;

import java.util.Map;

public class Bed extends MapElement {

    private static final String BASE_COMMAND = "bed";
    private static final String OWNER_SETTING = "Owner";
    private static final String DIRECTION_SETTING = "Direction";
    private static final String DEFENSE_SETTING = "Defense";
    public static final MapSetting[] SETTINGS = new MapSetting[]{
        new MapEnumSetting<>(
            BASE_COMMAND,
            MatchTeam.class,
            OWNER_SETTING,
            "Bed can't be broken by this team",
            MatchTeam.BLUE, MatchTeam.RED, MatchTeam.GREEN, MatchTeam.YELLOW,
            MatchTeam.AQUA, MatchTeam.PINK, MatchTeam.WHITE, MatchTeam.GRAY
        ),
        new MapEnumSetting<>(
            BASE_COMMAND,
            BedDirection.class,
            DIRECTION_SETTING,
            "Direction the bed will face"
        ),
        new MapEnumSetting<>(
            BASE_COMMAND,
            BedDefenseType.class,
            DEFENSE_SETTING,
            "Pre-built bed defense"
        )
    };

    public Bed(Map<String, Object> yaml) {
        super(MapElementType.BED, yaml, SETTINGS);
    }

    public MatchTeam getOwner() {
        return this.getEnumSetting(OWNER_SETTING);
    }

    public BedDefenseType getDefense() {
        return this.getEnumSetting(DEFENSE_SETTING);
    }

    @Override
    public String getBookHover() {
        return "\n\nOwned by " + this.getOwner().getChatColor() + YamlUtils.formatEnumString(this.getOwner().name()) +
            "\n" + (this.getDefense() == BedDefenseType.NONE ? "No" : YamlUtils.formatEnumString(this.getDefense().name())) + " Defense" +
            "\n" + YamlUtils.formatCoords(this.getLocation(), true, false);
    }

    @Override
    public Preview getPreview(Player player) {
        return new BedPreview(
            player,
            this.location,
            this.getDefense(),
            this.getEnumSetting(DIRECTION_SETTING),
            this.getOwner()
        );
    }

}
