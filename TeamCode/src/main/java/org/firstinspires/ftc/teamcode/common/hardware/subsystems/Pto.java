package org.firstinspires.ftc.teamcode.common.hardware.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.common.hardware.RobotHardware;

public class Pto implements Subsystem {

    RobotHardware robot = RobotHardware.getInstance();
    double state = 1 / 355.0 * 14.4;

    public boolean hang()
    {
        state = 0.564 + 1 / 355.0 * 14.4;
        return true;
    }

    @Override
    public void read() {

    }

    @Override
    public void periodic() {

    }

    @Override
    public void write() {
        robot.servo_pto.setPosition(state);
    }

    @Override
    public void init(HardwareMap hwMap) {
        robot.servo_pto.init(hwMap);
        robot.servo_pto.setPosition(0.4);
        robot.servo_pto.setPosition(0.5);
    }
}
