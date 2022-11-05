package just.monika.LiteraruteMC.Lucky.module.impl.movement;

import just.monika.LiteraruteMC.Lucky.event.EventListener;
import just.monika.LiteraruteMC.Lucky.event.impl.player.MotionEvent;
import just.monika.LiteraruteMC.Lucky.module.Category;
import just.monika.LiteraruteMC.Lucky.module.Module;
import just.monika.LiteraruteMC.Lucky.settings.impl.BooleanSetting;
import just.monika.LiteraruteMC.Lucky.settings.impl.ModeSetting;
import just.monika.LiteraruteMC.Lucky.settings.impl.NumberSetting;
import just.monika.LiteraruteMC.Lucky.utils.player.RotationUtils;
import just.monika.LiteraruteMC.Lucky.utils.player.ScaffoldUtils;
import just.monika.LiteraruteMC.Lucky.utils.time.TimerUtil;
import net.minecraft.network.play.client.*;
import net.minecraft.potion.Potion;

public class Scaffold extends Module {

    private ScaffoldUtils.BlockCache blockCache, lastBlockCache;
    private ModeSetting placetype = new ModeSetting("Place Type", "Post", "Pre", "Post");
    public static NumberSetting extend = new NumberSetting("Extend", 0, 6, 0, 0.01);
    public static BooleanSetting sprint = new BooleanSetting("Sprint", false);
    private BooleanSetting tower = new BooleanSetting("Tower", false);
    private NumberSetting towerTimer = new NumberSetting("Tower Timer Boost", 1.2, 5, 0.1, 0.1);
    private BooleanSetting swing = new BooleanSetting("Swing", false);
    private float rotations[];
    private TimerUtil timer = new TimerUtil();

    public Scaffold() {
        super("Scaffold", Category.MOVEMENT, "Automatically places blocks under you");
        this.addSettings(placetype, extend, sprint, tower, towerTimer, swing);
        towerTimer.addParent(tower, mode -> mode.isEnabled());
    }

    private final EventListener<MotionEvent> onMotion = e -> {

        if(e.isPre()) {

            // Rotations
            if(lastBlockCache != null) {
                rotations = RotationUtils.getFacingRotations2(lastBlockCache.getPosition().getX(), lastBlockCache.getPosition().getY(), lastBlockCache.getPosition().getZ());
                mc.thePlayer.renderYawOffset = rotations[0];
                mc.thePlayer.rotationYawHead = rotations[0];
                e.setYaw(rotations[0]);
                e.setPitch(81);
                mc.thePlayer.rotationPitchHead = 81;
            } else {
                e.setPitch(81);
                e.setYaw(mc.thePlayer.rotationYaw + 180);
                mc.thePlayer.rotationPitchHead = 81;
                mc.thePlayer.renderYawOffset = mc.thePlayer.rotationYaw + 180;
                mc.thePlayer.rotationYawHead = mc.thePlayer.rotationYaw + 180;
            }

            // Speed 2 Slowdown
            if(mc.thePlayer.isPotionActive(Potion.moveSpeed.id)){
                mc.thePlayer.motionX *= 0.66;
                mc.thePlayer.motionZ *= 0.66;
            }

            // Setting Block Cache
            blockCache = ScaffoldUtils.grab();
            if (blockCache != null) {
                lastBlockCache = ScaffoldUtils.grab();
            }else{
                return;
            }

            // Setting Item Slot (Pre)
            int slot = ScaffoldUtils.grabBlockSlot();
            if(slot == -1) return;

            // Setting Slot
            mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(slot));

            // Placing Blocks (Pre)
            if(placetype.getMode().equalsIgnoreCase("Pre")){
                if (blockCache == null) return;
                mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getStackInSlot(slot), lastBlockCache.position, lastBlockCache.facing, ScaffoldUtils.getHypixelVec3(lastBlockCache));
                if(swing.isEnabled()){
                    mc.thePlayer.swingItem();
                }
                mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                blockCache = null;
            }
        }else{

            // Tower
            if(tower.isEnabled()) {
                if(mc.gameSettings.keyBindJump.isKeyDown()) {
                    mc.timer.timerSpeed = towerTimer.getValue().floatValue();
                    if(mc.thePlayer.motionY < 0) {
                        mc.thePlayer.jump();
                    }
                }else{
                    mc.timer.timerSpeed = 1;
                }
            }

            // Setting Item Slot (Post)
            int slot = ScaffoldUtils.grabBlockSlot();
            if(slot == -1) return;

            // Placing Blocks (Post)
            if(placetype.getMode().equalsIgnoreCase("Post")){
                if (blockCache == null) return;
                mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getStackInSlot(slot), lastBlockCache.position, lastBlockCache.facing, ScaffoldUtils.getHypixelVec3(lastBlockCache));
                if(swing.isEnabled()){
                    mc.thePlayer.swingItem();
                }
                mc.thePlayer.sendQueue.addToSendQueue(new C0APacketAnimation());
                blockCache = null;
            }
        }
    };

    @Override
    public void onDisable() {
        mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
        super.onDisable();
    }

    @Override
    public void onEnable() {
        lastBlockCache = null;
        super.onEnable();
    }
}
