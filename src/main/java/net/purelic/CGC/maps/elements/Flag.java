package net.purelic.CGC.maps.elements;

import net.purelic.CGC.maps.constants.FlagDirection;
import net.purelic.CGC.maps.constants.MapElementType;
import net.purelic.CGC.maps.constants.MatchTeam;
import net.purelic.CGC.maps.NamedMapElement;
import net.purelic.CGC.maps.previews.FlagPreview;
import net.purelic.CGC.maps.previews.Preview;
import net.purelic.CGC.maps.settings.MapEnumSetting;
import net.purelic.CGC.maps.settings.MapSetting;
import net.purelic.commons.utils.YamlUtils;
import org.bukkit.entity.Player;

import java.util.Map;

public class Flag extends NamedMapElement {

    private static final String BASE_COMMAND = "flag";
    private static final String OWNER_SETTING = "Owner";
    private static final String DIRECTION_SETTING = "Direction";
    public static final MapSetting[] SETTINGS = new MapSetting[]{
        new MapEnumSetting<>(
            BASE_COMMAND,
            MatchTeam.class,
            OWNER_SETTING,
            "Flag can't be captured by this team unless neutral"
        ),
        new MapEnumSetting<>(
            BASE_COMMAND,
            FlagDirection.class,
            DIRECTION_SETTING,
            "Direction the flag will face"
        )
    };

    public Flag(Map<String, Object> yaml) {
        super(MapElementType.FLAG, yaml, SETTINGS);
    }

    public MatchTeam getOwner() {
        return this.getEnumSetting(OWNER_SETTING);
    }

    @Override
    public String getBookHover() {
        return "\n\n" + this.getName() +
            "\nOwned by " + this.getOwner().getChatColor() + YamlUtils.formatEnumString(this.getOwner().name()) +
            "\n" + YamlUtils.formatCoords(this.getLocation(), true, false);
    }

    @Override
    public Preview getPreview(Player player) {
        return new FlagPreview(
            player,
            this.location,
            this.name,
            this.getEnumSetting(DIRECTION_SETTING),
            this.getOwner()
        );
    }

}
