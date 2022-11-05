package just.monika.LiteraruteMC.Lucky.module.impl.combat;

import just.monika.LiteraruteMC.Lucky.event.EventListener;
import just.monika.LiteraruteMC.Lucky.event.impl.game.TickEvent;
import just.monika.LiteraruteMC.Lucky.module.Category;
import just.monika.LiteraruteMC.Lucky.module.Module;
import just.monika.LiteraruteMC.Lucky.utils.time.TimerUtil;
import net.minecraft.client.Minecraft;

import java.util.Random;

public class LegitAura extends Module {
    public LegitAura(){
        super("LegitAura", Category.COMBAT,"L");
    }
    private EventListener<TickEvent> ms = event ->{
        TimerUtil t = new TimerUtil();
        boolean Bool;
        Bool = new Random().nextBoolean();
        if (mc.objectMouseOver.entityHit != null) {
            if (t.hasTimeElapsed(new Random(1).nextInt(51) + 50))
                mc.thePlayer.swingItem();
            mc.playerController.attackEntity(mc.thePlayer, Minecraft.getMinecraft().objectMouseOver.entityHit);
        }
    };
}
