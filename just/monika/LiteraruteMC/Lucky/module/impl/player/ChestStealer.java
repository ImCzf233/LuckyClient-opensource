package just.monika.LiteraruteMC.Lucky.module.impl.player;

import com.google.common.collect.Lists;
import just.monika.LiteraruteMC.Lucky.LuckyClient;
import just.monika.LiteraruteMC.Lucky.event.EventListener;
import just.monika.LiteraruteMC.Lucky.event.impl.player.MotionEvent;
import just.monika.LiteraruteMC.Lucky.module.Category;
import just.monika.LiteraruteMC.Lucky.module.Module;
import just.monika.LiteraruteMC.Lucky.settings.impl.BooleanSetting;
import just.monika.LiteraruteMC.Lucky.settings.impl.NumberSetting;
import just.monika.LiteraruteMC.Lucky.utils.time.TimerUtil;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.inventory.ContainerChest;

import java.util.Collections;
import java.util.List;

public class ChestStealer extends Module {

    private final NumberSetting delay = new NumberSetting("Delay", 80, 300, 0, 10);
    public static BooleanSetting titleCheck = new BooleanSetting("Title Check", true);
    public static BooleanSetting freeLook = new BooleanSetting("Free Look", true);
    private final BooleanSetting reverse = new BooleanSetting("Reverse", false);

    private final TimerUtil timer = new TimerUtil();

    public ChestStealer() {
        super("ChestStealer", Category.PLAYER, "auto loot chests");
        this.addSettings(delay, titleCheck, freeLook, reverse);
    }

    private final EventListener<MotionEvent> onMotion = e -> {
        if (e.isPre() && mc.thePlayer.openContainer instanceof ContainerChest) {
            ContainerChest chest = (ContainerChest) mc.thePlayer.openContainer;
            String chestName = chest.getLowerChestInventory().getName();
            if (titleCheck.isEnabled() && !((chestName.contains("Chest") && !chestName.equals("Ender Chest")) || chestName.equals("LOW")))
                return;

            List<Integer> slots = Lists.newArrayList();
            for (int i = 0; i < chest.getLowerChestInventory().getSizeInventory(); i++) {
                if (chest.getLowerChestInventory().getStackInSlot(i) != null) {
                    slots.add(i);
                }
            }

            if (reverse.isEnabled()) Collections.reverse(slots);

            for (int slot : slots) {
                if (delay.getValue() == 0 || timer.hasTimeElapsed( delay.getValue().longValue(), true)) {
                    mc.playerController.windowClick(chest.windowId, slot, 0, 1, mc.thePlayer);
                }
            }

            if (slots.isEmpty() || this.isInventoryFull()) {
                mc.thePlayer.closeScreen();
            }
        }
    };

    private boolean isInventoryFull() {
        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getStack() == null) {
                return false;
            }
        }
        return true;
    }

    public static boolean shouldFreeLook() {
        if (LuckyClient.INSTANCE.isToggled(ChestStealer.class) && freeLook.isEnabled() && mc.currentScreen instanceof GuiChest) {
            ContainerChest chest = (ContainerChest) mc.thePlayer.openContainer;
            String chestName = chest.getLowerChestInventory().getName();
            return !titleCheck.isEnabled() || (chestName.contains("Chest") && !chestName.equals("Ender Chest")) || chestName.equals("LOW");
        }
        return false;
    }

}
