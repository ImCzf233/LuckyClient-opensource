package just.monika.LiteraruteMC.Lucky.hackerdetector.checks;

import just.monika.LiteraruteMC.Lucky.hackerdetector.Category;
import just.monika.LiteraruteMC.Lucky.hackerdetector.Detection;
import just.monika.LiteraruteMC.Lucky.hackerdetector.utils.MovementUtils;
import net.minecraft.entity.player.EntityPlayer;

public class FlightB extends Detection {

    public FlightB() {
        super("Flight B", Category.MOVEMENT);
    }

    @Override
    public boolean runCheck(EntityPlayer player) {
        return player.airTicks > 20 && player.motionY == 0 && MovementUtils.isMoving(player);
    }
}
