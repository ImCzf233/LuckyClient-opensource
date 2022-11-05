package just.monika.LiteraruteMC.Lucky.subclient.rose;

//import dev.client.rose.protection.RoseProtectedLaunch;
//import dev.client.rose.ui.GuiPanel;
//import dev.client.rose.ui.mainmenu.MainMenu;

import just.monika.LiteraruteMC.Lucky.subclient.rose.rose.utils.render.HoveringUtil;
import just.monika.LiteraruteMC.Lucky.utils.animations.Animation;
import just.monika.LiteraruteMC.Lucky.utils.animations.Direction;
import just.monika.LiteraruteMC.Lucky.utils.font.FontUtil;
import just.monika.LiteraruteMC.Lucky.utils.render.StencilUtil;
import just.monika.LiteraruteMC.Lucky.LuckyClient;
import just.monika.LiteraruteMC.Lucky.config.ConfigManager;
import just.monika.LiteraruteMC.Lucky.subclient.tenacity.TenacityBackgroundProcess;
import just.monika.LiteraruteMC.Lucky.ui.mainmenu.TenacityMainMenu;
import just.monika.LiteraruteMC.Lucky.utils.render.ColorUtil;
import just.monika.LiteraruteMC.Lucky.utils.render.RenderUtil;
import just.monika.LiteraruteMC.Lucky.utils.render.RoundedUtil;
import just.monika.LiteraruteMC.Lucky.utils.render.ShaderUtil;
import just.monika.LiteraruteMC.Lucky.utils.toolkit.孤儿戴子越的Util;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.Random;

public class EnterScreen extends GuiScreen {

    private Animation hoverTenaAnimation;
    private Animation hoverRoseAnimation;

    private final ShaderUtil roseShader = new ShaderUtil("Rose/Shaders/menu.frag");
    private long startTime = 0;


