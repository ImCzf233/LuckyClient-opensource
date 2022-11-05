package just.monika.LiteraruteMC.Lucky.module.impl.misc;

import just.monika.LiteraruteMC.Lucky.event.EventListener;
import just.monika.LiteraruteMC.Lucky.event.impl.network.PacketReceiveEvent;
import just.monika.LiteraruteMC.Lucky.module.Category;
import just.monika.LiteraruteMC.Lucky.settings.impl.BooleanSetting;
import just.monika.LiteraruteMC.Lucky.settings.impl.ModeSetting;
import just.monika.LiteraruteMC.Lucky.utils.network.PacketUtils;
import just.monika.LiteraruteMC.Lucky.module.Module;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public final class NoRotate extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", "Normal", "Normal", "Cancel");

    private final BooleanSetting fakeUpdate = new BooleanSetting("Fake Update", false);

    private final EventListener<PacketReceiveEvent> packetReceiveEventEventListener = e -> {
        if (e.getPacket() instanceof S08PacketPlayerPosLook) {
            S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook) e.getPacket();

            switch (mode.getMode()) {
                case "Normal":
                    packet.setYaw(mc.thePlayer.rotationYaw);
                    packet.setPitch(mc.thePlayer.rotationPitch);
                    break;
                case "Cancel":
                    e.cancel();
                    break;
            }

            if (fakeUpdate.isEnabled()) {
                PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX,
                        mc.thePlayer.posY,
                        mc.thePlayer.posZ,
                        packet.getYaw(),
                        packet.getPitch(),
                        mc.thePlayer.onGround));
            }
        }
    };

    public NoRotate() {
        super("NoRotate", Category.MISC, "prevents servers from forcing rotations");
        this.addSettings(fakeUpdate);
    }
}
