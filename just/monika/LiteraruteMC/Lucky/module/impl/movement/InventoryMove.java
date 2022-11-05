package just.monika.LiteraruteMC.Lucky.module.impl.movement;

import just.monika.LiteraruteMC.Lucky.event.EventListener;
import just.monika.LiteraruteMC.Lucky.event.impl.network.PacketReceiveEvent;
import just.monika.LiteraruteMC.Lucky.event.impl.player.MotionEvent;
import just.monika.LiteraruteMC.Lucky.module.Category;
import just.monika.LiteraruteMC.Lucky.module.Module;
import just.monika.LiteraruteMC.Lucky.settings.impl.ModeSetting;
import just.monika.LiteraruteMC.Lucky.utils.time.TimerUtil;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraft.network.play.server.S2EPacketCloseWindow;

import java.util.Arrays;
import java.util.List;

public final class InventoryMove extends Module {
    private final TimerUtil delayTimer = new TimerUtil();
    private final ModeSetting mode = new ModeSetting("Mode", "Vanilla", "Vanilla", "Spoof", "Delay");
    private static final List<KeyBinding> keys = Arrays.asList(
            mc.gameSettings.keyBindForward,
            mc.gameSettings.keyBindBack,
            mc.gameSettings.keyBindLeft,
            mc.gameSettings.keyBindRight,
            mc.gameSettings.keyBindJump
    );

    public static void updateStates() {
        if (mc.currentScreen != null) {
            keys.forEach(k -> k.pressed = GameSettings.isKeyDown(k));
        }
    }

    private final EventListener<MotionEvent> motionEventEventListener = e -> {
        switch (mode.getMode()) {
            case "Spoof":
            case "Vanilla":
                if (e.isPre() && mc.currentScreen instanceof GuiContainer) {
                    updateStates();
                }
                break;
            case "Delay":
                if (e.isPre() && mc.currentScreen instanceof GuiContainer) {
                    if (delayTimer.hasTimeElapsed(100)) {
                        updateStates();
                        delayTimer.reset();
                    }
                }
                break;
        }
    };

    private final EventListener<PacketReceiveEvent> packetReceiveEventEventListener = e -> {
        if (mode.is("Spoof")) {
            if (e.getPacket() instanceof S2DPacketOpenWindow) {
                e.cancel();
            }
            if (e.getPacket() instanceof S2EPacketCloseWindow) {
                e.cancel();
            }
        }
    };

    public InventoryMove() {
        super("InventoryMove", Category.MOVEMENT, "lets you move in your inventory");
        this.addSettings(mode);
    }
}
