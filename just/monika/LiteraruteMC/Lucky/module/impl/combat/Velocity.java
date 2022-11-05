package just.monika.LiteraruteMC.Lucky.module.impl.combat;

import just.monika.LiteraruteMC.Lucky.event.EventListener;
import just.monika.LiteraruteMC.Lucky.event.impl.network.PacketReceiveEvent;
import just.monika.LiteraruteMC.Lucky.module.Category;
import just.monika.LiteraruteMC.Lucky.module.Module;
import just.monika.LiteraruteMC.Lucky.settings.Setting;
import just.monika.LiteraruteMC.Lucky.settings.impl.BooleanSetting;
import just.monika.LiteraruteMC.Lucky.settings.impl.ModeSetting;
import just.monika.LiteraruteMC.Lucky.settings.impl.NumberSetting;
import just.monika.LiteraruteMC.Lucky.utils.misc.MathUtils;
import just.monika.LiteraruteMC.Lucky.utils.player.MovementUtils;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

public class Velocity extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", "Packet", "Packet", "Matrix");
    private final NumberSetting horizontal = new NumberSetting("Horizontal", 0, 100, 0, 1);
    private final NumberSetting vertical = new NumberSetting("Vertical", 0, 100, 0, 1);
    private final NumberSetting chance = new NumberSetting("Chance", 100, 100, 0, 1);
    private final BooleanSetting onlyWhileMoving = new BooleanSetting("Only while moving", false);

    public Velocity() {
        super("Velocity", Category.COMBAT, "Reduces your knockback");
        Setting.addParent(mode, m -> m.is("Packet"), horizontal, vertical);
        this.addSettings(mode, horizontal, vertical, chance, onlyWhileMoving);
    }

    private final EventListener<PacketReceiveEvent> onPacketReceive = e -> {
        if ((onlyWhileMoving.isEnabled() && !MovementUtils.isMoving()) || (chance.getValue() != 100 && MathUtils.getRandomInRange(0, 100) > chance.getValue()))
            return;
        Packet<?> packet = e.getPacket();
        switch (mode.getMode()) {
            case "Packet":
                if (packet instanceof S12PacketEntityVelocity) {
                    S12PacketEntityVelocity s12 = (S12PacketEntityVelocity) e.getPacket();
                    if (mc.thePlayer != null && s12.getEntityID() == mc.thePlayer.getEntityId()) {
                        if (cancel(e)) return;
                        s12.motionX *= horizontal.getValue() / 100.0;
                        s12.motionZ *= horizontal.getValue() / 100.0;
                        s12.motionY *= vertical.getValue() / 100.0;
                    }
                } else if (packet instanceof S27PacketExplosion) {
                    if (cancel(e)) return;
                    S27PacketExplosion s27 = (S27PacketExplosion) e.getPacket();
                    s27.motionX *= horizontal.getValue() / 100.0;
                    s27.motionZ *= horizontal.getValue() / 100.0;
                    s27.motionY *= vertical.getValue() / 100.0;
                }
                break;
            case "Matrix":
                if (packet instanceof S12PacketEntityVelocity) {
                    S12PacketEntityVelocity s12 = (S12PacketEntityVelocity) e.getPacket();
                    if (mc.thePlayer != null && s12.getEntityID() == mc.thePlayer.getEntityId()) {
                        s12.motionX *= 5 / 100.0;
                        s12.motionZ *= 5 / 100.0;
                        s12.motionY *= 100 / 100.0;
                    }
                }
                break;
        }
    };

    private boolean cancel(PacketReceiveEvent e) {
        if (horizontal.getValue() == 0 && vertical.getValue() == 0) {
            e.cancel();
            return true;
        }
        return false;
    }

}
