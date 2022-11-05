package just.monika.LiteraruteMC.Lucky.event.impl.network;

import just.monika.LiteraruteMC.Lucky.event.Event;
import net.minecraft.network.Packet;

public class PacketReceiveEvent extends Event {

    private Packet<?> packet;

    public PacketReceiveEvent(Packet<?> packet) {
        this.packet = packet;
    }

    public Packet<?> getPacket() {
        return packet;
    }

    public void setPacket(Packet<?> packet) {
        this.packet = packet;
    }

}
