package brickbot.old.quickstart.follower;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;
import org.firstinspires.ftc.robotcore.external.navigation.UnnormalizedAngleUnit;
import org.firstinspires.ftc.teamcode.common.hardware.subsystems.Updatable;

public class Localizer implements Updatable {
    private GoBildaPinpointDriver pinpoint;

    private HardwareMap hardwareMap;

    public Localizer(HardwareMap hwMap){
        hardwareMap = hwMap;
    }

    public void setOffsets(double xOffset, double yOffset, DistanceUnit distanceUnit){
        pinpoint.setOffsets(xOffset, yOffset,distanceUnit);
    }

    public void reset(){
        pinpoint.resetPosAndIMU();
    }

    public void setPosition(DistanceUnit distanceUnit, AngleUnit angleUnit, double x, double y, double heading){
        pinpoint.setPosition(new Pose2D(distanceUnit, x, y, angleUnit, heading));
    }

    public double getXVelocity(DistanceUnit distanceUnit){
        return pinpoint.getVelX(distanceUnit);
    }

    public double getYVelocity(DistanceUnit distanceUnit){
        return pinpoint.getVelY(distanceUnit);
    }

    public double getHeadingVelocity(UnnormalizedAngleUnit unnormalizedAngleUnit){
        return pinpoint.getHeadingVelocity(unnormalizedAngleUnit);
    }

    public double getXVelocity(){
        return pinpoint.getVelX(DistanceUnit.CM);
    }

    public double getYVelocity(){
        return pinpoint.getVelY(DistanceUnit.CM);
    }

    public double getHeadingVelocity(){
        return pinpoint.getHeadingVelocity(UnnormalizedAngleUnit.DEGREES);
    }

    public double getX(){
        return pinpoint.getPosX(DistanceUnit.CM);
    }

    public double getY(){
        return pinpoint.getPosY(DistanceUnit.CM);
    }

    public double getHeading(){
        return pinpoint.getHeading(AngleUnit.DEGREES);
    }

    public Pose2D getPosition(){
        return pinpoint.getPosition();
    }

    public void setEncoderDirections(GoBildaPinpointDriver.EncoderDirection directionStrafe, GoBildaPinpointDriver.EncoderDirection directionForward){
        pinpoint.setEncoderDirections(directionStrafe, directionForward);
    }

    public void setEncoderOffsets(double offsetStrafe, double offsetForward, DistanceUnit distanceUnit){
        pinpoint.setOffsets(offsetStrafe, offsetForward, distanceUnit);
    }

    public void init(){
        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");
        pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
    }

    @Override
    public void update() {
        pinpoint.update();
    }
}
