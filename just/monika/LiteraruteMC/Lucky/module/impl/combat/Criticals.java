package just.monika.LiteraruteMC.Lucky.module.impl.combat;


import just.monika.LiteraruteMC.Lucky.event.EventListener;
import just.monika.LiteraruteMC.Lucky.event.impl.player.MotionEvent;
import just.monika.LiteraruteMC.Lucky.module.Category;
import just.monika.LiteraruteMC.Lucky.module.Module;
import just.monika.LiteraruteMC.Lucky.settings.impl.ModeSetting;
import just.monika.LiteraruteMC.Lucky.settings.impl.NumberSetting;
import just.monika.LiteraruteMC.Lucky.utils.network.PacketUtils;
import just.monika.LiteraruteMC.Lucky.utils.time.TimerUtil;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.client.C18PacketSpectate;

import java.util.UUID;

@SuppressWarnings("unused")
public final class Criticals extends Module {

    private final ModeSetting modeSetting = new ModeSetting("Mode", "Watchdog", "Watchdog", "Packet", "Dev");
    private final NumberSetting delay = new NumberSetting("Delay", 1,20, 0, 1);
    private final TimerUtil timer = new TimerUtil();

    public Criticals() {
        super("Criticals", Category.COMBAT, "Crit attacks");
        this.addSettings(modeSetting, delay);
    }

    private final EventListener<MotionEvent> onMotion = e -> {
        switch (modeSetting.getMode()) {
            case "Watchdog":
                if (KillAura.attacking && e.isOnGround()) {
                    if (KillAura.target.hurtTime > delay.getValue().intValue()) {
                        for (double offset : new double[]{0.06f, 0.01f}) {
                            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + offset + (Math.random() * 0.001), mc.thePlayer.posZ, false));
                        }
                    }
                }
                break;
            case "Packet":
                if (mc.objectMouseOver.entityHit != null && mc.thePlayer.onGround) {
                    if (mc.objectMouseOver.entityHit.hurtResistantTime > delay.getValue().intValue()) {
                        for (double offset : new double[]{0.006253453, 0.002253453, 0.001253453}) {
                            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + offset, mc.thePlayer.posZ, false));
                        }
                    }
                }
                break;
            case "Dev":
                if (mc.objectMouseOver.entityHit != null && mc.thePlayer.onGround) {
                    if (mc.objectMouseOver.entityHit.hurtResistantTime > delay.getValue().intValue()) {
                        for (double offset : new double[]{0.06253453, 0.02253453, 0.001253453, 0.0001135346}) {
                            mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + offset, mc.thePlayer.posZ, false));
                            PacketUtils.sendPacketNoEvent(new C18PacketSpectate(UUID.randomUUID()));
                        }
                    }
                }
                break;
        }
    };

}
