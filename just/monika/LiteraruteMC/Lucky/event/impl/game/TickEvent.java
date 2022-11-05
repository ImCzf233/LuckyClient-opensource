package just.monika.LiteraruteMC.Lucky.event.impl.game;

import just.monika.LiteraruteMC.Lucky.event.Event;

public class TickEvent extends Event {

    private final int ticks;

    public TickEvent(int ticks) {
        this.ticks = ticks;
    }

    public int getTicks() {
        return ticks;
    }

}
