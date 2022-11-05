package just.monika.LiteraruteMC.Lucky.module.impl.misc;

import just.monika.LiteraruteMC.Lucky.event.EventListener;
import just.monika.LiteraruteMC.Lucky.event.impl.network.PacketSendEvent;
import just.monika.LiteraruteMC.Lucky.event.impl.player.MotionEvent;
import just.monika.LiteraruteMC.Lucky.module.Category;
import just.monika.LiteraruteMC.Lucky.utils.network.PacketUtils;
import just.monika.LiteraruteMC.Lucky.module.Module;
import net.minecraft.network.play.client.C09PacketHeldItemChange;

@SuppressWarnings("unused")
public final class AntiDesync extends Module {

    private int slot;

    public AntiDesync() {
        super("AntiDesync", Category.MISC, "pervents desync client side");
    }

    private final EventListener<PacketSendEvent> packetSendEventEventListener = event -> {
        if (event.getPacket() instanceof C09PacketHeldItemChange) {
            slot = ((C09PacketHeldItemChange) event.getPacket()).getSlotId();
        }
    };

    private final EventListener<MotionEvent> motionEventEventListener = event -> {
        if (slot != mc.thePlayer.inventory.currentItem && slot != -1) {
            PacketUtils.sendPacketNoEvent(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
        }
    };

}
