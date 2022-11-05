package just.monika.LiteraruteMC.Lucky.event.impl.render;

import just.monika.LiteraruteMC.Lucky.event.Event;

public class Render3DEvent extends Event {

    private float ticks;

    public Render3DEvent(float ticks) {
        this.ticks = ticks;
    }

    public float getTicks() {
        return ticks;
    }

    public void setTicks(float ticks) {
        this.ticks = ticks;
    }

}
