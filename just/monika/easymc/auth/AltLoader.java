package just.monika.easymc.auth;

import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;
import java.net.MalformedURLException;
import java.net.URL;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.Session;
public class AltLoader {
    public static class RedeemResponse {
        public String session;

        public String mcName;

        public String uuid;
    }
    public void setEasyMCSession(RedeemResponse response) {
        ReflectionUtil.setFieldByClass(Minecraft.getSessionInfo(), new Session(response.mcName, response.uuid, response.session, "mojang"));
        try {
            ReflectionUtil.setStaticField(YggdrasilMinecraftSessionService.class, "JOIN_URL", new URL("https://sessionserver.easymc.io/session/minecraft/join"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void setMojangSession(Session session) {
        ReflectionUtil.setFieldByClass(Minecraft.getSessionInfo(), session);
        try {
            ReflectionUtil.setStaticField(YggdrasilMinecraftSessionService.class, "JOIN_URL", new URL("https://sessionserver.mojang.com/session/minecraft/join"));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
