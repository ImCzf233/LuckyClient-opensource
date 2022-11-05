package just.monika.LiteraruteMC.Lucky.module.impl.player;

import just.monika.LiteraruteMC.Lucky.event.EventListener;
import just.monika.LiteraruteMC.Lucky.event.impl.network.PacketReceiveEvent;
import just.monika.LiteraruteMC.Lucky.event.impl.player.BoundingBoxEvent;
import just.monika.LiteraruteMC.Lucky.event.impl.player.MotionEvent;
import just.monika.LiteraruteMC.Lucky.event.impl.player.PushOutOfBlockEvent;
import just.monika.LiteraruteMC.Lucky.module.Category;
import just.monika.LiteraruteMC.Lucky.module.Module;
import net.minecraft.network.play.client.C03PacketPlayer;

public final class Freecam extends Module {

    private final EventListener<MotionEvent> motionEventEventListener = event -> {

    };

    private final EventListener<BoundingBoxEvent> boundingBoxEventEventListener = event -> {
        if(mc.thePlayer != null) {
            event.cancel();
        }
    };

    private final EventListener<PushOutOfBlockEvent> pushOutOfBlockEventEventListener = event -> {
        if(mc.thePlayer != null) {
            event.cancel();
        }
    };

    private final EventListener<PacketReceiveEvent> packetReceiveEventEventListener = event -> {
        if (event.getPacket() instanceof C03PacketPlayer) {
            event.cancel();
        }
    };

    @Override
    public void onEnable(){
        if(mc.thePlayer != null) {
            mc.thePlayer.capabilities.allowFlying = true;
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        if(mc.thePlayer != null) {
            mc.thePlayer.capabilities.allowFlying = false;
            mc.thePlayer.capabilities.isFlying = false;
        }
        super.onDisable();
    }

    public Freecam() {
        super("Freecam", Category.PLAYER, "allows you to look around freely");
    }
}
