package just.monika.LiteraruteMC.Lucky.module.impl.render;

import just.monika.LiteraruteMC.Lucky.module.Category;
import just.monika.LiteraruteMC.Lucky.module.Module;
import just.monika.LiteraruteMC.Lucky.settings.impl.BooleanSetting;
import just.monika.LiteraruteMC.Lucky.settings.impl.ColorSetting;
import just.monika.LiteraruteMC.Lucky.settings.impl.ModeSetting;
import just.monika.LiteraruteMC.Lucky.settings.impl.NumberSetting;
import just.monika.LiteraruteMC.Lucky.ui.clickguis.dropdown.DropdownClickGui;
import just.monika.LiteraruteMC.Lucky.utils.render.RoundedUtil;
import just.monika.LiteraruteMC.Lucky.utils.render.ShaderUtil;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class ClickGuiMod extends Module {

    public static final ModeSetting colorMode = new ModeSetting("Color Mode", "Sync", "Sync", "Double Color", "Static", "Dynamic", "Dynamic Sync");
    public static final ColorSetting color = new ColorSetting("Color", new Color(41, 200, 224));
    public static final ColorSetting color2 = new ColorSetting("Color", new Color(136, 41, 224));
    public static final BooleanSetting accentedSettings = new BooleanSetting("Background Accent", true);
    public static final ModeSetting settingAccent = new ModeSetting("Setting Accent", "White", "White", "Color");
    public static final BooleanSetting walk = new BooleanSetting("Allow Movement", true);
    public static final ModeSetting scrollMode = new ModeSetting("Scroll Mode", "Screen Height", "Screen Height", "Value");
    public static final NumberSetting clickHeight = new NumberSetting("Tab Height", 250, 500, 100, 1);
    public static GuiScreen dropdownClickGui = new DropdownClickGui();

    public ClickGuiMod() {
        super("ClickGui", Category.RENDER, "Displays modules");
        clickHeight.addParent(scrollMode, selection -> selection.is("Value"));
        color.addParent(colorMode, selection -> !selection.is("Sync") || !selection.is("Dynamic Sync"));
        color2.addParent(colorMode, selection -> selection.is("Double Color") || selection.is("Dynamic"));
        this.addSettings(colorMode, color, color2, accentedSettings, settingAccent, walk, scrollMode, clickHeight);
        this.setKey(Keyboard.KEY_RSHIFT);
    }

    public void toggle() {
        this.onEnable();
    }

    public void onEnable() {
        mc.displayGuiScreen(dropdownClickGui);
        RoundedUtil.roundedShader = new ShaderUtil("roundedRect");
    }

}
