package just.monika.LiteraruteMC.Lucky.module.impl.render;

import just.monika.LiteraruteMC.Lucky.LuckyClient;
import just.monika.LiteraruteMC.Lucky.module.Category;
import just.monika.LiteraruteMC.Lucky.module.Module;
import just.monika.LiteraruteMC.Lucky.event.EventListener;
import just.monika.LiteraruteMC.Lucky.event.impl.game.KeyPressEvent;
import just.monika.LiteraruteMC.Lucky.event.impl.render.Render2DEvent;
import just.monika.LiteraruteMC.Lucky.utils.font.FontUtil;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class TabGUI extends Module {
    private final ArrayList<Tab> tabs = new ArrayList<>();
    private int selectTab = 1;

    private boolean sub = false;

    private int selectSub = 0;
    public TabGUI(){
        super("TabGUI", Category.RENDER,"");
        tabs.add(new Tab(""));

        for (Category type : Category.values()) {
            Tab tab = new Tab(type.name());

            LuckyClient.INSTANCE.getModuleCollection().getModulesInCategory(type).
                    forEach(it -> tab.sub.add(new Tab(it.getName())));

            tabs.add(tab);
        }
        toggleSilent();
    }
    private final EventListener<KeyPressEvent> onKey = (e) -> {
        switch (e.getKey()) {
            case Keyboard.KEY_UP:
                up();
                break;
            case Keyboard.KEY_DOWN:
                down();
                break;
            case Keyboard.KEY_LEFT:
                left();
                break;
            case Keyboard.KEY_RIGHT:
                right();
                break;
            case Keyboard.KEY_RETURN:
                enter();
                break;
        }
    };
    private final EventListener<Render2DEvent> onRender = (e) -> {
        int typeY = 10;
        int indexY = 0;
        int tabX = 5;
        for (Tab tab : tabs) {
            String s0 = tab.name;
            if(indexY == selectTab){
                s0="  "+s0;
            }
            FontUtil.draw(s0, 5, typeY, 0xFFFFFF);

            if (indexY == selectTab) {

                if (sub) {
                    int moduleY = typeY;
                    int moduleIndex = 0;
                    for (Tab subTab : tab.sub) {
                        String s = subTab.name;
                        int color=0xFFFFFF;


                        if (moduleIndex == selectSub) {
                            s="  "+s;
                            //drawRect(tabX + getMaxType(), moduleY, getMaxModule(), mc.fontRendererObj.FONT_HEIGHT, new Color(20, 100, 190, 150).getRGB());
                        }
                        FontUtil.draw(s, tabX + getMaxType(), moduleY, color);

                        moduleIndex++;
                        moduleY += just.monika.LiteraruteMC.Lucky.utils.font.FontUtil.tenacityFont20.getHeight();
                    }
                }

            }

            indexY++;
            typeY += just.monika.LiteraruteMC.Lucky.utils.font.FontUtil.tenacityFont20.getHeight();
        }
    };
    private void up() {
        if (!sub) {
            if (selectTab > 1) {
                selectTab--;
            } else {
                selectTab = Category.values().length;
            }
        } else {
            if (selectSub > 0) {
                selectSub--;
            } else {
                selectSub = LuckyClient.INSTANCE.getModuleCollection().getModulesInCategory(Category.values()[selectTab]).size() - 1;
            }
        }
    }

    private void down() {
        if (!sub) {
            if (selectTab < Category.values().length) {
                selectTab++;
            } else {
                selectTab = 1;
            }
        } else {
            if (selectSub < LuckyClient.INSTANCE.getModuleCollection().getModulesInCategory(Category.values()[selectTab]).size() - 1) {
                selectSub++;
            } else {
                selectSub = 0;
            }
        }
    }

    private void left() {
        sub = false;
    }

    private void right() {
        sub = true;
        selectSub = 0;
    }

    private void enter() {
        if (sub) {
            LuckyClient.INSTANCE.getModuleCollection().getModulesInCategory(Category.values()[selectTab-1]).get(selectSub).toggle();
        }
    }
    public static int getMaxModule() {
        List<Module> modules = LuckyClient.INSTANCE.getModuleCollection().getModules();
        modules.sort((o1, o2) -> FontUtil.getWidth(o2.getName()) - FontUtil.getWidth(o1.getName()));
        return FontUtil.getWidth(modules.get(0).getName());
    }

    public static int getMaxType() {
        List<Category> collect = Arrays.stream(Category.values()).
                sorted((o1, o2) -> FontUtil.getWidth(o2.name())
                        - FontUtil.getWidth(o1.name())).collect(Collectors.toList());
        return FontUtil.getWidth(collect.get(0).name());
    }
    private static class Tab {
        private final String name;

        private final ArrayList<Tab> sub = new ArrayList<>();

        private Tab(String name) {
            this.name = name;
        }
    }
    public static class FontUtil {
        public static int draw(String text, int x, int y, int color) {
            return just.monika.LiteraruteMC.Lucky.utils.font.FontUtil.tenacityFont20.drawString(text, x, y, color);
        }

        public static int getWidth(String text) {
            return (int) just.monika.LiteraruteMC.Lucky.utils.font.FontUtil.tenacityFont20.getStringWidth(text);
        }

        public static int getHeight() {
            return just.monika.LiteraruteMC.Lucky.utils.font.FontUtil.tenacityFont20.getHeight();
        }
    }
}
