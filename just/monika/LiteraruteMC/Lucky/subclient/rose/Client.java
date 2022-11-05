package just.monika.LiteraruteMC.Lucky.subclient.rose;

import just.monika.LiteraruteMC.Lucky.event.Event;
import just.monika.LiteraruteMC.Lucky.LuckyClient;

public class Client {

    public static ClientType client;

    public static void dispatchEvent(Event event) {
        if (client == null) return;
        LuckyClient.INSTANCE.getEventProtocol().dispatch(event);
    }

}
