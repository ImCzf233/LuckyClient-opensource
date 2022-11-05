package just.monika.LiteraruteMC.Lucky.module.impl.render;

import just.monika.LiteraruteMC.Lucky.module.Category;
import just.monika.LiteraruteMC.Lucky.module.Module;
import just.monika.LiteraruteMC.Lucky.settings.impl.BooleanSetting;

public class ScoreboardMod extends Module {

    public static final BooleanSetting textShadow = new BooleanSetting("Text Shadow", true);
    public static final BooleanSetting redNumbers = new BooleanSetting("Red Numbers", false);

    public ScoreboardMod() {
        super("Scoreboard", Category.RENDER, "Scoreboard preferences");
        this.addSettings(textShadow, redNumbers);
        this.setToggled(true);
    }

}
