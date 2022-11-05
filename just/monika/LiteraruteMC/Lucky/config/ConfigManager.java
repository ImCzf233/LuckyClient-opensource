package just.monika.LiteraruteMC.Lucky.config;

import com.google.gson.*;
import com.google.gson.internal.LinkedTreeMap;
import just.monika.LiteraruteMC.Lucky.LuckyClient;
import just.monika.LiteraruteMC.Lucky.module.Category;
import just.monika.LiteraruteMC.Lucky.settings.Setting;
import just.monika.LiteraruteMC.Lucky.settings.impl.*;
import just.monika.LiteraruteMC.Lucky.ui.notifications.NotificationManager;
import just.monika.LiteraruteMC.Lucky.ui.notifications.NotificationType;
import just.monika.LiteraruteMC.Lucky.utils.misc.CloudUtils;
import just.monika.LiteraruteMC.Lucky.module.Module;
import just.monika.LiteraruteMC.Lucky.utils.objects.Drag;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ConfigManager {
    public static final List<LocalConfig> localConfigs = new ArrayList<>();
    public static final List<OnlineConfig> onlineConfigs = new ArrayList<>();
    public static boolean loadVisuals;
    public static File defaultConfig;
    public static final File file = new File(Minecraft.getMinecraft().mcDataDir, "/LuckyClient/Configs");
    private final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
    static {

        defaultConfig = new File(net.minecraft.client.Minecraft.getMinecraft().mcDataDir, "/LuckyClient/Configs/default.json");
    }
    public void collectConfigs() {
        localConfigs.clear();
        file.mkdirs();

        //For each config in the config folder it adds it to the list and removes the ".json" from the name
        Arrays.stream(Objects.requireNonNull(file.listFiles())).forEach(f -> localConfigs.add(new LocalConfig(f.getName().split("\\.")[0])));
    }

    public void collectOnlineConfigs() {
        onlineConfigs.clear();

        JsonArray jsonArray = CloudUtils.listAllData();

        if (jsonArray != null && jsonArray.size() > 0) {
            for (JsonElement jsonElement : jsonArray) {
                JsonObject data = jsonElement.getAsJsonObject();

                String configName = data.get("name").getAsString();
                String[] meta = data.get("meta").getAsString().split(":");
                String creator = meta[1];
                String description = data.get("description").getAsString();
                String clientVersion = meta[2];
                String shareCode = data.get("share_code").getAsString();

                OnlineConfig onlineConfig;
                //This has to be here for an update or two just to support the configs
                // that are already uploaded that are missing the fourth argument
                if (meta.length > 4) {
                    boolean verified = Boolean.parseBoolean(meta[3]);
                    int votes = Integer.parseInt(meta[4]);
                    onlineConfig = new OnlineConfig(configName, creator, description, clientVersion, shareCode, verified, votes);
                } else {
                    onlineConfig = new OnlineConfig(configName, creator, description, clientVersion, shareCode, false, 0);
                }

                onlineConfigs.add(onlineConfig);

            }
        } else {
            NotificationManager.post(NotificationType.WARNING, "Error", "Failed to reach intent servers");
        }
    }


    /**
     * Saving config method
     *
     * @see ConfigManager#serialize() to serialize the modules and settings
     */
    public boolean saveConfig(String name) {
        LocalConfig localConfig = new LocalConfig(name);
        localConfig.getFile().getParentFile().mkdirs();
        try {
            Files.write(localConfig.getFile().toPath(), serialize().getBytes(StandardCharsets.UTF_8));
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(String configName) {
        List<Configuration> configsMatch = localConfigs.stream().filter(localConfig -> localConfig.getName().equals(configName)).collect(Collectors.toList());
        try {
            LocalConfig configToDelete = (LocalConfig) configsMatch.get(0);
            Files.deleteIfExists(configToDelete.getFile().toPath());
        } catch (IOException | IndexOutOfBoundsException e) {
            e.printStackTrace();
            NotificationManager.post(NotificationType.WARNING, "Config Manager", "Failed to delete config!");
            return false;
        }
        return true;
    }

    public void saveDefaultConfig() {
        defaultConfig.getParentFile().mkdirs();
        try {
            Files.write(defaultConfig.toPath(), serialize().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to save " + defaultConfig);
        }
    }

    public String serialize() {
        for (Module module : LuckyClient.INSTANCE.getModuleCollection().getModules()) {
            List<ConfigSetting> settings = new ArrayList<>();
            for (Setting setting : module.getSettingsList()) {
                ConfigSetting cfgSetting = new ConfigSetting(null, null);
                cfgSetting.name = setting.name;
                cfgSetting.value = setting.getConfigValue();
                settings.add(cfgSetting);

            }
            module.cfgSettings = settings.toArray(new ConfigSetting[0]);
        }
        return gson.toJson(LuckyClient.INSTANCE.getModuleCollection().getModules());
    }

    public String readConfigData(Path configPath) {
        try {
            return new String(Files.readAllBytes(configPath));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
    public static boolean saveConfig(){
        DragManager.saveDragData();
        return LuckyClient.INSTANCE.getConfigManager().saveConfig("justmonika");
    }


    public static boolean loadConfig()  {
        DragManager.loadDragData();
        try {
            FileInputStream fis=new FileInputStream(new File(file,"justmonika.json"));
            byte b[]=new byte[fis.available()];
            fis.read(b);
            return LuckyClient.INSTANCE.getConfigManager().loadConfig(new String(b));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean loadConfig(String data) {
        return loadConfig(data, true);
    }

    public boolean loadConfig(String data, boolean keybinds) {
        Module[] modules = gson.fromJson(data, Module[].class);

        for (Module module : LuckyClient.INSTANCE.getModuleCollection().getModules()) {
            if (!keybinds && !loadVisuals && module.getCategory().equals(Category.RENDER)) continue;

            for (Module configModule : modules) {
                if (module.getName().equals(configModule.getName())) {
                    try {
                        if (module.isToggled() != configModule.isToggled()) {
                            module.toggleSilent();
                        }
                        for (Setting setting : module.getSettingsList()) {
                            for (ConfigSetting cfgSetting : configModule.cfgSettings) {
                                if (setting.name.equals(cfgSetting.name)) {
                                    if (setting instanceof KeybindSetting) {
                                        if (keybinds) {
                                            KeybindSetting keybindSetting = (KeybindSetting) setting;
                                            keybindSetting.setCode(Double.valueOf(String.valueOf(cfgSetting.value)).intValue());
                                        }
                                    }
                                    if (setting instanceof BooleanSetting) {
                                        ((BooleanSetting) setting).setState(Boolean.parseBoolean(String.valueOf(cfgSetting.value)));
                                    }
                                    if (setting instanceof ModeSetting) {
                                        String value = String.valueOf(cfgSetting.value);
                                        ModeSetting ms = (ModeSetting) setting;
                                        if (ms.modes.contains(value)) {
                                            ms.setCurrentMode(value);
                                        } else {
                                            ms.setCurrentMode(ms.modes.get(0));
                                            System.out.printf("The value of setting %s in module %s was reset%n", ms.name, module.getName());
                                        }
                                    }
                                    if (setting instanceof NumberSetting) {
                                        NumberSetting ss = (NumberSetting) setting;
                                        double value;
                                        try {
                                            value = Double.parseDouble(String.valueOf(cfgSetting.value));
                                        } catch (NumberFormatException e) {
                                            value = ss.getDefaultValue();
                                            System.out.printf("The value of setting %s in module %s was reset%n", ss.name, module.getName());
                                        }
                                        ss.setValue(value);
                                    }
                                    if (setting instanceof MultipleBoolSetting) {
                                        LinkedTreeMap<String, Boolean> boolMap = (LinkedTreeMap<String, Boolean>) cfgSetting.value;
                                        MultipleBoolSetting mbs = (MultipleBoolSetting) setting;
                                        for (String s : boolMap.keySet()) {
                                            BooleanSetting childSetting = mbs.getSetting(s);
                                            if (childSetting != null && boolMap.get(s) != null) {
                                                childSetting.setState(boolMap.get(s));
                                            }
                                        }
                                    }
                                    if (setting instanceof ColorSetting) {
                                        int color = Double.valueOf(String.valueOf(cfgSetting.value)).intValue();
                                        Color c = new Color(color);
                                        float[] hsb = Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), null);
                                        ((ColorSetting) setting).setColor(hsb[0], hsb[1], hsb[2]);
                                    }
                                    if (setting instanceof StringSetting) {
                                        String value = String.valueOf(cfgSetting.value);
                                        if (value != null) {
                                            ((StringSetting) setting).setString(value);
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return true;
    }
}
