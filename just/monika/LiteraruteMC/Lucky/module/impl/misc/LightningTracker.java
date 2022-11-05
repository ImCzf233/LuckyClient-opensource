package just.monika.LiteraruteMC.Lucky.module.impl.misc;

import just.monika.LiteraruteMC.Lucky.event.EventListener;
import just.monika.LiteraruteMC.Lucky.event.impl.network.PacketReceiveEvent;
import just.monika.LiteraruteMC.Lucky.module.Category;
import just.monika.LiteraruteMC.Lucky.utils.player.ChatUtils;
import just.monika.LiteraruteMC.Lucky.module.Module;
import net.minecraft.network.play.server.S29PacketSoundEffect;

public final class LightningTracker extends Module {

    public LightningTracker() {
        super("LightningTracker", Category.MISC, "detects lightning");
    }

    private final EventListener<PacketReceiveEvent> onPacketReceive = e -> {
        if (e.getPacket() instanceof S29PacketSoundEffect) {
            S29PacketSoundEffect soundPacket = ((S29PacketSoundEffect) e.getPacket());
            if (soundPacket.getSoundName().equals("ambient.weather.thunder")) {
                ChatUtils.print(String.format("Lightning detected at (%s, %s, %s)", (int) soundPacket.getX(), (int) soundPacket.getY(), (int) soundPacket.getZ()));
            }
        }
    };

}
