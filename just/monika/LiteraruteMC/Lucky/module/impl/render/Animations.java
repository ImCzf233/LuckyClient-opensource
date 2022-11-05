package just.monika.LiteraruteMC.Lucky.module.impl.render;


import just.monika.LiteraruteMC.Lucky.module.Category;
import just.monika.LiteraruteMC.Lucky.module.Module;
import just.monika.LiteraruteMC.Lucky.settings.impl.BooleanSetting;
import just.monika.LiteraruteMC.Lucky.settings.impl.ModeSetting;

public final class Animations extends Module {

    public static final ModeSetting modeSetting = new ModeSetting("Mode", "LoL",
            "LoL", "Stella", "Fathum", "1.7", "Exhi", "Exhi 2", "Shred", "Smooth", "Sigma");
    public static final BooleanSetting oldDamage = new BooleanSetting("Old Damage", false);

    public Animations() {
        super("Animations", Category.RENDER, "changes animations");
        this.addSettings(modeSetting, oldDamage);
    }

}
