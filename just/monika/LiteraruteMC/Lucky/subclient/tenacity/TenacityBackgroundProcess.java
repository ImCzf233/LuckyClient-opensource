package just.monika.LiteraruteMC.Lucky.subclient.tenacity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import just.monika.LiteraruteMC.Lucky.LuckyClient;
import just.monika.LiteraruteMC.Lucky.config.ConfigManager;
import just.monika.LiteraruteMC.Lucky.config.DragManager;
import just.monika.LiteraruteMC.Lucky.event.EventListener;
import just.monika.LiteraruteMC.Lucky.event.impl.game.GameCloseEvent;
import just.monika.LiteraruteMC.Lucky.event.impl.game.KeyPressEvent;
import just.monika.LiteraruteMC.Lucky.event.impl.game.TickEvent;
import just.monika.LiteraruteMC.Lucky.event.impl.game.WorldEvent;
import just.monika.LiteraruteMC.Lucky.event.impl.player.ChatReceivedEvent;
import just.monika.LiteraruteMC.Lucky.module.Module;
import just.monika.LiteraruteMC.Lucky.ui.mainmenu.TenacityMainMenu;
import just.monika.LiteraruteMC.Lucky.utils.Utils;
import just.monika.LiteraruteMC.Lucky.module.impl.render.SessionStats;
import net.minecraft.client.gui.GuiDisconnected;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.util.StringUtils;

import java.io.File;
import java.util.Arrays;

public class TenacityBackgroundProcess implements Utils {

    private final EventListener<KeyPressEvent> onKeyPress = e ->
            LuckyClient.INSTANCE.getModuleCollection().getModules().stream()
                    .filter(m -> m.getKeybind().getCode() == e.getKey()).forEach(Module::toggle);

    private final File dragData = new File(LuckyClient.DIRECTORY, "Drag.json");
    private final Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
//.setLenient()

    private final EventListener<WorldEvent.Load> onLoad = e ->{
        ConfigManager.loadConfig();
    };
    
    private final EventListener<GameCloseEvent> onGameClose = e -> {
        LuckyClient.INSTANCE.getConfigManager().saveDefaultConfig();
        ConfigManager.saveConfig();
    };

    private final EventListener<ChatReceivedEvent> onChatReceived = e -> {
        if (mc.thePlayer == null) return;
        String message = StringUtils.stripControlCodes(e.message.getUnformattedText());
        if (!message.contains(":") && Arrays.stream(SessionStats.KILL_TRIGGERS).anyMatch(message.replace(mc.thePlayer.getName(), "*")::contains)) {
            SessionStats.killCount++;
        } else if (e.message.toString().contains("ClickEvent{action=RUN_COMMAND, value='/play ")) {
            SessionStats.gamesPlayed++;
        }
    };

    private final EventListener<TickEvent> onTick = e -> {
        if (SessionStats.endTime == -1 && ((!mc.isSingleplayer() && mc.getCurrentServerData() == null) || mc.currentScreen instanceof TenacityMainMenu || mc.currentScreen instanceof GuiMultiplayer || mc.currentScreen instanceof GuiDisconnected)) {
            SessionStats.endTime = System.currentTimeMillis();
        } else if (SessionStats.endTime != -1 && (mc.isSingleplayer() || mc.getCurrentServerData() != null)) {
            SessionStats.reset();
        }
    };

}
