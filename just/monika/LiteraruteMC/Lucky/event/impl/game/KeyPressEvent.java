package just.monika.LiteraruteMC.Lucky.event.impl.game;

import just.monika.LiteraruteMC.Lucky.event.Event;

public class KeyPressEvent extends Event {

    private final int key;

    public KeyPressEvent(int key) {
        this.key = key;
    }

    public int getKey() {
        return key;
    }

}
