package just.monika.LiteraruteMC.Lucky.module.impl.combat;

import just.monika.LiteraruteMC.Lucky.event.EventListener;
import just.monika.LiteraruteMC.Lucky.event.impl.player.MotionEvent;
import just.monika.LiteraruteMC.Lucky.module.Category;
import just.monika.LiteraruteMC.Lucky.module.Module;
import just.monika.LiteraruteMC.Lucky.settings.impl.NumberSetting;


public final class Reach extends Module {
    private final NumberSetting reach = new NumberSetting("Reach", 4, 6, 1, 0.1);
    public static double dbReach;
    
    public Reach() {
        super("Reach", Category.COMBAT, "reach");
    }
    
    private final EventListener<MotionEvent> motionEventEventListener = event -> {
    	dbReach = this.reach.getValue();
    };
}
