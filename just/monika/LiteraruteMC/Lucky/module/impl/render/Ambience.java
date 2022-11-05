package just.monika.LiteraruteMC.Lucky.module.impl.render;

import just.monika.LiteraruteMC.Lucky.event.EventListener;
import just.monika.LiteraruteMC.Lucky.event.impl.game.TickEvent;
import just.monika.LiteraruteMC.Lucky.event.impl.network.PacketReceiveEvent;
import just.monika.LiteraruteMC.Lucky.module.Category;
import just.monika.LiteraruteMC.Lucky.module.Module;
import just.monika.LiteraruteMC.Lucky.settings.impl.NumberSetting;
import net.minecraft.network.play.server.S03PacketTimeUpdate;

public class Ambience extends Module {

    private final NumberSetting time = new NumberSetting("Time", 12000, 24000, 0, 1000);

    public Ambience() {
        super("Ambience", Category.RENDER, "world time");
        this.addSettings(time);
    }

    private final EventListener<TickEvent> onTick = e -> {
        if (mc.theWorld != null) {
            mc.theWorld.setWorldTime(time.getValue().longValue());
        }
    };

    private final EventListener<PacketReceiveEvent> onPacketReceive = e -> {
        if (e.getPacket() instanceof S03PacketTimeUpdate) {
            e.cancel();
        }
    };

}
