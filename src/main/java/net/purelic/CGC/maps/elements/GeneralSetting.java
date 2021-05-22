package net.purelic.CGC.maps.elements;

import net.purelic.CGC.maps.constants.MapElementType;
import net.purelic.CGC.maps.GenericMapElement;
import net.purelic.CGC.maps.settings.generic.GenericMapNumberSetting;
import net.purelic.CGC.maps.settings.generic.GenericMapToggleSetting;
import net.purelic.CGC.maps.settings.MapSetting;

import java.util.Map;

public class GeneralSetting extends GenericMapElement {

    private static final String BASE_COMMAND = "setting";
    private static final String MIN_BUILD_LIMIT_SETTING = "Min. Build Limit";
    private static final String MAX_BUILD_LIMIT_SETTING = "Max Build Limit";
    private static final String TICK_SPEED = "Tick Speed";
    private static final String BLOCK_PROTECTION_SETTING = "Block Protection";
    private static final String BLOCK_PLACEMENT_SETTING = "Block Placement";
    private static final String NIGHT_VISION_SETTING = "Night Vision";
    public static final MapSetting[] SETTINGS = new MapSetting[]{
        new GenericMapNumberSetting(
            BASE_COMMAND,
            MIN_BUILD_LIMIT_SETTING,
            "Minimum y-level for placing blocks",
            "",
            0,
            250,
            25,
            false,
            0
        ),
        new GenericMapNumberSetting(
            BASE_COMMAND,
            MAX_BUILD_LIMIT_SETTING,
            "Maximum y-level for placing blocks",
            "",
            0,
            250,
            25,
            false,
            100
        ),
        new GenericMapNumberSetting(
            BASE_COMMAND,
            TICK_SPEED,
            "Random tick speed (vanilla = 3)",
            "",
            0,
            100,
            1,
            false,
            0
        ),
        new GenericMapToggleSetting(
            BASE_COMMAND,
            BLOCK_PROTECTION_SETTING,
            "If players can break non-player placed blocks",
            true
        ),
        new GenericMapToggleSetting(
            BASE_COMMAND,
            BLOCK_PLACEMENT_SETTING,
            "If players can place blocks",
            false
        ),
        new GenericMapToggleSetting(
            BASE_COMMAND,
            NIGHT_VISION_SETTING,
            "If the player should spawn with night vision",
            false
        )
    };

    public GeneralSetting(Map<String, Object> yaml) {
        super(MapElementType.SETTING, yaml, SETTINGS);
    }

}
