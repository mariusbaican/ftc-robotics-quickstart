package brickbot.old.quickstart.follower;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

import brickbot.old.quickstart.controltheory.PDFSController;
import brickbot.old.quickstart.hardware.subsystems.Updatable;
import brickbot.quickstart.devices.BrickMotor;

public class Follower implements Updatable {
    private Pose targetPose;
    private HardwareMap hardwareMap;
    private Localizer localizer;

    public static double yP, yD, yF, yS;
    public static double xP, xD, xF, xS;
    public static double headingP, headingD, headingF, headingS;

    private DcMotorSimple.Direction flDirection, frDirection, rlDirection, rrDirection;

    PDFSController xPDFS = new PDFSController(0,0,0,0);
    PDFSController yPDFS = new PDFSController(0,0,0,0);
    PDFSController headingPDFS = new PDFSController(0,0,0,0);

    private BrickMotor frontLeft, frontRight, rearLeft, rearRight;

    public Follower(HardwareMap hwMap){
        hardwareMap = hwMap;
    }

    public void setEncoderDirections(GoBildaPinpointDriver.EncoderDirection directionStrafe, GoBildaPinpointDriver.EncoderDirection directionForward){
        localizer.setEncoderDirections(directionStrafe, directionForward);
    }

    public void setEncoderOffsets(double offsetStrafe, double offsetForward, DistanceUnit distanceUnit){
        localizer.setEncoderOffsets(offsetStrafe, offsetForward, distanceUnit);
    }

    public void setMotorNames(String frontLeftName, String frontRightName, String rearLeftName, String rearRightName){
        frontLeft = new BrickMotor(frontLeftName);
        frontRight = new BrickMotor(frontRightName);
        rearLeft = new BrickMotor(rearLeftName);
        rearRight = new BrickMotor(rearRightName);
    }


    public void setMotorDirections(DcMotorSimple.Direction frontLeftDirection, DcMotorSimple.Direction frontRightDirection, DcMotorSimple.Direction rearLeftDirection, DcMotorSimple.Direction rearRightDirection){
        flDirection = frontLeftDirection;
        frDirection = frontRightDirection;
        rlDirection = rearLeftDirection;
        rrDirection = rearRightDirection;
    }

    public void setTargetPose(Pose pose){
        targetPose = pose;
    }

    public void setYConstants(double yP, double yD, double yF, double yS){
        this.yP = yP;
        this.yD = yD;
        this.yF = yF;
        this.yS = yS;
    }

    public void setXConstants(double xP, double xD, double xF, double xS){
        this.xP = xP;
        this.xD = xD;
        this.xF = xF;
        this.xS = xS;
    }

    public void setHeadingConstants(double headingP, double headingD, double headingF, double headingS){
        this.headingP = headingP;
        this.headingD = headingD;
        this.headingF = headingF;
        this.headingS = headingS;
    }


    public void init(){
        localizer = new Localizer(hardwareMap);
        localizer.init();

        frontLeft.init(hardwareMap);
        frontRight.init(hardwareMap);
        rearLeft.init(hardwareMap);
        rearRight.init(hardwareMap);

        frontLeft.setDirection(flDirection);
        frontRight.setDirection(frDirection);
        rearLeft.setDirection(rlDirection);
        rearRight.setDirection(rrDirection);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rearLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rearRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }


    public void update(){
        yPDFS.setConstants(yP, yD, yF, yS);
        xPDFS.setConstants(xP, xD, xF, xS);
        headingPDFS.setConstants(headingP, headingD, headingF, headingS);

        double posX = localizer.getX();
        double posY = localizer.getY();
        double botHeading = localizer.getHeading();

        double y = yPDFS.calculate(posY, targetPose.getY());
        double x = xPDFS.calculate(posX, targetPose.getX());
        double rx = headingPDFS.calculate(localizer.getHeading(), targetPose.getHeading());

        double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
        double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);
        double frontLeftPower = (rotY + rotX + rx) / denominator;
        double backLeftPower = (rotY - rotX + rx) / denominator;
        double frontRightPower = (rotY - rotX - rx) / denominator;
        double backRightPower = (rotY + rotX - rx) / denominator;

        frontLeft.setPower(frontLeftPower);
        rearLeft.setPower(backLeftPower);
        frontRight.setPower(frontRightPower);
        rearRight.setPower(backRightPower);
    }
}
