package just.monika.LiteraruteMC.Lucky.module.impl.player;

import just.monika.LiteraruteMC.Lucky.event.EventListener;
import just.monika.LiteraruteMC.Lucky.event.impl.player.SafeWalkEvent;
import just.monika.LiteraruteMC.Lucky.module.Category;
import just.monika.LiteraruteMC.Lucky.module.Module;
import just.monika.LiteraruteMC.Lucky.settings.impl.BooleanSetting;
import net.minecraft.item.ItemBlock;

public final class SafeWalk extends Module {

    private final BooleanSetting blocksOnly = new BooleanSetting("Blocks only", true);

    private final EventListener<SafeWalkEvent> onSafeWalk = e -> {
        if (canSafeWalk()) {
            e.setSafe(true);
        }
    };

    private boolean canSafeWalk() {
        if (!blocksOnly.isEnabled()) return true;
        return mc.thePlayer != null && mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemBlock;
    }

    public SafeWalk() {
        super("SafeWalk", Category.PLAYER, "prevents walking off blocks");
        this.addSettings(blocksOnly);
    }

}
