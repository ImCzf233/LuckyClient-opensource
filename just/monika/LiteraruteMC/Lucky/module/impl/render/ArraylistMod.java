package just.monika.LiteraruteMC.Lucky.module.impl.render;

import just.monika.LiteraruteMC.Lucky.LuckyClient;
import just.monika.LiteraruteMC.Lucky.event.EventListener;
import just.monika.LiteraruteMC.Lucky.event.impl.render.Render2DEvent;
import just.monika.LiteraruteMC.Lucky.event.impl.render.ShaderEvent;
import just.monika.LiteraruteMC.Lucky.module.Category;
import just.monika.LiteraruteMC.Lucky.module.Module;
import just.monika.LiteraruteMC.Lucky.settings.ParentAttribute;
import just.monika.LiteraruteMC.Lucky.settings.impl.BooleanSetting;
import just.monika.LiteraruteMC.Lucky.settings.impl.ModeSetting;
import just.monika.LiteraruteMC.Lucky.settings.impl.NumberSetting;
import just.monika.LiteraruteMC.Lucky.utils.animations.Animation;
import just.monika.LiteraruteMC.Lucky.utils.animations.Direction;
import just.monika.LiteraruteMC.Lucky.utils.font.FontUtil;
import just.monika.LiteraruteMC.Lucky.utils.font.MinecraftFontRenderer;
import just.monika.LiteraruteMC.Lucky.utils.misc.StringUtils;
import just.monika.LiteraruteMC.Lucky.utils.objects.Dragging;
import just.monika.LiteraruteMC.Lucky.utils.render.ColorUtil;
import just.monika.LiteraruteMC.Lucky.utils.render.RenderUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;
import java.util.Comparator;
import java.util.List;

public class ArraylistMod extends Module {

    private final BooleanSetting importantModules = new BooleanSetting("Important", true);
    public final NumberSetting height = new NumberSetting("Height", 11, 20, 9, .5f);
    private final ModeSetting animation = new ModeSetting("Animation", "Move in", "Move in", "Scale in");
    private final NumberSetting colorIndex = new NumberSetting("Color Seperation", 20, 100, 5, 1);
    private final NumberSetting colorSpeed = new NumberSetting("Color Speed", 15, 30, 2, 1);
    private final BooleanSetting background = new BooleanSetting("Background", false);
    private final NumberSetting backgroundAlpha = new NumberSetting("Background Alpha", .35, 1, .01, .01);

    private static final MinecraftFontRenderer font = FontUtil.tenacityFont20;
    public List<Module> modules;

    public ArraylistMod() {
        super("Arraylist", Category.RENDER, "Displays your active modules");
        this.addSettings(importantModules, height, animation, colorIndex, colorSpeed, background, backgroundAlpha);
        backgroundAlpha.addParent(background, ParentAttribute.BOOLEAN_CONDITION);
        if (!toggled) this.toggleSilent();
    }

    private final Comparator<Object> SORT_METHOD = Comparator.comparingDouble(m -> {
        Module module = (Module) m;
        String name = module.getName() + (module.hasMode() ? " " + module.getSuffix() : "");
        return font.getStringWidth(name);
    }).reversed();

    public void getModules() {
        if (modules == null) {
            List<Class<? extends Module>> hiddenModules = LuckyClient.INSTANCE.getModuleCollection().getHiddenModules();
            List<Module> moduleList = LuckyClient.INSTANCE.getModuleCollection().getModules();
            moduleList.removeIf(module -> hiddenModules.stream().anyMatch(moduleClass -> moduleClass == module.getClass()));
            modules = moduleList;
        }
    }

    public Dragging arraylistDrag = LuckyClient.INSTANCE.createDrag(this, "arraylist", 2, 3);

    public String longest = "";
    double longestWidth = 0;


    private final EventListener<ShaderEvent> blurEvent = e -> {
        if(modules == null) return;
        double yOffset = 0;
        ScaledResolution sr = new ScaledResolution(mc);
        for (Module module : modules) {
            if (importantModules.isEnabled() && module.getCategory() == Category.RENDER) continue;
            final Animation moduleAnimation = module.animation;
            if (!module.isToggled() && moduleAnimation.finished(Direction.BACKWARDS)) continue;

            String displayText = module.getName() + (module.hasMode() ? " §7" + module.getSuffix() : "");
            double textWidth = font.getStringWidth(displayText);

            double xValue = sr.getScaledWidth() - (arraylistDrag.getX());

            boolean flip = xValue <= sr.getScaledWidth() / 2f;
            double x = flip ? xValue : sr.getScaledWidth() - (textWidth + arraylistDrag.getX());

            double y = yOffset + arraylistDrag.getY();

            double heightVal = height.getValue() + 1;
            switch (animation.getMode()) {
                case "Move in":
                    if(flip){
                        x -= Math.abs((moduleAnimation.getOutput() - 1) * (sr.getScaledWidth() - (arraylistDrag.getX() + textWidth)));
                    }else {
                        x += Math.abs((moduleAnimation.getOutput() - 1) * (arraylistDrag.getX() + textWidth));
                    }
                    break;
                case "Scale in":
                    RenderUtil.scaleStart((float) (x + font.getStringWidth(displayText) / 2f), (float) (y + heightVal / 2 - font.getHeight() / 2f), (float) moduleAnimation.getOutput());
                    break;
            }

            if(background.isEnabled()) {
                Gui.drawRect2(x - 2, y - 3, font.getStringWidth(displayText) + 5, heightVal, Color.WHITE.getRGB());
            }
            if (animation.is("Scale in")) {
                RenderUtil.scaleEnd();
            }

            yOffset += moduleAnimation.getOutput() * heightVal;
        }
    };

