package just.monika.LiteraruteMC.Lucky.module.impl.render;

import just.monika.LiteraruteMC.Lucky.LuckyClient;
import just.monika.LiteraruteMC.Lucky.event.EventListener;
import just.monika.LiteraruteMC.Lucky.event.impl.render.Render2DEvent;
import just.monika.LiteraruteMC.Lucky.module.Category;
import just.monika.LiteraruteMC.Lucky.module.Module;
import just.monika.LiteraruteMC.Lucky.settings.impl.BooleanSetting;
import just.monika.LiteraruteMC.Lucky.settings.impl.ColorSetting;
import just.monika.LiteraruteMC.Lucky.settings.impl.ModeSetting;
import just.monika.LiteraruteMC.Lucky.utils.font.FontUtil;
import just.monika.LiteraruteMC.Lucky.utils.font.MinecraftFontRenderer;
import just.monika.LiteraruteMC.Lucky.utils.objects.Dragging;
import just.monika.LiteraruteMC.Lucky.utils.render.ColorUtil;
import just.monika.LiteraruteMC.Lucky.utils.render.GradientUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.*;

import static net.minecraft.client.gui.Gui.drawRect;

public class HudMod extends Module {

    public static final ModeSetting colorMode = new ModeSetting("Hud Mode", "LuckyClient", "LuckyClient", "Light Rainbow", "Rainbow", "Static", "Fade", "Double Color", "Analogous");
    public static final ModeSetting degree = new ModeSetting("Degree", "30", "30", "-30");
    public static final ColorSetting color = new ColorSetting("Main Color", LuckyClient.INSTANCE.getClientColor());
    public static final ColorSetting colorAlt = new ColorSetting("Alt Color", LuckyClient.INSTANCE.getAlternateClientColor());
    public static final BooleanSetting movingColors = new BooleanSetting("Moving Colors", false);
    public static final BooleanSetting hueInterpolation = new BooleanSetting("Hue Interpolate", false);
    private final ModeSetting infoMode = new ModeSetting("Info Mode", "Semi Bold", "Semi Bold", "Bold", "Normal");
    private final BooleanSetting whiteInfo = new BooleanSetting("White Info", true);
    public final Dragging watermarkDrag = LuckyClient.INSTANCE.createDrag(this, "watermark", 5, 2);

    public HudMod() {
        super("Hud", Category.RENDER, "Settings that change how the client looks");
        movingColors.addParent(colorMode, m -> m.is("LuckyClient") || m.is("Fade") || m.is("Double Color") || m.is("Analogous"));
        color.addParent(colorMode, m -> m.is("Static") || m.is("Fade") || m.is("Double Color") || m.is("Analogous"));
        colorAlt.addParent(colorMode, m -> m.is("Double Color"));
        degree.addParent(colorMode, m -> m.is("Analogous"));
        this.addSettings(colorMode, degree, color, colorAlt, movingColors, hueInterpolation, infoMode, whiteInfo);
        if (!toggled) this.toggleSilent();
    }



    private final EventListener<Render2DEvent> onRender2D = e -> {
        String display=LuckyClient.NAME+" | "+Minecraft.getMinecraft().thePlayer.getName()+" | "+ new Date();
        watermarkDrag.setWidth(TabGUI.FontUtil.getWidth(display)+10);
        watermarkDrag.setHeight(TabGUI.FontUtil.getHeight()+7);
        drawRect(watermarkDrag.getX(), watermarkDrag.getY(), watermarkDrag.getX()+TabGUI.FontUtil.getWidth(display)+10, watermarkDrag.getY()+TabGUI.FontUtil.getHeight()+7, new Color(128,128,128, 150).getRGB());
        TabGUI.FontUtil.draw(
                display,
                (int) (watermarkDrag.getX()+2), (int) (watermarkDrag.getY()+3),
                new Color(255,255,255,255).getRGB()
        );
    };

    private final Map<String, String> bottomLeftText = new LinkedHashMap<>();

