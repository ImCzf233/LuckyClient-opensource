package just.monika.LiteraruteMC.Lucky.module.impl.render;

import just.monika.LiteraruteMC.Lucky.event.EventListener;
import just.monika.LiteraruteMC.Lucky.event.impl.game.TickEvent;
import just.monika.LiteraruteMC.Lucky.module.Category;
import just.monika.LiteraruteMC.Lucky.module.Module;

public class FixInp extends Module {
    private final EventListener<TickEvent> tickEventEventListener = event -> {
    };
    public FixInp() {
        super("FixInput", Category.RENDER, "L");
    }
}
