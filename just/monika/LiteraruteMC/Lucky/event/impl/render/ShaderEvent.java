package just.monika.LiteraruteMC.Lucky.event.impl.render;

import just.monika.LiteraruteMC.Lucky.event.Event;

public class ShaderEvent extends Event {

    public final boolean bloom;
    public ShaderEvent(boolean bloom){
        this.bloom = bloom;
    }
}
