package just.monika.LiteraruteMC.Lucky.module.impl.combat;

import just.monika.LiteraruteMC.Lucky.event.EventListener;
import just.monika.LiteraruteMC.Lucky.event.impl.player.MotionEvent;
import just.monika.LiteraruteMC.Lucky.module.Category;
import just.monika.LiteraruteMC.Lucky.module.Module;
import just.monika.LiteraruteMC.Lucky.settings.impl.ModeSetting;
import just.monika.LiteraruteMC.Lucky.utils.network.PacketUtils;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.C03PacketPlayer;
import org.lwjgl.input.Mouse;


public final class Fastbow extends Module {

    private final ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla", "Ghostly");

    private final EventListener<MotionEvent> motionEventEventListener = event -> {
        if(mc.thePlayer.getCurrentEquippedItem() == null) return;
        switch (mode.getMode()) {
            case "Vanilla":
            if (Mouse.isButtonDown(1) && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow) {
                for (int i = 0; i < 20; ++i) {
                    mc.rightClickDelayTimer = 0;
                    PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, mc.thePlayer.onGround));
                }
                mc.playerController.onStoppedUsingItem(mc.thePlayer);
            }
            break;
            case "Ghostly":
                if(Mouse.isButtonDown(1) && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBow){
                    for(int i = 0; i < 20; i++){
                        mc.rightClickDelayTimer = 0;
                        PacketUtils.sendPacketNoEvent(new C03PacketPlayer(true));
                    }
                    mc.playerController.onStoppedUsingItem(mc.thePlayer);
                }
                break;
        }
    };

    @Override
    public void onDisable(){
        mc.rightClickDelayTimer = 4;
        super.onDisable();
    }

    public Fastbow() {
        super("Fastbow", Category.COMBAT, "shoot bows faster");
        this.addSettings(mode);
    }
}
