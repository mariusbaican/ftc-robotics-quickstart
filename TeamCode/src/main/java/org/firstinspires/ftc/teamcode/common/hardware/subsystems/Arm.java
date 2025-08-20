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
    private final double init_arm = 0.54, spec_arm = 0.26, intake_idle = 0.50, intake = 0.43, spec_in = 0.87;
    private double arm_pos = init_arm;

    public void go_up()
    {
        arm_pos -=0.003;

    }

    public void score_spec_arm()
    {
        arm_pos = 0.73;

    }

    public void intake_retract()
    {
        arm_pos = intake_idle + 0.05;

    }



    public void spec_intake_arm()
    {
        arm_pos = 0.12;

    }

    public void spec_intake()
    {
        arm_pos = spec_in;

    }

    public void reset()
    {
        arm_pos = init_arm;

    }

    public void intake_idle()
    {
        arm_pos = intake_idle;

    }

    public void vert()
    {
        arm_pos = 0.8;

    }

    public void wait_up()
    {
        arm_pos = 0.56;

    }

    public void get()
    {
        arm_pos = 0.475;

    }
    public  void drop()
    {
        arm_pos = 0.585;

    }

    public void intake()
    {
        arm_pos = intake;

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
        robot.set_arm.setPosition(spec_arm + 0.12);
        robot.set_arm.setPosition(spec_arm);
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