    private void drawInfo(Color[] clientColors) {
        ScaledResolution sr = new ScaledResolution(mc);
        bottomLeftText.put("XYZ:", Math.round(mc.thePlayer.posX) + " " + Math.round(mc.thePlayer.posY) + " " + Math.round(mc.thePlayer.posZ));
        bottomLeftText.put("FPS:", String.valueOf(Minecraft.getDebugFPS()));
        bottomLeftText.put("Speed:", String.valueOf(calculateBPS()));

        //InfoStuff
        MinecraftFontRenderer fr = (infoMode.is("Semi Bold") || infoMode.is("Bold")) ? FontUtil.tenacityBoldFont20 : FontUtil.tenacityFont20;

        float yOffset = (float) (14.5 * GuiChat.openingAnimation.getOutput());
        if(whiteInfo.isEnabled()){
            float boldFontMovement = fr.getHeight() + 2 + yOffset;
            for (String line : bottomLeftText.keySet()) {
                fr.drawString(line, 2, sr.getScaledHeight() - boldFontMovement, -1);
                boldFontMovement += fr.getHeight() + 2;
            }
        }else {
            float height = (fr.getHeight() + 2) * bottomLeftText.keySet().size();
            float width = (float) fr.getStringWidth("Speed:");
            GradientUtil.applyGradientVertical(2, sr.getScaledHeight() - (height + yOffset), width, height, 1, clientColors[0], clientColors[1], () -> {
                float boldFontMovement = fr.getHeight() + 2 + yOffset;
                for (String line : bottomLeftText.keySet()) {
                    fr.drawString(line, 2, sr.getScaledHeight() - boldFontMovement, -1);
                    boldFontMovement += fr.getHeight() + 2;
                }
            });
        }

        switch (infoMode.getMode()) {
            case "Bold":
                float boldFontMovement = FontUtil.tenacityBoldFont20.getHeight() + 2 + yOffset;
                for (Map.Entry<String, String> line : bottomLeftText.entrySet()) {

                    FontUtil.tenacityBoldFont20.drawString(line.getValue(), 2 + 2 + FontUtil.tenacityBoldFont20.getStringWidth(line.getKey()),
                            sr.getScaledHeight() - boldFontMovement, -1);

                    boldFontMovement += FontUtil.tenacityBoldFont20.getHeight() + 2;
                }
                break;
            case "Semi Bold":
                float semiBoldFontMovement = FontUtil.tenacityBoldFont20.getHeight() + 2 + yOffset;
                for (Map.Entry<String, String> line : bottomLeftText.entrySet()) {
                    FontUtil.tenacityFont20.drawString(line.getValue(), 2 + 2 + FontUtil.tenacityBoldFont20.getStringWidth(line.getKey()), sr.getScaledHeight() - semiBoldFontMovement, -1);
                    semiBoldFontMovement += FontUtil.tenacityBoldFont20.getHeight() + 2;
                }
                break;

            case "Normal":
                float fontMovement = FontUtil.tenacityFont20.getHeight() + 2 + yOffset;
                for (Map.Entry<String, String> line : bottomLeftText.entrySet()) {
                    FontUtil.tenacityFont20.drawString(line.getValue(), 2 + 2 + FontUtil.tenacityFont20.getStringWidth(line.getKey()), sr.getScaledHeight() - fontMovement, -1);
                    fontMovement += FontUtil.tenacityFont20.getHeight() + 2;
                }
                break;
        }
    }

    private double calculateBPS() {
        double bps = (Math.hypot(mc.thePlayer.posX - mc.thePlayer.prevPosX, mc.thePlayer.posZ - mc.thePlayer.prevPosZ) * mc.timer.timerSpeed) * 20;
        return Math.round(bps * 100.0) / 100.0;
    }

    private Color mixColors(Color color1, Color color2) {
        if (movingColors.isEnabled()) {
            return ColorUtil.interpolateColorsBackAndForth(15, 1, color1, color2, hueInterpolation.isEnabled());
        } else {
            return ColorUtil.interpolateColorC(color1, color2, 0);
        }
    }


    public Color[] getClientColors() {
        Color firstColor;
        Color secondColor;
        switch (colorMode.getMode()) {
            case "LuckyClient":
                firstColor = mixColors(LuckyClient.INSTANCE.getClientColor(), LuckyClient.INSTANCE.getAlternateClientColor());
                secondColor = mixColors(LuckyClient.INSTANCE.getAlternateClientColor(), LuckyClient.INSTANCE.getClientColor());
                break;
            case "Light Rainbow":
                firstColor = ColorUtil.rainbow(15, 1, .6f, 1, 1);
                secondColor = ColorUtil.rainbow(15, 40, .6f, 1, 1);
                break;
            case "Rainbow":
                firstColor = ColorUtil.rainbow(15, 1, 1, 1, 1);
                secondColor = ColorUtil.rainbow(15, 40, 1, 1, 1);
                break;
            case "Static":
                firstColor = color.getColor();
                secondColor = firstColor;
                break;
            case "Fade":
                firstColor = ColorUtil.fade(15, 1, color.getColor(), 1);
                secondColor = ColorUtil.fade(15, 50, color.getColor(), 1);
                break;
            case "Double Color":
                firstColor = mixColors(color.getColor(), colorAlt.getColor());
                secondColor = mixColors(colorAlt.getColor(), color.getColor());
                break;
            case "Analogous":
                int val = degree.is("30") ? 0 : 1;
                Color analogous = ColorUtil.getAnalogousColor(color.getColor())[val];
                firstColor = mixColors(color.getColor(), analogous);
                secondColor = mixColors(analogous, color.getColor());
                break;
            default:
                firstColor = new Color(-1);
                secondColor = new Color(-1);
                break;
        }
        return new Color[]{firstColor, secondColor};
    }

}
