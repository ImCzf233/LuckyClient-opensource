package just.monika.LiteraruteMC.Lucky.ui.clickguis.vapulite;

import just.monika.LiteraruteMC.Lucky.LuckyClient;
import just.monika.LiteraruteMC.Lucky.module.Category;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClickGUI extends GuiScreen {

    private List<just.monika.LiteraruteMC.Lucky.ui.clickguis.vapulite.Panel> panels = new ArrayList<>();

    public ClickGUI() {
        int x = 20;

        for(final Category moduleCategory : Category.values()) {
            final just.monika.LiteraruteMC.Lucky.ui.clickguis.vapulite.Panel panel = new just.monika.LiteraruteMC.Lucky.ui.clickguis.vapulite.Panel(moduleCategory.name, x, 50, 100);
            LuckyClient.INSTANCE.getModuleCollection().getModules().stream().filter(module -> module.getCategory() == moduleCategory).forEach(module -> panel.addButton(new just.monika.LiteraruteMC.Lucky.ui.clickguis.vapulite.Button(panel, module)));
            panels.add(panel);
            x = x + 110;
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        // DO NOT DELETE THIS!
        fontRendererObj.drawStringWithShadow("LuckyClient Author: Monika", 5, this.height - 20, new Color(255, 255, 255).hashCode());
        fontRendererObj.drawStringWithShadow("L Vapu", 5, this.height - 10, new Color(255, 0, 0).hashCode());
        for(final just.monika.LiteraruteMC.Lucky.ui.clickguis.vapulite.Panel panel : panels) {
            Gui.drawRect(panel.getX(), panel.getY(), panel.getX() + panel.getWidth(), panel.getY() + 20, new Color(8, 85, 204).hashCode());
            fontRendererObj.drawString(panel.getPanelName(), panel.getX() + 5, panel.getY() + 5, 0xffffff);

            for(int i = 0; i < panel.getButtons().size(); i++) {
                final just.monika.LiteraruteMC.Lucky.ui.clickguis.vapulite.Button button = panel.getButtons().get(i);
                Gui.drawRect(panel.getX(), panel.getY() + 20 + (20 * i), panel.getX() + panel.getWidth(), panel.getY() + (20 * i) + 40, Integer.MIN_VALUE);
                fontRendererObj.drawString((button.getModule().isToggled() ? "§a" : "") + button.getModule().getName(), panel.getX() + 2, panel.getY() + 20 + (20 * i) + 7, 0xffffff);
            }

            if(panel.isDrag()) {
                panel.setX(mouseX + panel.getDragX());
                panel.setY(mouseY + panel.getDragY());
            }
        }
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if(mouseButton == 0) {
            for(int index = panels.size() - 1; index >= 0; index--) {
                final just.monika.LiteraruteMC.Lucky.ui.clickguis.vapulite.Panel panel = panels.get(index);


                if(panel.isHoverHead(mouseX, mouseY)) {
                    panel.setDrag(true);
                    panel.setDragX(panel.getX() - mouseX);
                    panel.setDragY(panel.getY() - mouseY);
                    panels.remove(panel);
                    panels.add(panel);
                    break;
                }


                for(int buttonIndex = 0; buttonIndex < panel.getButtons().size(); buttonIndex++) {
                    final just.monika.LiteraruteMC.Lucky.ui.clickguis.vapulite.Button button = panel.getButtons().get(buttonIndex);

                    if(button.isHover(panel.getX(),panel.getY() + 20 + (20 * buttonIndex), panel.getWidth(), 20, mouseX, mouseY)) {
                        button.getModule().setToggled(!button.getModule().isToggled());
                    }
                }
            }
        } else if(mouseButton == 1){
            for(int index = panels.size() - 1; index >= 0; index--) {
                final just.monika.LiteraruteMC.Lucky.ui.clickguis.vapulite.Panel panel = panels.get(index);


                if(panel.isHoverHead(mouseX, mouseY)) {
                    panel.setDrag(true);
                    panel.setDragX(panel.getX() - mouseX);
                    panel.setDragY(panel.getY() - mouseY);
                    panels.remove(panel);
                    panels.add(panel);
                    break;
                }


                for(int buttonIndex = 0; buttonIndex < panel.getButtons().size(); buttonIndex++) {
                    final Button button = panel.getButtons().get(buttonIndex);
                }
            }
        }


        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        for(final Panel panel : panels)
            panel.setDrag(false);
        super.mouseReleased(mouseX, mouseY, state);
    }
}