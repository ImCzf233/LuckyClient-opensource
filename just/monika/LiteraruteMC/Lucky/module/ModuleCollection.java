package just.monika.LiteraruteMC.Lucky.module;

import just.monika.LiteraruteMC.Lucky.module.impl.combat.*;
import just.monika.LiteraruteMC.Lucky.module.impl.exploit.*;
import just.monika.LiteraruteMC.Lucky.module.impl.misc.*;
import just.monika.LiteraruteMC.Lucky.module.impl.movement.*;
import just.monika.LiteraruteMC.Lucky.module.impl.player.*;
import just.monika.LiteraruteMC.Lucky.module.impl.render.*;
import just.monika.LiteraruteMC.Lucky.utils.toolkit.ClassUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ModuleCollection {

    public static boolean reloadModules;

    private HashMap<Class<? extends Module>, Module> modules = new HashMap<>();
    private final List<Class<? extends Module>> hiddenModules = new ArrayList<>(Arrays.asList(ArraylistMod.class, NotificationsMod.class));

    public List<Class<? extends Module>> getHiddenModules() {
        return hiddenModules;
    }

    public List<Module> getModules() {
        return new ArrayList<>(this.modules.values());
    }

    public void setModules(HashMap<Class<? extends Module>, Module> modules) {
        this.modules = modules;
    }

    public List<Module> getModulesInCategory(Category c) {
        return this.modules.values().stream().filter(m -> m.getCategory() == c).collect(Collectors.toList());
    }

    public Module get(Class<? extends Module> mod) {
        return this.modules.get(mod);
    }

    public Module getModuleByName(String name) {
        return this.modules.values().stream().filter(m -> m.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public List<Module> getModulesContains(String text) {
        return this.modules.values().stream().filter(m -> m.getName().toLowerCase().contains(text.toLowerCase())).collect(Collectors.toList());
    }

    public final List<Module> getToggledModules() {
        return this.modules.values().stream().filter(Module::isToggled).collect(Collectors.toList());
    }

    public void init() {
            this.modules.put(ServerSwitcher.class,new ServerSwitcher());
            this.modules.put(ServerFucker.class,new ServerFucker());
            this.modules.put(LegitAura.class,new LegitAura());
            this.modules.put(IQBoost.class,new IQBoost());
            this.modules.put(FixInp.class,new FixInp());
            this.modules.put(AutoHead.class, new AutoHead());
            this.modules.put(AutoPot.class, new AutoPot());
            this.modules.put(Criticals.class, new Criticals());
            this.modules.put(Fastbow.class, new Fastbow());
            this.modules.put(KillAura.class, new KillAura());
            this.modules.put(TargetStrafe.class, new TargetStrafe());
            this.modules.put(Velocity.class, new Velocity());
            this.modules.put(AntiAura.class, new AntiAura());
            this.modules.put(AntiInvis.class, new AntiInvis());
            this.modules.put(Disabler.class, new Disabler());
            this.modules.put(Regen.class, new Regen());
            this.modules.put(ResetVL.class, new ResetVL());
            this.modules.put(AntiDesync.class, new AntiDesync());
            this.modules.put(AntiFreeze.class, new AntiFreeze());
            this.modules.put(AntiTabComplete.class, new AntiTabComplete());
            this.modules.put(AutoHypixel.class, new AutoHypixel());
            this.modules.put(AutoRespawn.class, new AutoRespawn());
            this.modules.put(HackerDetector.class, new HackerDetector());
            this.modules.put(LightningTracker.class, new LightningTracker());
            this.modules.put(NoRotate.class, new NoRotate());
            this.modules.put(Spammer.class, new Spammer());
            this.modules.put(FastLadder.class, new FastLadder());
            this.modules.put(Flight.class, new Flight());
            this.modules.put(InventoryMove.class, new InventoryMove());
            this.modules.put(LongJump.class, new LongJump());
            this.modules.put(Scaffold.class, new Scaffold());
            this.modules.put(Speed.class, new Speed());
            this.modules.put(Sprint.class, new Sprint());
            this.modules.put(Step.class, new Step());
            this.modules.put(AntiVoid.class, new AntiVoid());
            this.modules.put(AutoArmor.class, new AutoArmor());
            this.modules.put(Blink.class, new Blink());
            this.modules.put(ChestStealer.class, new ChestStealer());
            this.modules.put(FastPlace.class, new FastPlace());
            this.modules.put(Freecam.class, new Freecam());
            this.modules.put(InvManager.class, new InvManager());
            this.modules.put(NoFall.class, new NoFall());
            this.modules.put(NoSlow.class, new NoSlow());
            this.modules.put(SafeWalk.class, new SafeWalk());
            this.modules.put(SpeedMine.class, new SpeedMine());
            this.modules.put(Ambience.class, new Ambience());
            this.modules.put(Animations.class, new Animations());
            this.modules.put(ArraylistMod.class, new ArraylistMod());
            this.modules.put(BlurModule.class, new BlurModule());
            this.modules.put(Brightness.class, new Brightness());
            this.modules.put(ChinaHat.class, new ChinaHat());
            this.modules.put(ClickGuiMod.class, new ClickGuiMod());
            this.modules.put(ESP2D.class, new ESP2D());
            this.modules.put(GlowESP.class, new GlowESP());
            this.modules.put(HudMod.class, new HudMod());
            this.modules.put(NotificationsMod.class, new NotificationsMod());
            this.modules.put(Radar.class, new Radar());
            this.modules.put(ScoreboardMod.class, new ScoreboardMod());
            this.modules.put(SessionStats.class, new SessionStats());
            this.modules.put(TabGUI.class,new TabGUI());
            this.modules.put(Damage.class,new Damage());
            this.modules.put(Kick.class,new Kick());
            this.modules.put(XCarry.class,new XCarry());
            this.modules.put(KillAuraModule.class,new KillAuraModule());
     }


}
