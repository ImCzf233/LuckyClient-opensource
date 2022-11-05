package just.monika.LiteraruteMC.Lucky.module.impl.render;

import just.monika.LiteraruteMC.Lucky.module.Category;
import just.monika.LiteraruteMC.Lucky.module.Module;
import just.monika.LiteraruteMC.Lucky.settings.impl.BooleanSetting;
import just.monika.LiteraruteMC.Lucky.settings.impl.NumberSetting;

public class NotificationsMod extends Module {

    public static final NumberSetting colorInterpolation = new NumberSetting("Color Value", .5, 1, 0, .05);
    public static final BooleanSetting toggleNotifications = new BooleanSetting("Toggle", true);

    public NotificationsMod() {
        super("Notifications", Category.RENDER, "Allows you to customize the client notifications");
        this.addSettings(colorInterpolation, toggleNotifications);
    }

}
