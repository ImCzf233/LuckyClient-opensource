package just.monika.LiteraruteMC.Lucky.utils.font;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.awt.*;
import java.io.InputStream;
import java.util.Map;

public class FontUtil {
    public static volatile int completed;
    //These are for the icon font for ease of access
    public final static String
            BUG = "a",
            LIST = "b",
            BOMB = "c",
            EYE = "d",
            PERSON = "e",
            WHEELCHAIR = "f",
            SCRIPT = "g",
            SKIP_LEFT = "h",
            PAUSE = "i",
            PLAY = "j",
            SKIP_RIGHT = "k",
            SHUFFLE = "l",
            INFO = "m",
            SETTINGS = "n",
            CHECKMARK = "o",
            XMARK = "p",
            TRASH = "q",
            WARNING = "r",
            FOLDER = "s",
            LOAD = "t",
    SAVE = "u";


    public static MinecraftFontRenderer
            tenacityFont14,
            tenacityFont16, tenacityBoldFont16,
            tenacityFont18, tenacityBoldFont18,
            tenacityFont20, tenacityBoldFont20,
            tenacityFont22, tenacityBoldFont22,
            tenacityFont24,
            tenacityBoldFont26,
            tenacityFont28,
            tenacityFont32, tenacityBoldFont32,
            tenacityFont40, tenacityBoldFont40,
            neverlose22, iconFont16, iconFont20, iconFont26, iconFont35, iconFont40, arial20;
    public static Font
            tenacityFont14_,
            tenacityFont16_, tenacityBoldFont16_,
            tenacityFont18_, tenacityBoldFont18_,
            tenacityFont20_, tenacityBoldFont20_,
            tenacityFont22_, tenacityBoldFont22_,
            tenacityFont24_,
            tenacityBoldFont26_,
            tenacityFont28_,
            tenacityFont32_, tenacityBoldFont32_,
            tenacityFont40_, tenacityBoldFont40_,
            neverlose22_, iconFont16_, iconFont20_, iconFont26_, iconFont35_, iconFont40_, arial20_;

    public static Font getFont(Map<String, Font> locationMap, String location, int size) {
        Font font;
        try {
            if (locationMap.containsKey(location)) {
                font = locationMap.get(location).deriveFont(Font.PLAIN, size);
            } else {
                InputStream is = Minecraft.getMinecraft().getResourceManager()
                        .getResource(new ResourceLocation("LuckyClient/Fonts/" + location)).getInputStream();
                locationMap.put(location, font = Font.createFont(0, is));
                font = font.deriveFont(Font.PLAIN, size);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", Font.PLAIN, +10);
        }
        return font;
    }

    public static boolean hasLoaded() {
        return completed >= 3;
    }
}
