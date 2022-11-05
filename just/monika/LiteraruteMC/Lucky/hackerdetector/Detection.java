package just.monika.LiteraruteMC.Lucky.hackerdetector;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.entity.player.EntityPlayer;

@Getter
@Setter
public abstract class Detection {

    private String name;
    private Category type;
    private long lastViolated;

    public Detection(String name, Category type) {
        this.name = name;
        this.type = type;
    }

    public abstract boolean runCheck(EntityPlayer player);

	public String getName() {
		return name;
	}

	public long getLastViolated() {
		return lastViolated;
	}

	public void setLastViolated(long lastViolated) {
		this.lastViolated = lastViolated;
	}
}
