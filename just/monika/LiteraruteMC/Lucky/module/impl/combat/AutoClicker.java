package just.monika.LiteraruteMC.Lucky.module.impl.combat;

import just.monika.LiteraruteMC.Lucky.event.EventListener;
import just.monika.LiteraruteMC.Lucky.event.impl.game.TickEvent;
import just.monika.LiteraruteMC.Lucky.module.Category;
import just.monika.LiteraruteMC.Lucky.module.Module;

public class AutoClicker extends Module {
    public AutoClicker(){
        super("AutoClicker", Category.COMBAT,"L Not Skidded");
    }
    private EventListener<TickEvent> tickEventEventListener = event -> {

    };
}
