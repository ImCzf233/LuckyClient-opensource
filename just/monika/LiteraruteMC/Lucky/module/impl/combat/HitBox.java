package just.monika.LiteraruteMC.Lucky.module.impl.combat;

import just.monika.LiteraruteMC.Lucky.module.Category;
import just.monika.LiteraruteMC.Lucky.module.Module;
import just.monika.LiteraruteMC.Lucky.settings.impl.NumberSetting;

public class HitBox extends Module {
    public final NumberSetting box=new NumberSetting("Box", 10, 100, 0, 10);
    public HitBox() {
        super("HitBox", Category.COMBAT,"Make HitBox Bigger");
        addSettings(box);
    }
}
