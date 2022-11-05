package just.monika.LiteraruteMC.Lucky.ui.mainmenu;

import just.monika.LiteraruteMC.Lucky.LuckyClient;
import just.monika.LiteraruteMC.Lucky.utils.animations.Animation;
import just.monika.LiteraruteMC.Lucky.utils.animations.Direction;
import just.monika.LiteraruteMC.Lucky.utils.animations.impl.DecelerateAnimation;
import just.monika.LiteraruteMC.Lucky.utils.animations.impl.ElasticAnimation;
import just.monika.LiteraruteMC.Lucky.utils.font.FontUtil;
import just.monika.LiteraruteMC.Lucky.utils.misc.HoveringUtil;
import just.monika.LiteraruteMC.Lucky.utils.misc.NetworkingUtils;
import just.monika.LiteraruteMC.Lucky.utils.render.ColorUtil;
import just.monika.LiteraruteMC.Lucky.utils.render.GradientUtil;
import just.monika.LiteraruteMC.Lucky.utils.render.RenderUtil;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TenacityMainMenu extends GuiScreen {

    public Animation openAnimation;
    private Animation hoverBarAnimation;
    private Animation openSideAnim;
    private Animation fadeInAnim;

    private final List<MenuButton> buttons = new ArrayList() {{
        add(new MenuButton("LuckyClient/MainMenu/button1.png", "Singleplayer"));
        add(new MenuButton("LuckyClient/MainMenu/button2.png", "Multiplayer"));
    }};

    private final List<OptionButton> sidebarButtons = new ArrayList() {{
        add(new OptionButton(FontUtil.XMARK, "Exit"));
        add(new OptionButton(FontUtil.SETTINGS, "Settings"));
    }};

    private boolean clickedSidebar;

    @Override
    public void initGui() {
        mc.gameSettings.ofFastRender = false;
        clickedSidebar = false;
        mc.gameSettings.guiScale = 2;
        openAnimation = new ElasticAnimation(750, 1, 3.8f, 1.75f, false);
        hoverBarAnimation = new ElasticAnimation(700, 1, 3.8f, 2f, false);
        openSideAnim = new ElasticAnimation(700, 1, 3.8f, 2f, false);
        fadeInAnim = new DecelerateAnimation(550, 1);
        buttons.forEach(MenuButton::initGui);
        sidebarButtons.forEach(OptionButton::initGui);
    }

    private final float barsWidth = 131 / 2f;
    private final float barsHeight = 119 / 2f;
    private final float sideBarWidth = 216;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        ScaledResolution sr = new ScaledResolution(mc);
        Color gradientColor1 = ColorUtil.interpolateColorsBackAndForth(15, 1, LuckyClient.INSTANCE.getClientColor(), LuckyClient.INSTANCE.getAlternateClientColor(), false);
        Color gradientColor2 = ColorUtil.interpolateColorsBackAndForth(15, 1, LuckyClient.INSTANCE.getAlternateClientColor(), LuckyClient.INSTANCE.getClientColor(), false);
        // Gui.drawGradientRectSideways2(0, 0, this.width, this.height, gradientColor1.getRGB(), gradientColor2.getRGB());
        GradientUtil.drawGradient(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), 1, gradientColor1, gradientColor1, gradientColor2, gradientColor2);

        width = sr.getScaledWidth();
        height = sr.getScaledHeight();

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        RenderUtil.setAlphaLimit(0);


        float separation = 0;
        for (MenuButton button : buttons) {
            button.buttonWH = 179 / 2f;
            button.x = 50+width / 2f - button.buttonWH / 2f - 90.5f + separation;
            button.y = 180;

            button.clickAction = () -> {
                switch (button.text) {
                    case "Singleplayer":
                        mc.displayGuiScreen(new GuiSelectWorld(this));
                        break;
                    case "Multiplayer":
                        mc.displayGuiScreen(new GuiMultiplayer(this));
                        break;
                    case "Alt Manager":
                        mc.displayGuiScreen(LuckyClient.INSTANCE.getAltManager());
                        break;
                }
            };

            button.drawScreen(mouseX, mouseY);
            separation += 90.5f;
        }


        boolean hoveringBars = HoveringUtil.isHovering(11, 18, barsWidth - 33, barsHeight - 33, mouseX, mouseY);
        hoverBarAnimation.setDirection(hoveringBars && !clickedSidebar ? Direction.FORWARDS : Direction.BACKWARDS);
        mc.getTextureManager().bindTexture(new ResourceLocation("LuckyClient/MainMenu/triplebars.png"));
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, (-5 + 62 * hoverBarAnimation.getOutput()), 0);
        GlStateManager.rotate((float) (-90 * hoverBarAnimation.getOutput()), 0, 0, 1);
        GlStateManager.translate(0, -(-5 + (62 * hoverBarAnimation.getOutput())), 0);
        drawModalRectWithCustomSizedTexture(-5, (float) (((float) (-5 + (62 * hoverBarAnimation.getOutput())) - 45) + (45 * openAnimation.getOutput())), 0, 0, barsWidth, barsHeight, barsWidth, barsHeight);
        GlStateManager.popMatrix();


        openSideAnim.setDirection(clickedSidebar ? Direction.FORWARDS : Direction.BACKWARDS);
        fadeInAnim.setDirection(openSideAnim.getDirection());

        if (clickedSidebar || !openSideAnim.isDone()) {
            Gui.drawRect2(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), ColorUtil.applyOpacity(Color.BLACK, (float) Math.min(.4 * openSideAnim.getOutput(), .4)).getRGB());
        }
        GlStateManager.enableBlend();

        float sidebarWidth = (float) ((float) (15 * hoverBarAnimation.getOutput()) + (sideBarWidth * openSideAnim.getOutput()));
        int interpolateSideRect = ColorUtil.interpolateColorsBackAndForth(15, 1,
                new Color(192, 147, 203), new Color(98, 180, 206), false).getRGB();
        Gui.drawRect2(0, 0, sidebarWidth, sr.getScaledHeight(), interpolateSideRect);

        if (clickedSidebar || !openSideAnim.isDone()) {
            int seperation = 0;
            for (OptionButton sideButton : sidebarButtons) {
                sideButton.iconAdjustY = 3;
                switch (sideButton.name) {
                    case "Exit":
                        sideButton.color = Color.RED;
                        sideButton.clickAction = () -> mc.shutdown();
                        break;
                    case "Settings":
                        sideButton.color = new Color(80, 194, 255);
                        sideButton.clickAction = () -> mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
                        break;
                    case "QQ":
                        sideButton.color = new Color(88, 101, 242);
                        sideButton.iconAdjustY = 4;
                        sideButton.clickAction = () -> NetworkingUtils.openLink("https://jq.qq.com/?_wv=1027&k=FbFpZfDK");
                        break;
                    case "Scripting":
                        sideButton.color = new Color(255, 80, 126);
                        sideButton.clickAction = () -> NetworkingUtils.openLink("https://scripting.tenacity.dev");
                        break;
                }
                sideButton.x = (float) (-200 + (200 * openSideAnim.getOutput()));
                sideButton.y = 50 + seperation;
                sideButton.height = 50;
                sideButton.width = 170;
                sideButton.drawScreen(mouseX, mouseY);
                seperation += 50 + 25;
            }
        }


    }


    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        buttons.forEach(button -> button.mouseClicked(mouseX, mouseY, mouseButton));
        ScaledResolution sr = new ScaledResolution(mc);

        //If clicking anywhere else on the screen besides the sidebar, close the sidebar
        if (!HoveringUtil.isHovering(0, 0, 216, sr.getScaledHeight(), mouseX, mouseY) && mouseButton == 0 && clickedSidebar) {
            clickedSidebar = false;
        }
        //If hovering sidebar and clicking then open the sidebar
        boolean hoveringBars = HoveringUtil.isHovering(11, 18, barsWidth - 33, barsHeight - 33, mouseX, mouseY);
        if (hoveringBars && mouseButton == 0) {
            clickedSidebar = true;
        }

        if(clickedSidebar && mouseButton == 0){
            sidebarButtons.forEach(button -> button.mouseClicked(mouseX, mouseY, mouseButton));
        }


    }
}
