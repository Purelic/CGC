package net.purelic.CGC.maps.elements;

import net.purelic.CGC.maps.constants.MapElementType;
import net.purelic.CGC.maps.MapElement;
import net.purelic.CGC.maps.previews.JumpPadPreview;
import net.purelic.CGC.maps.previews.Preview;
import net.purelic.CGC.maps.settings.MapNumberSetting;
import net.purelic.CGC.maps.settings.MapSetting;
import net.purelic.CGC.maps.settings.MapToggleSetting;
import org.bukkit.entity.Player;

import java.util.Map;

public class JumpPad extends MapElement {

    private static final String BASE_COMMAND = "jumppad";
    private static final String POWER_SETTING = "Power";
    private static final String ANGLE_SETTING = "Angle";
    private static final String DESTRUCTIVE_SETTING = "Destructive";
    public static final MapSetting[] SETTINGS = new MapSetting[]{
        new MapNumberSetting(
            BASE_COMMAND,
            POWER_SETTING,
            "Power of the jump pad",
            "",
            3,
            10,
            1,
            false,
            5
        ),
        new MapNumberSetting(
            BASE_COMMAND,
            ANGLE_SETTING,
            "Angle of the jump pad",
            "°",
            15,
            90,
            15,
            false,
            45
        ),
        new MapToggleSetting(
            BASE_COMMAND,
            DESTRUCTIVE_SETTING,
            "Jump pad to replace itself with a slime block",
            true
        )
    };

    public JumpPad(Map<String, Object> yaml) {
        super(MapElementType.JUMP_PAD, yaml, SETTINGS);
    }

    public int getPower() {
        return this.getNumberSetting(POWER_SETTING);
    }

    public int getAngle() {
        return this.getNumberSetting(ANGLE_SETTING);
    }

    @Override
    public String getBookHover() {
        return "\n\nPower " + this.getPower() +
            "\n" + this.getAngle() + "° Angle";
    }

    @Override
    public Preview getPreview(Player player) {
        return new JumpPadPreview(
            player,
            this.location,
            this.getNumberSetting(ANGLE_SETTING),
            this.getNumberSetting(POWER_SETTING),
            this.getToggleSetting(DESTRUCTIVE_SETTING)
        );
    }

}
