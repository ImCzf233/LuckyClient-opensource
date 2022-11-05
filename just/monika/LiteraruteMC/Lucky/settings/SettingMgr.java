package just.monika.LiteraruteMC.Lucky.settings;

import just.monika.LiteraruteMC.Lucky.module.Module;

import java.util.List;

public class SettingMgr {
    public static SettingMgr mgr=new SettingMgr();
    public List<Setting> getSettingsByMod(Module module){
        return module.getSettingsList();
    }
}
