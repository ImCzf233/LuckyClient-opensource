package just.monika.LiteraruteMC.Lucky.hackerdetector.checks;

import just.monika.LiteraruteMC.Lucky.hackerdetector.Category;
import just.monika.LiteraruteMC.Lucky.hackerdetector.Detection;
import net.minecraft.entity.player.EntityPlayer;

public class ReachA extends Detection {

    public ReachA() {
        super("Reach A", Category.COMBAT);
    }

    @Override
    public boolean runCheck(EntityPlayer player) {
        return false;
    }
}
