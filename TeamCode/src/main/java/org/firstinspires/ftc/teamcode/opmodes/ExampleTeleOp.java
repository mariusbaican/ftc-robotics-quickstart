package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.common.opmode.BrickOpMode;
import org.firstinspires.ftc.teamcode.hardware.RobotState;

@Disabled
@TeleOp(name="ExampleTeleOp", group="Example")
public class ExampleTeleOp extends BrickOpMode {
    private final RobotState robot = RobotState.getInstance();

    @Override
    public void onInit() {
        robot.init(hardwareMap)
                .setGamepads(gamepad1, gamepad2);
    }

    @Override
    public void initLoop() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void run() {

    }
}
