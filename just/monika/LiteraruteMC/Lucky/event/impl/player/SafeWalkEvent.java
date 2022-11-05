package just.monika.LiteraruteMC.Lucky.event.impl.player;

import just.monika.LiteraruteMC.Lucky.event.Event;


public class SafeWalkEvent extends Event {

    private boolean safe;

    public boolean isSafe() {
        return this.safe;
    }

     
    public void setSafe(boolean safe) {
        this.safe = safe;
    }

}
