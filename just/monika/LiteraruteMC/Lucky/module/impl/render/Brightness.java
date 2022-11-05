package just.monika.LiteraruteMC.Lucky.module.impl.render;

import just.monika.LiteraruteMC.Lucky.event.EventListener;
import just.monika.LiteraruteMC.Lucky.event.impl.player.MotionEvent;
import just.monika.LiteraruteMC.Lucky.module.Category;
import just.monika.LiteraruteMC.Lucky.module.Module;
import just.monika.LiteraruteMC.Lucky.utils.Utils;

public final class Brightness extends Module {
    private final EventListener<MotionEvent> motionEventEventListener = event -> {
        Utils.mc.gameSettings.gammaSetting = 100;
    };

    @Override
    public void onDisable(){
        Utils.mc.gameSettings.gammaSetting = 0;
        super.onDisable();
    }

    public Brightness() {
        super("Brightness", Category.RENDER, "changes the game brightness");
    }
}
