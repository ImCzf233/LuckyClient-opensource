package just.monika.LiteraruteMC.Lucky.module.impl.misc;

import just.monika.LiteraruteMC.Lucky.event.EventListener;
import just.monika.LiteraruteMC.Lucky.event.impl.game.TickEvent;
import just.monika.LiteraruteMC.Lucky.module.Category;
import just.monika.LiteraruteMC.Lucky.module.Module;
import just.monika.LiteraruteMC.Lucky.settings.impl.BooleanSetting;
import just.monika.LiteraruteMC.Lucky.settings.impl.MultipleBoolSetting;
import just.monika.LiteraruteMC.Lucky.ui.notifications.NotificationManager;
import just.monika.LiteraruteMC.Lucky.ui.notifications.NotificationType;
import just.monika.LiteraruteMC.Lucky.utils.Utils;
import just.monika.LiteraruteMC.Lucky.utils.time.TimerUtil;
import just.monika.LiteraruteMC.Lucky.hackerdetector.Detection;
import just.monika.LiteraruteMC.Lucky.hackerdetector.DetectionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumChatFormatting;

public class HackerDetector extends Module {

    private DetectionManager detectionManager = new DetectionManager();
    private TimerUtil timer = new TimerUtil();
    private final MultipleBoolSetting detections = new MultipleBoolSetting("Detections",
            new BooleanSetting("Flight A", true),
            new BooleanSetting("Flight B", true),
            new BooleanSetting("Reach A", true));

    public HackerDetector() {
        super("HackerDetector", Category.MISC, "Detects people using cheats inside your game");
        this.addSettings(detections);
    }

    private final EventListener<TickEvent> onTick = e -> {
        if(Utils.mc.theWorld == null || Utils.mc.thePlayer == null) return;
        for(Entity entity : Utils.mc.theWorld.getLoadedEntityList()) {
            if(entity instanceof EntityPlayer) {
                EntityPlayer entityPlayer = (EntityPlayer) entity;
                if(entityPlayer != Utils.mc.thePlayer) {
                    for(Detection d : detectionManager.getDetections()) {
                        if(detections.getSetting(d.getName()).isEnabled()) {
                            if(d.runCheck(entityPlayer) && System.currentTimeMillis() > d.getLastViolated() + 500) {
                                NotificationManager.post(NotificationType.WARNING, entityPlayer.getName(), "has flagged " + d.getName() + " | " + EnumChatFormatting.BOLD + entityPlayer.VL);
                                entityPlayer.VL++;
                                d.setLastViolated(System.currentTimeMillis());
                            }
                        }
                    }
                }
            }
        }
    };
}
