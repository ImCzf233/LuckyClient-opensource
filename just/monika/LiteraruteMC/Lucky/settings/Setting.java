package just.monika.LiteraruteMC.Lucky.settings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public abstract class Setting {

    @Expose
    @SerializedName("name")
    public String name;
    private List<ParentAttribute<? extends Setting>> parents = new ArrayList<>();

    public boolean hasParent() {
        return !parents.isEmpty();
    }

    public List<ParentAttribute<? extends Setting>> getParents() {
        return parents;
    }

    public void setParents(List<ParentAttribute<? extends Setting>> parents) {
        this.parents = parents;
    }

    public void addParent(ParentAttribute<? extends Setting> parent) {
        parents.add(parent);
    }

    public <T extends Setting> void addParent(T parent, Predicate<T> condition) {
        addParent(new ParentAttribute<>(parent, condition));
    }

    public static <T extends Setting> void addParent(T parent, Predicate<T> condition, Setting... settings) {
        Arrays.asList(settings).forEach(s -> s.addParent(new ParentAttribute<>(parent, condition)));
    }

    public boolean shouldBeShownTenacityClickgui() {
        if (!hasParent()) return false;
        return getParents().stream().noneMatch(ParentAttribute::isValid);
    }

    public abstract <T> T getConfigValue();


}
