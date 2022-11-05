package just.monika.LiteraruteMC.Lucky.hackerdetector.checks;

import just.monika.LiteraruteMC.Lucky.hackerdetector.Category;
import just.monika.LiteraruteMC.Lucky.hackerdetector.Detection;
import just.monika.LiteraruteMC.Lucky.hackerdetector.utils.MovementUtils;
import net.minecraft.entity.player.EntityPlayer;

public class FlightA extends Detection {

    public FlightA() {
        super("Flight A", Category.MOVEMENT);
    }

    @Override
    public boolean runCheck(EntityPlayer player) {
        return !player.onGround && player.motionY == 0 && MovementUtils.isMoving(player);
    }
}
