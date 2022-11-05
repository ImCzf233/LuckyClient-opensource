package just.monika.LiteraruteMC.Lucky.module;

import just.monika.LiteraruteMC.Lucky.utils.font.FontUtil;
import just.monika.LiteraruteMC.Lucky.utils.objects.Drag;
import just.monika.LiteraruteMC.Lucky.utils.objects.Scroll;
import lombok.Getter;

public enum Category {

    COMBAT("Combat", FontUtil.BOMB),
    MOVEMENT("Movement", FontUtil.WHEELCHAIR),
    RENDER("Render", FontUtil.EYE),
    PLAYER("Player", FontUtil.PERSON),
    EXPLOIT("Exploit", FontUtil.BUG),
    MISC("Misc", FontUtil.LIST);

    public final String name;
    public final String icon;
    public final int posX;
    public final boolean expanded;

    @Getter
    private final Scroll scroll = new Scroll();

    @Getter
    private final Drag drag;
    public int posY = 20;

    Category(String name, String icon) {
        this.name = name;
        this.icon = icon;
        posX = 40 + (Module.categoryCount * 120);
        drag = new Drag(posX, posY);
        expanded = true;
        Module.categoryCount++;
    }

	public Drag getDrag() {
		return drag;
	}

	public Scroll getScroll() {
		return scroll;
	}
}
