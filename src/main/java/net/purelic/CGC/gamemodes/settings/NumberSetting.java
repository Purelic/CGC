package net.purelic.CGC.gamemodes.settings;

import net.purelic.CGC.gamemodes.settings.constants.GameModeNumberSetting;

public class NumberSetting implements Setting {

    private final GameModeNumberSetting setting;
    private int value;

    public NumberSetting(String setting, Object value) {
        this.setting = GameModeNumberSetting.valueOf(setting.toUpperCase());
        this.value = value instanceof Long ? Integer.parseInt(String.valueOf((long) value)) : (int) value;
    }

    @Override
    public boolean isDefault() {
        return this.value == (int) this.setting.getDefaultValue();
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
