package just.monika.LiteraruteMC.Lucky.module.impl.misc;

import just.monika.LiteraruteMC.Lucky.event.EventListener;
import just.monika.LiteraruteMC.Lucky.event.impl.game.TickEvent;
import just.monika.LiteraruteMC.Lucky.module.Category;
import just.monika.LiteraruteMC.Lucky.utils.network.PacketUtils;
import just.monika.LiteraruteMC.Lucky.module.Module;
import net.minecraft.network.play.client.C16PacketClientStatus;

public final class AutoRespawn extends Module {

    private final EventListener<TickEvent> tickEventEventListener = event -> {
        if(mc.thePlayer.isDead){
            PacketUtils.sendPacketNoEvent(new C16PacketClientStatus(C16PacketClientStatus.EnumState.PERFORM_RESPAWN));
        }
    };

    public AutoRespawn() {
        super("AutoRespawn", Category.MISC, "automatically respawn");
    }
}
