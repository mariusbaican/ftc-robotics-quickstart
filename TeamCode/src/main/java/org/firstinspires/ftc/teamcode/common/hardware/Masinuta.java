package org.firstinspires.ftc.teamcode.common.hardware;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.pedropathing.localization.GoBildaPinpointDriver;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Supplier;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.VoltageUnit;
import org.firstinspires.ftc.teamcode.common.commandbase.CommandScheduler;
import  org.firstinspires.ftc.teamcode.common.hardware.devicewrappers.BetterEncoder;
import  org.firstinspires.ftc.teamcode.common.hardware.devicewrappers.BetterMotor;
import  org.firstinspires.ftc.teamcode.common.hardware.devicewrappers.BetterServo;
import  org.firstinspires.ftc.teamcode.common.hardware.subsystems.Arm;
import  org.firstinspires.ftc.teamcode.common.hardware.subsystems.Camera;
import  org.firstinspires.ftc.teamcode.common.hardware.subsystems.Claw;
import  org.firstinspires.ftc.teamcode.common.hardware.subsystems.FieldCentricDrive;
import org.firstinspires.ftc.teamcode.common.hardware.subsystems.FieldCentricV2;
import  org.firstinspires.ftc.teamcode.common.hardware.subsystems.Pivot;
import  org.firstinspires.ftc.teamcode.common.hardware.subsystems.Pto;
import  org.firstinspires.ftc.teamcode.common.hardware.subsystems.Slides;
import  org.firstinspires.ftc.teamcode.common.hardware.subsystems.Subsystem;


import java.util.ArrayList;

public class Masinuta
{
    public static Masinuta masinuta = null;

    private static boolean resetPinpoint = true;

    public double voltage = 12.0;

    // Declare Devices
    LynxModule controlHub;

    public Gamepad gamepad1;
    public Gamepad lastGamepad1;
    public Gamepad gamepad2;
    public Gamepad lastGamepad2;

    public BetterMotor rearLeft = new BetterMotor("rearLeft");
    public BetterMotor rearRight = new BetterMotor("rearRight");




    public BetterServo steer = new BetterServo("steer");



    public Masinuta init(HardwareMap hwMap)
    {
        //camera = new Camera();

        controlHub = hwMap.get(LynxModule.class, "Control Hub");

        controlHub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        controlHub.clearBulkCache();


        //pinpoint = hwMap.get(GoBildaPinpointDriver.class, "odo");
        rearLeft.init(hwMap);
        rearRight.init(hwMap);
        rearLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rearRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        steer.init(hwMap);
        steer.setPosition(0.5);

        return this;
    }

    public Masinuta setGamepads(Gamepad gamepad1, Gamepad gamepad2)
    {
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
        return this;
    }

    public void clearCache()
    {
        controlHub.clearBulkCache();
    }

    public static Masinuta getInstance()
    {
        if (masinuta == null)
            masinuta = new Masinuta();
        return masinuta;
    }
}
