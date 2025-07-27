package org.firstinspires.ftc.teamcode.common.hardware.subsystems;

import static java.lang.Math.abs;
import static java.lang.Thread.sleep;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.hardware.RobotHardware;
import org.firstinspires.ftc.teamcode.common.hardware.devicewrappers.BetterMotor;
import org.firstinspires.ftc.teamcode.common.hardware.devicewrappers.BetterServo;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Arm implements Subsystem
{
    RobotHardware robot = RobotHardware.getInstance();
    private final double init_arm = 0.54, spec_arm = 0.26, intake_idle = 0.55, intake = 0.46, spec_in = 0.87;
    private double arm_pos = init_arm;

    public boolean go_up()
    {
        arm_pos -=0.003;
        return true;
    }

    public boolean score_spec_arm()
    {
        arm_pos = 0.65;
        return true;
    }

    public boolean spec_intake_arm()
    {
        arm_pos = 0.1;
        return true;
    }

    public boolean spec_intake()
    {
        arm_pos = spec_in;
        return true;
    }

    public boolean reset()
    {
        arm_pos = init_arm;
        return true;
    }

    public boolean intake_idle()
    {
        arm_pos = intake_idle;
        return true;
    }

    public boolean intake()
    {
        arm_pos = intake;
        return true;
    }

    public void setpos(double pos)
    {
        arm_pos = pos;
    }

    public double pos()
    {
        return robot.set_arm.getPosition();
    }

    public void init(HardwareMap hwMap)
    {
        robot.set_arm.init(hwMap);
        robot.arm.setpos(spec_arm);
        robot.arm.setpos(spec_arm);
    }

    @Override
    public void read() {

    }

    @Override
    public void periodic() {

    }

    @Override
    public void write() {
        robot.set_arm.setPosition(arm_pos);
    }
}