package just.monika.LiteraruteMC.Lucky.module.impl.player;

import just.monika.LiteraruteMC.Lucky.event.EventListener;
import just.monika.LiteraruteMC.Lucky.event.impl.game.KeyPressEvent;
import just.monika.LiteraruteMC.Lucky.module.Category;
import just.monika.LiteraruteMC.Lucky.module.Module;
import just.monika.LiteraruteMC.Lucky.settings.impl.NumberSetting;
import org.lwjgl.input.Keyboard;

import java.util.Random;

import static just.monika.LiteraruteMC.Lucky.utils.toolkit.MonikaIQBoost.LMessages;
import static org.lwjgl.input.Keyboard.KEY_P;

public final class IQBoost extends Module {

    private final EventListener<KeyPressEvent> motionEventEventListener = event -> {
        //this.setSuffix(this.getSettingsList().get(0).getConfigValue().toString());
        if (Keyboard.isKeyDown(KEY_P)) {
            Random r = new Random();
            mc.thePlayer.sendChatMessage(LMessages[r.nextInt(LMessages.length)]);
        }
    };

    public IQBoost() {
        super("IQBoost", Category.PLAYER, "press P to L");
        this.addSettings(new NumberSetting("IQ", 120, Integer.MAX_VALUE, 0, 10));
    }
}
