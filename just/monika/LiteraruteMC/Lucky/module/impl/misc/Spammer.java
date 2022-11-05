package just.monika.LiteraruteMC.Lucky.module.impl.misc;

import just.monika.LiteraruteMC.Lucky.event.EventListener;
import just.monika.LiteraruteMC.Lucky.event.impl.player.MotionEvent;
import just.monika.LiteraruteMC.Lucky.module.Category;
import just.monika.LiteraruteMC.Lucky.settings.impl.BooleanSetting;
import just.monika.LiteraruteMC.Lucky.settings.impl.MultipleBoolSetting;
import just.monika.LiteraruteMC.Lucky.settings.impl.NumberSetting;
import just.monika.LiteraruteMC.Lucky.settings.impl.StringSetting;
import just.monika.LiteraruteMC.Lucky.utils.misc.MathUtils;
import just.monika.LiteraruteMC.Lucky.utils.time.TimerUtil;
import just.monika.LiteraruteMC.Lucky.module.Module;

public final class Spammer extends Module {
    private final StringSetting text = new StringSetting("Text");
    private final NumberSetting delay = new NumberSetting("Delay", 100, 1000, 10, 1);
    private final MultipleBoolSetting settings = new MultipleBoolSetting("Settings",
            new BooleanSetting("AntiSpam", false),
            new BooleanSetting("Bypass", false));
    private final TimerUtil delayTimer = new TimerUtil();
    private final EventListener<MotionEvent> motionEventEventListener = event -> {
        String spammerText = text.getString();

        if (spammerText != null && delayTimer.hasTimeElapsed(settings.getSetting("Bypass").isEnabled() ? 2000 : delay.getValue().longValue())) {

            if (settings.getSetting("AntiSpam").isEnabled()) {
                spammerText += " " + MathUtils.getRandomInRange(10, 100000);
            }

            mc.thePlayer.sendChatMessage(spammerText);
            delayTimer.reset();
        }
    };

    public Spammer() {
        super("Spammer", Category.MISC, "Spam the chat with a custom message");
        this.addSettings(text, delay, settings);
    }
}
