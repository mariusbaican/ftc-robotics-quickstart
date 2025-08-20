package org.firstinspires.ftc.teamcode.common.hardware.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.common.hardware.RobotHardware;

public class Pto implements Subsystem {

    RobotHardware robot = RobotHardware.getInstance();
    double state = 1 / 355.0 * 14.4, pozhang1 = 0.5, pozhang2 = 0.5;

    public void hang()
    {
        pozhang1 = 1;
        pozhang2 = 1;
        state = 0.564 + 1 / 355.0 * 14.4;
    }

    @Override
    public void read() {

    }

    @Override
    public void periodic() {

    }

    @Override
    public void write() {
        robot.hang1.setPosition(pozhang1);
        robot.hang2.setPosition(pozhang2);
        robot.servo_pto.setPosition(state);
    }

    @Override
    public void init(HardwareMap hwMap) {
        robot.servo_pto.init(hwMap);
        robot.servo_pto.setPosition(0.4);
        robot.servo_pto.setPosition(state);
        robot.hang1.init(hwMap);
        robot.hang1.setPosition(0.4);
        robot.hang1.setPosition(0.5);
        robot.hang2.init(hwMap);
        robot.hang2.setPosition(0.4);
        robot.hang2.setPosition(0.5);
    }
}