    private final EventListener<Render2DEvent> onRender2D = e -> {
        getModules();
        modules.sort(SORT_METHOD);

        if(!StringUtils.getToggledModules(modules).isEmpty()) {
            Module firstMod = StringUtils.getToggledModules(modules).get(0);
            longest = firstMod.getName() + (firstMod.hasMode() ? " " + firstMod.getSuffix() : "");
            longestWidth = font.getStringWidth(longest);
        }
        double yOffset = 0;
        ScaledResolution sr = new ScaledResolution(mc);
        int count = 0;
        for (Module module : modules) {
            if (importantModules.isEnabled() && module.getCategory() == Category.RENDER) continue;
            final Animation moduleAnimation = module.animation;

            moduleAnimation.setDirection(module.isToggled() ? Direction.FORWARDS : Direction.BACKWARDS);

            if (!module.isToggled() && moduleAnimation.finished(Direction.BACKWARDS)) continue;

            String displayText = module.getName() + (module.hasMode() ? " §7" + module.getSuffix() : "");
            double textWidth = font.getStringWidth(displayText);

            double xValue = sr.getScaledWidth() - (arraylistDrag.getX());


            boolean flip = xValue <= sr.getScaledWidth() / 2f;
            double x = flip ? xValue : sr.getScaledWidth() - (textWidth + arraylistDrag.getX());


            float alphaAnimation = 1;

            double y = yOffset + arraylistDrag.getY();

            double heightVal = height.getValue() + 1;

            switch (animation.getMode()) {
                case "Move in":
                    if(flip){
                        x -= Math.abs((moduleAnimation.getOutput() - 1) * (sr.getScaledWidth() - (arraylistDrag.getX() - textWidth)));
                    }else {
                        x += Math.abs((moduleAnimation.getOutput() - 1) * (arraylistDrag.getX() + textWidth));
                    }
                    break;
                case "Scale in":
                    RenderUtil.scaleStart((float) (x + font.getStringWidth(displayText) / 2f), (float) (y + heightVal / 2 - font.getHeight() / 2f), (float) moduleAnimation.getOutput());
                    alphaAnimation = (float) moduleAnimation.getOutput();
                    break;
            }

            if (background.isEnabled()) {
                Gui.drawRect2(x - 2, y - 3, font.getStringWidth(displayText) + 5, heightVal,
                        ColorUtil.applyOpacity(new Color(10, 10, 10), backgroundAlpha.getValue().floatValue() * alphaAnimation).getRGB());
            }

            Color textcolor;
            int index = (int) (count * colorIndex.getValue());
            switch (HudMod.colorMode.getMode()) {//"LuckyClient", "Light Rainbow", "Rainbow", "Static", "Fade", "Double Color", "Analogous"
                case "LuckyClient":
                    textcolor = ColorUtil.interpolateColorsBackAndForth(15, count * 20, LuckyClient.INSTANCE.getClientColor(), LuckyClient.INSTANCE.getAlternateClientColor(), HudMod.hueInterpolation.isEnabled());
                    break;
                case "Light Rainbow":
                    textcolor = ColorUtil.rainbow(colorSpeed.getValue().intValue(), index, .6f, 1, 1);
                    break;
                case "Rainbow":
                    textcolor = ColorUtil.rainbow(colorSpeed.getValue().intValue(), index, 1f, 1, 1);
                    break;
                case "Static":
                    textcolor = HudMod.color.getColor();
                    break;
                case "Fade":
                    textcolor = ColorUtil.fade(colorSpeed.getValue().intValue(), index, HudMod.color.getColor(), 1);
                    break;
                case "Double Color":
                    textcolor = ColorUtil.interpolateColorsBackAndForth(colorSpeed.getValue().intValue(), index, HudMod.color.getColor(), HudMod.colorAlt.getColor(), HudMod.hueInterpolation.isEnabled());
                    break;
                case "Analogous":
                    int val = HudMod.degree.is("30") ? 0 : 1;
                    Color analogous = ColorUtil.getAnalogousColor(HudMod.color.getColor())[val];
                    textcolor = ColorUtil.interpolateColorsBackAndForth(colorSpeed.getValue().intValue(), index, HudMod.color.getColor(), analogous, HudMod.hueInterpolation.isEnabled());
                    break;
                default:
                    textcolor = new Color(-1);
                    break;
            }

            font.drawStringWithShadow(displayText, x, (y - 3) + font.getMiddleOfBox((float) heightVal), ColorUtil.applyOpacity(textcolor.getRGB(), alphaAnimation));

            if (animation.is("Scale in")) {
                RenderUtil.scaleEnd();
            }

            yOffset += moduleAnimation.getOutput() * heightVal;
            count++;
        }
    };

}
