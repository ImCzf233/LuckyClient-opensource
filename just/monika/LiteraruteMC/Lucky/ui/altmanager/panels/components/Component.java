package just.monika.LiteraruteMC.Lucky.ui.altmanager.panels.components;

import just.monika.LiteraruteMC.Lucky.ui.altmanager.helpers.AltManagerUtils;
import just.monika.LiteraruteMC.Lucky.utils.animations.Animation;
import just.monika.LiteraruteMC.Lucky.utils.render.ColorUtil;
import net.minecraft.client.Minecraft;

import java.awt.*;

public abstract class Component {
    public float x;
    public float y;
    public boolean hovering;
    protected int rectColorInt = new Color(20, 20, 29, 120).getRGB();
    protected Color rectColor = new Color(20, 20, 29, 120);
    protected Minecraft mc = Minecraft.getMinecraft();

    public abstract void initGui();

    public abstract void keyTyped(char typedChar, int keyCode);

    public abstract void drawScreen(int mouseX, int mouseY, float partialTicks, Animation initAnimation);

    public abstract void mouseClicked(int mouseX, int mouseY, int button, AltManagerUtils altManagerUtils);

    public int interpolateRectColor(Animation animation) {
        return ColorUtil.interpolateColor(rectColor, new Color(255, 255, 255, 120), (float) animation.getOutput());
    }
}
