package just.monika.LiteraruteMC.Lucky.ui.clickguis.dropdown.impl;


import just.monika.LiteraruteMC.Lucky.LuckyClient;
import just.monika.LiteraruteMC.Lucky.module.Module;
import just.monika.LiteraruteMC.Lucky.module.impl.render.ClickGuiMod;
import just.monika.LiteraruteMC.Lucky.module.impl.render.HudMod;
import just.monika.LiteraruteMC.Lucky.utils.animations.Animation;
import just.monika.LiteraruteMC.Lucky.utils.animations.Direction;
import just.monika.LiteraruteMC.Lucky.utils.animations.impl.DecelerateAnimation;
import just.monika.LiteraruteMC.Lucky.utils.animations.impl.EaseInOutQuad;
import just.monika.LiteraruteMC.Lucky.utils.font.FontUtil;
import just.monika.LiteraruteMC.Lucky.utils.misc.HoveringUtil;
import just.monika.LiteraruteMC.Lucky.utils.render.ColorUtil;
import just.monika.LiteraruteMC.Lucky.utils.render.GradientUtil;
import just.monika.LiteraruteMC.Lucky.utils.render.RenderUtil;
import net.minecraft.client.gui.Gui;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.Component;

public class ModuleRect extends Component {

    public final Module module;
    private final SettingComponents settingComponents;
    private final Animation animation = new EaseInOutQuad(300, 1, Direction.BACKWARDS);
    private final Animation arrowAnimation = new EaseInOutQuad(250, 1, Direction.BACKWARDS);
    private final Animation hoverAnimation = new DecelerateAnimation(250, 1, Direction.BACKWARDS);
    public Animation settingAnimation;
    public Animation openingAnimation;
    public float x, y, width, height, panelLimitY;
    public int alphaAnimation;
    int clickX, clickY;
    private double settingSize;

