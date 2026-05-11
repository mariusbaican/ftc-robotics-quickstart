package org.firstinspires.ftc.common.examples.robot;

import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.common.hardware.subsystems.SubsystemManager;
import org.firstinspires.ftc.robotcore.external.Supplier;
import org.firstinspires.ftc.robotcore.external.navigation.VoltageUnit;

public class ExampleRobotState
{
    public static final ExampleRobotState INSTANCE = new ExampleRobotState();

    public Gamepad gamepad1;
    public Gamepad gamepad2;

    // Declare hubs
    private LynxModule controlHub;
    private LynxModule expansionHub;

    // This returns the current voltage of the robot
    public Supplier<Double> voltageSupplier;

    SubsystemManager subsystems = SubsystemManager.getInstance();

    public static ExampleRobotState getInstance()
    {
        return INSTANCE;
    }

    public ExampleRobotState init(HardwareMap hwMap)
    {
        subsystems.clear();

        // Initialize the controlHub and expansionHub
        controlHub = hwMap.get(LynxModule.class, "Control Hub");
        expansionHub = hwMap.get(LynxModule.class, "Expansion Hub 2");

        controlHub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
        expansionHub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);

        controlHub.clearBulkCache();
        expansionHub.clearBulkCache();

        voltageSupplier = () -> controlHub.getAuxiliaryVoltage(VoltageUnit.VOLTS);

        subsystems.init(hwMap);
        return this;
    }

    public ExampleRobotState setGamepads(Gamepad gamepad1, Gamepad gamepad2)
    {
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;
        return this;
    }

    public void clearCache()
    {
        controlHub.clearBulkCache();
        expansionHub.clearBulkCache();
    }

    public double getVoltage() {
        return voltageSupplier.get();
    }

}