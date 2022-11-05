package just.monika.LiteraruteMC.Lucky.ui;

import just.monika.LiteraruteMC.Lucky.utils.Utils;

public interface Screen extends Utils {

    void initGui();

    void keyTyped(char typedChar, int keyCode);

    void drawScreen(int mouseX, int mouseY);

    void mouseClicked(int mouseX, int mouseY, int button);

    void mouseReleased(int mouseX, int mouseY, int state);

}
