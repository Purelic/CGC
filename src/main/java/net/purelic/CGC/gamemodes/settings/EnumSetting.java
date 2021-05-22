package net.purelic.CGC.gamemodes.settings;

import net.purelic.CGC.gamemodes.settings.constants.GameModeEnumSetting;

public class EnumSetting implements Setting {

    private final GameModeEnumSetting setting;
    private String value;

    public EnumSetting(String setting, Object value) {
        this.setting = GameModeEnumSetting.valueOf(setting.toUpperCase());
        this.value = (String) value;
    }

    @Override
    public boolean isDefault() {
        return this.value.equals(this.setting.getDefaultValue());
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
