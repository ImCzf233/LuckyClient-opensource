package just.monika.LiteraruteMC.Lucky.ui.oldaltmanager;

import just.monika.LiteraruteMC.Lucky.LuckyClient;
import just.monika.LiteraruteMC.Lucky.ui.mainmenu.TenacityMainMenu;
import just.monika.LiteraruteMC.Lucky.ui.notifications.NotificationManager;
import just.monika.LiteraruteMC.Lucky.ui.notifications.NotificationType;
import just.monika.LiteraruteMC.Lucky.utils.animations.Animation;
import just.monika.LiteraruteMC.Lucky.utils.animations.impl.DecelerateAnimation;
import just.monika.LiteraruteMC.Lucky.utils.font.FontUtil;
import just.monika.LiteraruteMC.Lucky.utils.time.TimerUtil;
import just.monika.LiteraruteMC.Lucky.ui.oldaltmanager.backend.Alt;
import just.monika.LiteraruteMC.Lucky.ui.oldaltmanager.backend.AltManagerUtils;
import just.monika.LiteraruteMC.Lucky.utils.render.ColorUtil;
import just.monika.LiteraruteMC.Lucky.utils.render.GradientUtil;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

import java.awt.*;

public class AltManager extends GuiScreen {

    private final TimerUtil timerUtil = new TimerUtil();
    AltManagerUtils altManagerUtils = new AltManagerUtils();
    AltPanels altPanels = null;//LuckyClient.INSTANCE.altPanels;
    private Animation initAnimation;

    @Override
    public void initGui() {
        initAnimation = new DecelerateAnimation(600, 1);
        altPanels.getPanels().forEach(AltPanel::initGui);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if (keyCode == 1) {
            mc.displayGuiScreen(new TenacityMainMenu());
        }
        altPanels.getPanels().forEach(panel -> panel.keyTyped(typedChar, keyCode));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);
        altManagerUtils.writeAltsToFile(timerUtil);
        Color gradientColor1 = ColorUtil.interpolateColorsBackAndForth(15, 1, LuckyClient.INSTANCE.getClientColor(), LuckyClient.INSTANCE.getAlternateClientColor(), false);
        Color gradientColor2 = ColorUtil.interpolateColorsBackAndForth(15, 1, LuckyClient.INSTANCE.getAlternateClientColor(), LuckyClient.INSTANCE.getClientColor(), false);

        GradientUtil.drawGradient(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), 1, gradientColor1, gradientColor1, gradientColor2, gradientColor2);


        int altAmt = altManagerUtils.getAmountOfAlts();
        FontUtil.tenacityBoldFont32.drawStringWithShadow("LuckyClient Alt Manager @ " + altAmt + " Alt" + (altAmt != 1 ? "s" : ""),
                20, (10 - 20) + (20 * initAnimation.getOutput()), -1);

        if (mc.getSession() != null && mc.getSession().getUsername() != null) {
            FontUtil.tenacityFont24.drawStringWithShadow("Currently logged in as \247a" + mc.getSession().getUsername(), 20, sr.getScaledHeight() - 30, -1);
        }

        altPanels.getPanels().forEach(panel -> panel.drawScreen(mouseX, mouseY, partialTicks, initAnimation));

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
        altPanels.getPanels().forEach(panel -> panel.mouseClicked(mouseX, mouseY, mouseButton, altManagerUtils));
    }

}
