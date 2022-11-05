package just.monika.LiteraruteMC.Lucky.module.impl.movement;

import just.monika.LiteraruteMC.Lucky.LuckyClient;
import just.monika.LiteraruteMC.Lucky.event.EventListener;
import just.monika.LiteraruteMC.Lucky.event.impl.player.MotionEvent;
import just.monika.LiteraruteMC.Lucky.module.Category;
import just.monika.LiteraruteMC.Lucky.module.Module;
import just.monika.LiteraruteMC.Lucky.module.impl.player.NoSlow;
import just.monika.LiteraruteMC.Lucky.settings.impl.BooleanSetting;
import just.monika.LiteraruteMC.Lucky.utils.player.MovementUtils;

public class Sprint extends Module {

    private final BooleanSetting omniSprint = new BooleanSetting("Omni Sprint", false);

    public Sprint() {
        super("Sprint", Category.MOVEMENT, "Sprints automatically");
        this.addSettings(omniSprint);
    }

    private final EventListener<MotionEvent> onMotion = e -> {
        if (LuckyClient.INSTANCE.getModuleCollection().get(Scaffold.class).isToggled() && !Scaffold.sprint.isEnabled()) {
            mc.gameSettings.keyBindSprint.pressed = false;
            mc.thePlayer.setSprinting(false);
            return;
        }
        if (omniSprint.isEnabled()) {
            mc.thePlayer.setSprinting(MovementUtils.isMoving());
        } else {
            if(mc.thePlayer.isUsingItem()) {
                if (mc.thePlayer.moveForward > 0 && (LuckyClient.INSTANCE.getModuleCollection().get(NoSlow.class).isToggled() || !mc.thePlayer.isUsingItem()) && !mc.thePlayer.isSneaking() && !mc.thePlayer.isCollidedHorizontally && mc.thePlayer.getFoodStats().getFoodLevel() > 6) {
                    mc.thePlayer.setSprinting(true);
                }
            }else{
                mc.gameSettings.keyBindSprint.pressed = true;
            }
        }
    };

    @Override
    public void onDisable() {
        mc.thePlayer.setSprinting(false);
        super.onDisable();
    }

}
