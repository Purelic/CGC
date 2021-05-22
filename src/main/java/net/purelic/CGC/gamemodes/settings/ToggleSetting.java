package net.purelic.CGC.gamemodes.settings;

import net.purelic.CGC.gamemodes.settings.constants.GameModeToggleSetting;

public class ToggleSetting implements Setting {

    private final GameModeToggleSetting setting;
    private boolean value;

    public ToggleSetting(String setting, Object value) {
        this.setting = GameModeToggleSetting.valueOf(setting.toUpperCase());
        this.value = (boolean) value;
    }

    @Override
    public boolean isDefault() {
        return this.value == (boolean) this.setting.getDefaultValue();
    }

    @Override
    public Boolean getValue() {
        return this.value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

}
