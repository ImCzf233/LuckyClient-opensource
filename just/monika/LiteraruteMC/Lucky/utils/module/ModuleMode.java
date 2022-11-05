package just.monika.LiteraruteMC.Lucky.utils.module;

import just.monika.LiteraruteMC.Lucky.LuckyClient;
import just.monika.LiteraruteMC.Lucky.LuckyClient;
import net.minecraft.client.Minecraft;

public interface ModuleMode {

    Minecraft mc = Minecraft.getMinecraft();

    default void onEnable() {
        LuckyClient.INSTANCE.getEventProtocol().register(this);
    }

    default void onDisable() {
        LuckyClient.INSTANCE.getEventProtocol().unregister(this);
    }

}
