package just.monika.LiteraruteMC.Lucky.ui.clickguis.vapulite;

import just.monika.LiteraruteMC.Lucky.module.Module;

public class Button {

    private Panel panel;
    private Module module;

    public Button(Panel panel, Module module) {
        this.panel = panel;
        this.module = module;
    }

    public void click() {
        module.toggle();
    }

    public Module getModule() {
        return module;
    }

    public boolean isHover(final int x, final int y, final int widht, final int height, final int mouseX, final int mouseY) {
        return mouseX >= x && mouseX <= x + widht && mouseY >= y && mouseY <= y + height;
    }
}