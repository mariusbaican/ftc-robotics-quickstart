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
import org.firstinspires.ftc.teamcode.common.hardware.subsystems.Pto;
import  org.firstinspires.ftc.teamcode.common.hardware.subsystems.Slides;
import  org.firstinspires.ftc.teamcode.common.hardware.subsystems.Subsystem;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;

import com.revrobotics.ColorSensorV3;

import java.util.ArrayList;

public class RobotHardware
{
	public static RobotHardware robotHardware = null;

	private static boolean resetPinpoint = true;

	public double voltage = 12.0;

	// Declare Devices
	LynxModule controlHub;
	LynxModule expansionHub;




	public Gamepad gamepad1;
	public Gamepad lastGamepad1;
	public Gamepad gamepad2;
	public Gamepad lastGamepad2;

	public BetterMotor frontLeft = new BetterMotor("frontLeft");
	public BetterMotor rearLeft = new BetterMotor("rearLeft");
	public BetterMotor rearRight = new BetterMotor("rearRight");
	public BetterMotor frontRight = new BetterMotor("frontRight");

	public BetterMotor pivoting = new BetterMotor("pivoting");
	public BetterEncoder sliderEnc1 = new BetterEncoder("frontLeft");
	public BetterMotor sliderr = new BetterMotor("sliderr");
	public BetterMotor sliderl = new BetterMotor("sliderl");
	public BetterEncoder pivotEnc = new BetterEncoder("rearRight");


	public IMU imu;

	public BetterServo set_angle = new BetterServo("set_angle");
	public BetterServo state = new BetterServo("state");
	public BetterServo wrist = new BetterServo("wrist");
	public BetterServo set_arm = new BetterServo("set_arm");
	public BetterServo servo_pto = new BetterServo("pto");
	public BetterServo hang1 = new BetterServo("hang1");
	public BetterServo hang2 = new BetterServo("hang2");



	// Declare Subsystems
	public Claw claw;
	public Arm arm;
	public Pivot pivot;
	public Slides slides;
	public Camera camera;
	public Pto pto;
	public FieldCentricV2 drivetrain;
	public Supplier<Double> voltageSupplier;
	public GoBildaPinpointDriver pinpoint;
	public boolean agatat = false;
	public boolean ridicat = false;

	ArrayList<Subsystem> subsystems = new ArrayList<>();

	public RobotHardware init(HardwareMap hwMap)
	{
		subsystems.clear();

		drivetrain = new FieldCentricV2();


		claw = new Claw();
		arm = new Arm();
		pto = new Pto();
		pivot = new Pivot();
		slides = new Slides();

		//camera = new Camera();



		controlHub = hwMap.get(LynxModule.class, "Control Hub");
		expansionHub = hwMap.get(LynxModule.class, "Expansion Hub 3");

		controlHub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
		expansionHub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
		controlHub.clearBulkCache();
		expansionHub.clearBulkCache();
		voltageSupplier = () -> controlHub.getAuxiliaryVoltage(VoltageUnit.VOLTS);

		imu = hwMap.get(IMU.class, "imu");

		IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
				RevHubOrientationOnRobot.LogoFacingDirection.LEFT,
				RevHubOrientationOnRobot.UsbFacingDirection.UP));

		imu.initialize(parameters);

		//pinpoint = hwMap.get(GoBildaPinpointDriver.class, "odo");

		subsystems.add(claw);
		subsystems.add(arm);
		subsystems.add(drivetrain);
		subsystems.add(slides);
		subsystems.add(pto);
		frontLeft.init(hwMap);
		rearLeft.init(hwMap);
		rearRight.init(hwMap);
		frontRight.init(hwMap);
		//pinpoint.resetPosAndIMU();
		subsystems.add(pivot);

		for (Subsystem subsystem : subsystems)
			subsystem.init(hwMap);

		CommandScheduler.getInstance().addSubsystems(subsystems);

		return this;
	}

	public RobotHardware setGamepads(Gamepad gamepad1, Gamepad gamepad2)
	{
		this.gamepad1 = gamepad1;
		this.gamepad2 = gamepad2;
		return this;
	}

	public void read()
	{
		for (Subsystem subsystem : subsystems)
			subsystem.read();
		voltage = controlHub.getInputVoltage(VoltageUnit.VOLTS);
	}

	public void write()
	{
		for (Subsystem subsystem : subsystems)
			subsystem.write();
	}

	public void clearCache()
	{
		controlHub.clearBulkCache();
		expansionHub.clearBulkCache();
	}


	public static RobotHardware getInstance()
	{
		if (robotHardware == null)
			robotHardware = new RobotHardware();
		return robotHardware;
	}
}