    @Override
    public void initGui() {
        mc.gameSettings.ofFastRender = false;
        runLucky();
        mc.displayGuiScreen(new TenacityMainMenu());
        return;
        /*
      
       // Display.setTitle("Client");
        hoverTenaAnimation = new DecelerateAnimation(250, 1);
        hoverRoseAnimation = new DecelerateAnimation(250, 1);
        hoverTenaAnimation.setDirection(Direction.BACKWARDS);
        hoverRoseAnimation.setDirection(Direction.BACKWARDS);

        startTime = System.currentTimeMillis();*/
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            initGui();
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        Gui.drawRect2(0, 0, width, height, new Color(30, 30, 30).getRGB());
        FontUtil.tenacityFont40.drawCenteredString("What client would you like to play?", width / 2f, 50, -1);

        // GradientUtil.drawGradient(20,20,100,100, 1f, new Color(20,20,190), new Color(180,70,90), new Color(89,179, 69), new Color(20, 170, 190));

        ScaledResolution sr = new ScaledResolution(mc);

        float buttonWH = 200;
        float tenaX = (sr.getScaledWidth() / 2f - buttonWH / 2f) - 170;
        float tenaY = sr.getScaledHeight() / 2f - buttonWH / 2f;
        boolean hoveringTena = HoveringUtil.isHovering(tenaX, tenaY, buttonWH, buttonWH, mouseX, mouseY);

        hoverTenaAnimation.setDirection(hoveringTena ? Direction.FORWARDS : Direction.BACKWARDS);

        //Tena Button
        Color tena1 = ColorUtil.interpolateColorsBackAndForth(15, 1, LuckyClient.INSTANCE.getClientColor(), LuckyClient.INSTANCE.getAlternateClientColor(), false);
        Color tena2 = ColorUtil.interpolateColorsBackAndForth(15, 1, LuckyClient.INSTANCE.getAlternateClientColor(), LuckyClient.INSTANCE.getClientColor(), false);

        float tenaMovement = (float) (35 * hoverTenaAnimation.getOutput());
        FontUtil.tenacityFont40.drawCenteredString("Play LuckyClient?", tenaX + buttonWH / 2f, tenaY + buttonWH - FontUtil.tenacityFont40.getHeight(),
                ColorUtil.applyOpacity(-1, (float) hoverTenaAnimation.getOutput()));

        RoundedUtil.drawGradientCornerLR(tenaX, tenaY - tenaMovement, buttonWH, buttonWH, 30, tena1, tena2);

        FontUtil.tenacityBoldFont40.drawCenteredString(LuckyClient.NAME + " " + LuckyClient.VERSION, tenaX + buttonWH / 2f, tenaY + buttonWH - (40 + FontUtil.tenacityBoldFont40.getHeight()) - tenaMovement, -1);

        FontUtil.tenacityFont20.drawCenteredString("by cedo, tear, senoe, and fathum", tenaX + buttonWH / 2f, tenaY + buttonWH - (10 + FontUtil.tenacityBoldFont40.getHeight()) - tenaMovement, -1);


        GL11.glEnable(GL11.GL_BLEND);


        float roseX = (sr.getScaledWidth() / 2f - buttonWH / 2f) + 170;
        float roseY = sr.getScaledHeight() / 2f - buttonWH / 2f;

        boolean hoveringRose = HoveringUtil.isHovering(roseX, roseY, buttonWH, buttonWH, mouseX, mouseY);
        hoverRoseAnimation.setDirection(hoveringRose ? Direction.FORWARDS : Direction.BACKWARDS);

        FontUtil.tenacityFont40.drawCenteredString("Play Rose?", roseX + buttonWH / 2f, roseY + buttonWH - FontUtil.tenacityFont40.getHeight(), ColorUtil.applyOpacity(-1, (float) hoverRoseAnimation.getOutput()));

        float roseMovement = (float) (35 * hoverRoseAnimation.getOutput());
        RenderUtil.setAlphaLimit(0);
        StencilUtil.initStencilToWrite();
        RoundedUtil.drawRound(roseX, roseY - roseMovement, buttonWH, buttonWH, 30, LuckyClient.INSTANCE.getClientColor());
        StencilUtil.readStencilBuffer(1);
        roseShader.init();
        roseShader.setUniformf("resolution", width * 2, height * 2);
        roseShader.setUniformf("time", (System.currentTimeMillis() - startTime) / 1000f);
        ShaderUtil.drawQuads(roseX - 25, roseY - (25 + roseMovement), buttonWH + 50, buttonWH + 50);
        roseShader.unload();
        StencilUtil.uninitStencilBuffer();

        GL11.glEnable(GL11.GL_BLEND);
        mc.getTextureManager().bindTexture(new ResourceLocation("Rose/roseEnter.png"));
        float roseLogoWidth = 203 / 2f;
        float roseLogoHeight = 256 / 2f;
        drawModalRectWithCustomSizedTexture(roseX + buttonWH / 2f - roseLogoWidth / 2f, roseY + 10 - roseMovement, 0, 0, roseLogoWidth, roseLogoHeight, roseLogoWidth, roseLogoHeight);

     //   FontUtil.tenacityBoldFont40.drawCenteredString(Rose.NAME + " " + Rose.VERSION, roseX + buttonWH / 2f, roseY + buttonWH - (40 + FontUtil.tenacityBoldFont40.getHeight()) - roseMovement, -1);
        FontUtil.tenacityFont20.drawCenteredString("by trq, aether, g8lol, and cedo", roseX + buttonWH / 2f, roseY + buttonWH - (10 + FontUtil.tenacityBoldFont40.getHeight()) - roseMovement, -1);

    }


    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        ScaledResolution sr = new ScaledResolution(mc);
        float buttonWH = 200;
        float tenaX = (sr.getScaledWidth() / 2f - buttonWH / 2f) - 170;
        float tenaY = sr.getScaledHeight() / 2f - buttonWH / 2f;
        float roseMovement = (float) (35 * hoverRoseAnimation.getOutput());
        float roseX = (sr.getScaledWidth() / 2f - buttonWH / 2f) + 170;
        float roseY = sr.getScaledHeight() / 2f - buttonWH / 2f;

        boolean hoveringTena = HoveringUtil.isHovering(tenaX, tenaY, buttonWH, buttonWH, mouseX, mouseY);
        boolean hoveringRose = HoveringUtil.isHovering(roseX, roseY, buttonWH, buttonWH, mouseX, mouseY);

        if (mouseButton == 0) {
            if (hoveringTena) {
                runLucky();
                mc.displayGuiScreen(new TenacityMainMenu());
            } else if (hoveringRose) {
//                RoseProtectedLaunch.launch();
//                mc.displayGuiScreen(new MainMenu());
                Client.client = ClientType.DOKI;
            }

        }


    }


    private void runLucky() {
        Display.setTitle("LuckyClient | "+ 孤儿戴子越的Util.TITLE[new Random().nextInt(孤儿戴子越的Util.TITLE.length-1)]);
        LuckyClient.INSTANCE.getEventProtocol().register(new TenacityBackgroundProcess());

        Thread configDaemon = new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ConfigManager.saveConfig();
            }
        });
        configDaemon.setDaemon(true);
        configDaemon.start();
        Client.client = ClientType.LUCKY;
    }


}
