package org.firstinspires.ftc.teamcode.common.hardware.subsystems;

import static java.lang.Math.abs;
import static java.lang.Thread.sleep;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.common.hardware.RobotHardware;
import org.firstinspires.ftc.teamcode.common.hardware.subsystems.Camera;

public class Claw implements Subsystem {

    private RobotHardware robot = RobotHardware.getInstance();

    private final double ratio = 1.0 / 355.0;
    private final double Open = 0.25;
    private final double start = 0.8;
    private final double Closed = 0.53;
    private double set_angle_pos = start;
    private double state_pos = Closed;
    private double angle = 90;
    private double wrist_angle = 0.5;

    public void align()
    {
        if(angle <= 90 && angle > 10)
        {
            set_angle_pos = angle * ratio + robot.set_angle.getPosition();
        }
        else if(angle > 90 && angle < 170)
        {
            set_angle_pos = (-(180 - angle) * ratio + robot.set_angle.getPosition());
        }
    }

    public void wrist_intake()
    {
        wrist_angle = 0.92;//TODO set constant angle
    }

    public void wrist_intake_idle()
    {
        wrist_angle = 0.98;//TODO set constant angle
    }

    public void reset_wrist()
    {
        wrist_angle = 0.5;//TODO set constant angle
    }


    public void spec_intake()
    {
        wrist_angle = 0.58;//TODO set constant angle
    }


    public void wrist_basket()
    {
        wrist_angle = 0.61;//TODO set constant angle
    }

    public void wrist_spec()
    {
        wrist_angle = 0.56;//TODO set constant angle
    }

    public void go_up()
    {
        wrist_angle = wrist_angle + 0.04;
    }

    public void go_down()
    {
        wrist_angle = wrist_angle - 0.07;
    }

    public void go_left()
    {
        set_angle_pos = 0.25;
    }

    public void spec_angle()
    {
        set_angle_pos = 0.20;
    }

    public void go_right()
    {
        if(set_angle_pos == 0.5)
            set_angle_pos = 0.8;
        else set_angle_pos = 0.5;
    }

    public void open()
    {
        state_pos = Open;
    }

    public double pos()
    {
        return  robot.state.getPosition();
    }

    public void close()
    {
        state_pos = Closed;
    }

    public void reset_angle()
    {
        set_angle_pos = start;
    }

    public void get()
    {
        wrist_angle = 1;

    }

    public void wait_up()
    {
        wrist_angle = 1;

    }


    public void score_spec_wrist()
    {
        wrist_angle = 0.835;

    }

    public void score_spec_rotate()
    {
        set_angle_pos = 0.2;

    }

    public void spec_intake_wrist() {
        wrist_angle = 0.5;

    }


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