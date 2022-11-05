package just.monika.LiteraruteMC.Lucky.module.impl.player;

import just.monika.LiteraruteMC.Lucky.event.EventListener;
import just.monika.LiteraruteMC.Lucky.event.impl.network.PacketSendEvent;
import just.monika.LiteraruteMC.Lucky.event.impl.player.MotionEvent;
import just.monika.LiteraruteMC.Lucky.module.Category;
import just.monika.LiteraruteMC.Lucky.module.Module;
import just.monika.LiteraruteMC.Lucky.settings.impl.ModeSetting;
import just.monika.LiteraruteMC.Lucky.utils.network.PacketUtils;
import net.minecraft.network.play.client.C03PacketPlayer;

@SuppressWarnings("unused")
public final class NoFall extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla", "Packet", "Edit");

    private final EventListener<MotionEvent> motionEventEventListener = event -> {
        if (event.isPre()) {
            if (mc.thePlayer.fallDistance > 3.0) {
                switch (mode.getMode()) {
                    case "Vanilla":
                        event.setOnGround(true);
                        break;
                    case "Packet":
                        PacketUtils.sendPacket(new C03PacketPlayer(true));
                        break;
                }
                mc.thePlayer.fallDistance = 0;
            }
        }
    };

    private final EventListener<PacketSendEvent> packetSendEventEventListener = event -> {
    };

    public NoFall() {
        super("NoFall", Category.PLAYER, "pervents fall damage");
        this.addSettings(mode);
    }
}
