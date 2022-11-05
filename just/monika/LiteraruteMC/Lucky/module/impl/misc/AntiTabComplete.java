package just.monika.LiteraruteMC.Lucky.module.impl.misc;

import just.monika.LiteraruteMC.Lucky.event.EventListener;
import just.monika.LiteraruteMC.Lucky.event.impl.network.PacketReceiveEvent;
import just.monika.LiteraruteMC.Lucky.event.impl.network.PacketSendEvent;
import just.monika.LiteraruteMC.Lucky.module.Category;
import just.monika.LiteraruteMC.Lucky.module.Module;
import net.minecraft.network.play.client.C14PacketTabComplete;
import net.minecraft.network.play.server.S3APacketTabComplete;

@SuppressWarnings("unused")
public final class AntiTabComplete extends Module {

    public AntiTabComplete() {
        super("AntiTabComplete", Category.MISC, "prevents you from tab completing");
    }

    private final EventListener<PacketSendEvent> packetSendEventEventListener = event -> {
        if (event.getPacket() instanceof C14PacketTabComplete) {
            event.cancel();
        }
    };
    private final EventListener<PacketReceiveEvent> packetReceiveEventEventListener = event -> {
        if (event.getPacket() instanceof S3APacketTabComplete) {
            event.cancel();
        }
    };

}