    public ModuleRect(Module module) {
        this.module = module;
        settingComponents = new SettingComponents(module);
    }

    
    public void initGui() {
        animation.setDirection(module.isToggled() ? Direction.FORWARDS : Direction.BACKWARDS);

    }

    
    public void keyTyped(char typedChar, int keyCode) {
        if (module.expanded) {
            settingComponents.keyTyped(typedChar, keyCode);
        }
    }

    
    public void drawScreen(int mouseX, int mouseY) {
        Color rectColor = new Color(43, 45, 50, alphaAnimation);
        Color textColor = new Color(255, 255, 255, alphaAnimation);

        Color clickModColor = ColorUtil.applyOpacity(ClickGuiMod.color.getColor(), alphaAnimation / 255f);
        Color clickModColor2 = ColorUtil.applyOpacity(ClickGuiMod.color2.getColor(), alphaAnimation / 255f);
        HudMod hudMod = (HudMod) LuckyClient.INSTANCE.getModuleCollection().get(HudMod.class);
        Color[] colors = hudMod.getClientColors();
        colors[0] = ColorUtil.applyOpacity(colors[0], alphaAnimation / 255f);
        colors[1] = ColorUtil.applyOpacity(colors[1], alphaAnimation / 255f);
        float alpha = alphaAnimation / 255f;

        boolean hoveringModule = HoveringUtil.isHovering(x, y, width, height, mouseX, mouseY);
        hoverAnimation.setDirection(hoveringModule ? Direction.FORWARDS : Direction.BACKWARDS);

        // Normal Grey rect
        Gui.drawRect2(x, y, width, height, ColorUtil.interpolateColor(rectColor, ColorUtil.brighter(rectColor, .8f), (float) hoverAnimation.getOutput()));
        switch (ClickGuiMod.colorMode.getMode()) {
            case "Sync":
                GradientUtil.drawGradientLR(x, y, width, height, (float) animation.getOutput() * alpha,
                        ColorUtil.interpolateColorC(colors[0], ColorUtil.darker(colors[0], .8f), (float) hoverAnimation.getOutput()),
                        ColorUtil.interpolateColorC(colors[1], ColorUtil.darker(colors[1], .8f), (float) hoverAnimation.getOutput()));
                break;
            case "Dynamic Sync":
                Color dynamicSync = ColorUtil.interpolateColorsBackAndForth(15, 1,
                        ColorUtil.interpolateColorC(colors[0], ColorUtil.darker(colors[0], .8f), (float) hoverAnimation.getOutput()),
                        ColorUtil.interpolateColorC(colors[1], ColorUtil.darker(colors[1], .8f), (float) hoverAnimation.getOutput()),
                        HudMod.hueInterpolation.isEnabled());

                Gui.drawRect2(x, y, width, height, ColorUtil.applyOpacity(dynamicSync, (float) animation.getOutput()).getRGB());
                break;
            case "Dynamic":
                Color dynamic = ColorUtil.interpolateColorsBackAndForth(15, 1, clickModColor, clickModColor2, HudMod.hueInterpolation.isEnabled());
                Gui.drawRect2(x, y, width, height, ColorUtil.applyOpacity(dynamic, (float) animation.getOutput()).getRGB());
                break;
            case "Static":
                Gui.drawRect2(x, y, width, height, ColorUtil.applyOpacity(clickModColor, (float) animation.getOutput()).getRGB());
                break;
            case "Double Color":
                GradientUtil.drawGradientLR(x, y, width, height, (float) animation.getOutput() * alpha, clickModColor, clickModColor2);
                break;
        }


        FontUtil.tenacityFont20.drawString(module.getName(), x + 5, y + FontUtil.tenacityFont20.getMiddleOfBox(height), textColor.getRGB());

        if (Keyboard.isKeyDown(Keyboard.KEY_TAB) && module.getKeybind().getCode() != 0) {
            String keyName = Keyboard.getKeyName(module.getKeybind().getCode());
            FontUtil.tenacityFont20.drawString(keyName, x + width - FontUtil.tenacityFont20.getStringWidth(keyName) - 5, y + FontUtil.tenacityFont20.getMiddleOfBox(height), textColor.getRGB());
        } else {
            float arrowSize = 6;
            arrowAnimation.setDirection(module.expanded ? Direction.FORWARDS : Direction.BACKWARDS);
            RenderUtil.setAlphaLimit(0);
            RenderUtil.resetColor();
            RenderUtil.drawClickGuiArrow(x + width - (arrowSize + 5), y + height / 2f - 2, arrowSize, arrowAnimation, textColor.getRGB());
        }

        Color settingRectColor = new Color(32, 32, 32, alphaAnimation);


        double settingHeight = (settingComponents.settingSize) * settingAnimation.getOutput();
        if (module.expanded || !settingAnimation.isDone()) {
            Gui.drawRect2(x, y + height, width, settingHeight * height, settingRectColor.getRGB());

            boolean hoveringSettingsOrModule = HoveringUtil.isHovering(x, y, width, (float) (height + (settingHeight * height)), mouseX, mouseY);


            if (ClickGuiMod.accentedSettings.isEnabled()) {
                RenderUtil.resetColor();
                float accentAlpha = (float) (.85 * animation.getOutput()) * alpha;
                switch (ClickGuiMod.colorMode.getMode()) {
                    case "Sync":
                        GradientUtil.drawGradientLR(x, y + height, width, (float) (settingHeight * height), accentAlpha, colors[0], colors[1]);
                        break;
                    case "Dynamic Sync":
                        Color dynamicSync = ColorUtil.interpolateColorsBackAndForth(15, 1, colors[0], colors[1], HudMod.hueInterpolation.isEnabled());
                        Gui.drawRect2(x, y + height, width, (float) (settingHeight * height), ColorUtil.applyOpacity(dynamicSync, accentAlpha).getRGB());
                        break;
                    case "Dynamic":
                        Color dynamic = ColorUtil.interpolateColorsBackAndForth(15, 1, clickModColor, clickModColor2, HudMod.hueInterpolation.isEnabled());
                        Gui.drawRect2(x, y + height, width, (float) (settingHeight * height), ColorUtil.applyOpacity(dynamic, accentAlpha).getRGB());
                        break;
                    case "Static":
                        Gui.drawRect2(x, y + height, width, (float) (settingHeight * height), ColorUtil.applyOpacity(clickModColor, accentAlpha).getRGB());
                        break;
                    case "Double Color":
                        GradientUtil.drawGradientLR(x, y + height, width, (float) (settingHeight * height), accentAlpha, clickModColor, clickModColor2);
                        break;
                }
            }


            settingComponents.x = x;
            settingComponents.y = y + height;
            settingComponents.width = width;
            settingComponents.rectHeight = height;
            settingComponents.panelLimitY = panelLimitY;
            settingComponents.alphaAnimation = alphaAnimation;
            settingComponents.settingHeightScissor = settingAnimation;
            if (!settingAnimation.isDone()) {
                GL11.glEnable(GL11.GL_SCISSOR_TEST);
                RenderUtil.scissor(x, y + height, width, settingHeight * height);

                settingComponents.drawScreen(mouseX, mouseY);
                Gui.drawGradientRect2(x, y + height, width, 6, new Color(0, 0, 0, 60).getRGB(), new Color(0, 0, 0, 0).getRGB());
                Gui.drawGradientRect2(x, y + 11 + (settingHeight * height), width, 6, new Color(0, 0, 0, 0).getRGB(), new Color(0, 0, 0, 60).getRGB());
                GL11.glDisable(GL11.GL_SCISSOR_TEST);
            } else {
                settingComponents.drawScreen(mouseX, mouseY);
                Gui.drawGradientRect2(x, y + height, width, 6, new Color(0, 0, 0, 60).getRGB(), new Color(0, 0, 0, 0).getRGB());
                Gui.drawGradientRect2(x, y + 11 + (settingHeight * height), width, 6, new Color(0, 0, 0, 0).getRGB(), new Color(0, 0, 0, 60).getRGB());
            }

        }
        settingSize = settingHeight;


    }

    
    public void mouseClicked(int mouseX, int mouseY, int button) {
        boolean hoveringModule = isClickable(y, panelLimitY) && HoveringUtil.isHovering(x, y, width, height, mouseX, mouseY);
        if (hoveringModule) {
            switch (button) {
                case 0:
                    clickX = mouseX;
                    clickY = mouseY;
                    animation.setDirection(!module.isToggled() ? Direction.FORWARDS : Direction.BACKWARDS);
                    module.toggleSilent();
                    break;
                case 1:
                    module.expanded = !module.expanded;
                    break;
            }
        }
        if (module.expanded) {
            settingComponents.mouseClicked(mouseX, mouseY, button);
        }
    }

    
    public void mouseReleased(int mouseX, int mouseY, int state) {
        if (module.expanded) {
            settingComponents.mouseReleased(mouseX, mouseY, state);
        }
    }

    public double getSettingSize() {
        return settingSize;
    }

    public boolean isClickable(float y, float panelLimitY) {
        return y > panelLimitY && y < panelLimitY + Module.allowedClickGuiHeight + 17;
    }
}
