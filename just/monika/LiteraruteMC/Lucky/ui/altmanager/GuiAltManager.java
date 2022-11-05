package just.monika.LiteraruteMC.Lucky.ui.altmanager;

import just.monika.LiteraruteMC.Lucky.LuckyClient;
import just.monika.LiteraruteMC.Lucky.ui.altmanager.helpers.Alt;
import just.monika.LiteraruteMC.Lucky.ui.altmanager.helpers.AltManagerUtils;
import just.monika.LiteraruteMC.Lucky.ui.altmanager.panels.AltListAltPanel;
import just.monika.LiteraruteMC.Lucky.ui.mainmenu.TenacityMainMenu;
import just.monika.LiteraruteMC.Lucky.ui.notifications.NotificationManager;
import just.monika.LiteraruteMC.Lucky.ui.notifications.NotificationType;
import just.monika.LiteraruteMC.Lucky.utils.animations.Animation;
import just.monika.LiteraruteMC.Lucky.utils.animations.impl.DecelerateAnimation;
import just.monika.LiteraruteMC.Lucky.utils.font.FontUtil;
import just.monika.LiteraruteMC.Lucky.module.impl.render.HudMod;
import just.monika.LiteraruteMC.Lucky.utils.render.ColorUtil;
import just.monika.LiteraruteMC.Lucky.utils.render.GradientUtil;
import just.monika.LiteraruteMC.Lucky.utils.render.RenderUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import org.lwjgl.input.Keyboard;

import java.awt.*;

public class GuiAltManager extends GuiScreen {

    private final AltManagerUtils utils = new AltManagerUtils();
    private final AltPanels panels = LuckyClient.INSTANCE.altPanels;
    private Animation initAnimation;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        utils.writeAltsToFile();

        ScaledResolution sr = new ScaledResolution(mc);

        Color gradient1 = ColorUtil.interpolateColorsBackAndForth(
                15, 1, LuckyClient.INSTANCE.getClientColor(), LuckyClient.INSTANCE.getAlternateClientColor(), false);
        Color gradient2 = ColorUtil.interpolateColorsBackAndForth(
                15, 1, LuckyClient.INSTANCE.getAlternateClientColor(), LuckyClient.INSTANCE.getClientColor(), false);

        GradientUtil.drawGradient(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), 1, gradient1, gradient1, gradient2, gradient2);

        Color[] clientColors = ((HudMod) LuckyClient.INSTANCE.getModuleCollection().get(HudMod.class)).getClientColors();
        Color grad1 = clientColors[0], grad2 = clientColors[1];
        int altCount = AltManagerUtils.alts.size();
        double anim = initAnimation.getOutput();

        Gui.drawRect2(20 * anim, 10, sr.getScaledWidth() - 35, 25, new Color(20, 20, 29, 120).getRGB());

        GradientUtil.applyGradientHorizontal(23, 12, (float) FontUtil.tenacityBoldFont40.getStringWidth(LuckyClient.NAME.toLowerCase()), 20, 1, grad1, grad2, () -> {
            RenderUtil.setAlphaLimit(0);
            FontUtil.tenacityBoldFont40.drawString(LuckyClient.NAME.toLowerCase(), 23 * anim, 12, LuckyClient.INSTANCE.getClientColor().getRGB());
        });
        RenderUtil.resetColor();
        FontUtil.tenacityFont20.drawString(LuckyClient.VERSION, (FontUtil.tenacityBoldFont40.getStringWidth(LuckyClient.NAME.toLowerCase()) + 24) * anim, 12, grad2.getRGB());

        if (mc.getSession() != null && mc.getSession().getUsername() != null) {
            String name = mc.getSession().getUsername();
            AltListAltPanel altListPanel = (AltListAltPanel) panels.getPanel(AltListAltPanel.class);
            Alt fakeAlt = new Alt("_", "_");
            fakeAlt.username = name;
            altListPanel.drawAltHead(fakeAlt, (float) (sr.getScaledWidth() - FontUtil.tenacityFont24.getStringWidth(name) * anim - 42.5F), 12.5F, 20);
            FontUtil.tenacityFont24.drawStringWithShadow(name, sr.getScaledWidth() - FontUtil.tenacityFont24.getStringWidth(name) * anim - 20, 17, -1);
        }

        panels.getPanels().forEach(panel -> panel.drawScreen(mouseX, mouseY, partialTicks, initAnimation));

        LuckyClient.INSTANCE.getNotificationManager().drawNotifications(sr);
        switch (Alt.stage) {
            case 1:
                NotificationManager.post(NotificationType.INFO, "Alt Manager", "Invalid credentials!", 3);
                Alt.stage = 0;
                break;
            case 2:
                NotificationManager.post(NotificationType.SUCCESS, "Alt Manager", "Logged in successfully!", 3);
                Alt.stage = 0;
                break;
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        panels.getPanels().forEach(panel -> panel.mouseClicked(mouseX, mouseY, mouseButton, utils));
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == Keyboard.KEY_ESCAPE) {
            mc.displayGuiScreen(new TenacityMainMenu());
        } else {
            panels.getPanels().forEach(panel -> panel.keyTyped(typedChar, keyCode));
        }
    }

    @Override
    public void initGui() {
        initAnimation = new DecelerateAnimation(600, 1);
        panels.getPanels().forEach(AltPanel::initGui);
    }

}
