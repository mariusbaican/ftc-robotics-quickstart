package org.firstinspires.ftc.teamcode.common.hardware.subsystems;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.common.hardware.RobotHardware;

public class FieldCentricV2 implements Subsystem {

    RobotHardware robot = RobotHardware.getInstance();
    private double x;
    private double y;
    private double rotX;
    private double rotY;
    private double turn;
    private double lastTurn;
    private double headingOffset = Math.PI; //TODO GRAB FROM AUTONOMOUS
    private double botHeading = 0;
    private double targetHeading;
    private double brake = 1.0;
    public FieldCentricV2() {}

    @Override
    public void init(HardwareMap hwMap) {
        robot.frontLeft.init(hwMap);
        robot.rearLeft.init(hwMap);
        robot.rearRight.init(hwMap);
        robot.frontRight.init(hwMap);
        robot.frontLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.rearLeft.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.rearRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.frontRight.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        robot.frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.rearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.rearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        robot.frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        robot.rearLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        robot.imu.resetYaw();
    }
    @Override
    public void read() {
        botHeading = robot.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS) + headingOffset;
        x = robot.gamepad1.left_stick_x;
        y = -robot.gamepad1.left_stick_y;
        turn =  (robot.gamepad1.right_stick_x);
        if(robot.slides.getExtensionCm() > 35)
            brake = 0.4;
        else if (robot.gamepad1.right_trigger > 0 || robot.slides.getExtensionCm() > 20)
            brake = 0.5;
        else
            brake = 1.0;
        if (robot.gamepad1.options) {
            robot.imu.resetYaw();
        }
    }
    public void write() {
        double voltageCorrection = 12.0 / robot.voltage;
        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(turn), 1);
        double frontLeftPower = (rotY + rotX + turn) * voltageCorrection/ denominator;
        double backLeftPower = (rotY - rotX + turn) * voltageCorrection/ denominator;
        double frontRightPower = (rotY - rotX - turn) * voltageCorrection/ denominator;
        double backRightPower = (rotY + rotX - turn) * voltageCorrection/ denominator;

        if(!robot.agatat) {
            robot.frontLeft.setPower(frontLeftPower * brake);
            robot.rearLeft.setPower(backLeftPower * brake);
            robot.rearRight.setPower(backRightPower * brake);
            robot.frontRight.setPower(frontRightPower * brake);
        }
        else if(robot.agatat)
        {
            if(robot.gamepad2.cross)
            {
                robot.ridicat = true;
                robot.frontLeft.setPower(0.7);
                robot.frontRight.setPower(0.7);
                robot.rearLeft.setPower(0);
                robot.rearRight.setPower(0);
            }
            else if(robot.ridicat){
                robot.frontLeft.setPower(0.3);
                robot.frontRight.setPower(0.3);
                robot.rearLeft.setPower(0);
                robot.rearRight.setPower(0);
            }
        }
    }
    @Override
    public void periodic() {

        // Rotate the movement direction counter to the bot's rotation
        rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
        rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);
        rotX = rotX * 1.1;
    }
}


