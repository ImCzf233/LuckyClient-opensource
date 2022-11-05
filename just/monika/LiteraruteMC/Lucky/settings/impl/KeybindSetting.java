package just.monika.LiteraruteMC.Lucky.settings.impl;

import just.monika.LiteraruteMC.Lucky.settings.Setting;
import org.lwjgl.input.Keyboard;

public class KeybindSetting extends Setting {

    private int code;

    public KeybindSetting(int code) {
        this.name = "Keybind";
        this.code = code;
    }

    public int getCode() {
        return code == -1 ? Keyboard.KEY_NONE : code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public Integer getConfigValue() {
        return this.getCode();
    }

}
