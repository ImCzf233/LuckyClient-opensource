package just.monika.LiteraruteMC.Lucky.module.impl.player;

import just.monika.LiteraruteMC.Lucky.LuckyClient;
import just.monika.LiteraruteMC.Lucky.event.EventListener;
import just.monika.LiteraruteMC.Lucky.event.impl.player.MotionEvent;
import just.monika.LiteraruteMC.Lucky.module.Category;
import just.monika.LiteraruteMC.Lucky.module.Module;
import just.monika.LiteraruteMC.Lucky.settings.impl.ModeSetting;
import just.monika.LiteraruteMC.Lucky.utils.misc.MathUtils;
import just.monika.LiteraruteMC.Lucky.utils.network.PacketUtils;
import just.monika.LiteraruteMC.Lucky.module.impl.movement.Flight;
import net.minecraft.network.play.client.C03PacketPlayer;

public class AntiVoid extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", "Watchdog", "Watchdog");

    public AntiVoid() {
        super("AntiVoid", Category.PLAYER, "saves you from the void");
        this.addSettings(mode);
    }

    private final EventListener<MotionEvent> onMotion = e -> {
        this.setSuffix(mode.getMode());
        if (LuckyClient.INSTANCE.isToggled(Flight.class) || mc.thePlayer.isDead) return;
        if (e.isPre()) {
            switch (mode.getMode()) {
                case "Watchdog":
                    PacketUtils.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + MathUtils.getRandomInRange(10, 12), mc.thePlayer.posZ, false));
                    break;
            }
        }
    };

}
