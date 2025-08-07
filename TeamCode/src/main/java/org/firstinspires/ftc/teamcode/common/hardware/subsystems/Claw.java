package org.firstinspires.ftc.teamcode.common.hardware.subsystems;

import static java.lang.Math.abs;
import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.common.hardware.RobotHardware;
import org.firstinspires.ftc.teamcode.common.hardware.subsystems.Camera;

public class   Claw implements Subsystem {

    private RobotHardware robot = RobotHardware.getInstance();

    private final double ratio = 1.0 / 355.0;
    private final double Open = 0.25;
    private final double start = 0.8;
    private final double Closed = 0.53;
    private double set_angle_pos = start;
    private double state_pos = Closed;
    private double angle = 90;
    private double wrist_angle = 0.5;

    public boolean align()
    {
        if(angle <= 90 && angle > 10)
        {
            set_angle_pos = angle * ratio + robot.set_angle.getPosition();
        }
        else if(angle > 90 && angle < 170)
        {
            set_angle_pos = (-(180 - angle) * ratio + robot.set_angle.getPosition());
        }
        return true;
    }

    public boolean wrist_intake()
    {
        wrist_angle = 0.74;//TODO set constant angle
        return true;
    }

    public boolean wrist_intake_idle()
    {
        wrist_angle = 0.80;//TODO set constant angle
        return true;
    }

    public boolean reset_wrist()
    {
        wrist_angle = 0.5;//TODO set constant angle
        return true;
    }


    public boolean spec_intake()
    {
        wrist_angle = 0.58;//TODO set constant angle
        return true;
    }


    public boolean wrist_basket()
    {
        wrist_angle = 0.41;//TODO set constant angle
        return true;
    }

    public boolean wrist_spec()
    {
        wrist_angle = 0.56;//TODO set constant angle
        return true;
    }

    public  boolean go_up()
    {
        wrist_angle = wrist_angle + 0.04;
        return  true;
    }

    public  boolean go_down()
    {
        wrist_angle = wrist_angle - 0.07;
        return  true;
    }

    public boolean go_left()
    {
        set_angle_pos = 0.25;
        return true;
    }

    public boolean spec_angle()
    {
        set_angle_pos = 0.20;
        return true;
    }

    public boolean go_right()
    {
        set_angle_pos = 0.5;
        return true;
    }

    public boolean open()
    {
        state_pos = Open;
        return true;
    }

    public double pos()
    {
        return  robot.state.getPosition();
    }

    public boolean close()
    {
        state_pos = Closed;
        return true;
    }

    public boolean reset_angle()
    {
        set_angle_pos = start;
        return true;
    }

    public boolean score_spec_wrist()
    {
        wrist_angle = 0.66;
        return true;
    }

    public boolean score_spec_rotate()
    {
        set_angle_pos = 0.2;
        return true;
    }
 public boolean spec_intake_wrist() {
        wrist_angle = 0.3;
        return true; }


    @Override
    public void read() {
        //angle = robot.camera.finalangle;
    }

    @Override
    public void periodic() {

    }

    @Override
    public void write() {
        robot.state.setPosition(state_pos);
        robot.wrist.setPosition(wrist_angle);
        robot.set_angle.setPosition(set_angle_pos);
    }

    @Override
    public void init(HardwareMap hwMap) {
        robot.set_angle.init(hwMap);
        robot.state.init(hwMap);
        robot.wrist.init(hwMap);
        robot.set_angle.setPosition(0.4);
        robot.set_angle.setPosition(start);
        robot.state.setPosition(0.4);
        robot.state.setPosition(Closed);
        robot.wrist.setPosition(0.4);
        robot.wrist.setPosition(wrist_angle);
    }
}