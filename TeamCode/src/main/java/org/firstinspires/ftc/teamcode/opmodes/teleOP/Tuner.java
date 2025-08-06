package org.firstinspires.ftc.teamcode.opmodes.teleOP;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.common.commandbase.CommandScheduler;
import org.firstinspires.ftc.teamcode.common.commandbase.ConditionalCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.InstantCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.SequentialCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.TimedCommand;
import org.firstinspires.ftc.teamcode.common.controltheory.PDFSController;
import org.firstinspires.ftc.teamcode.common.hardware.Commands;
import org.firstinspires.ftc.teamcode.common.hardware.RobotHardware;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Config
@com.qualcomm.robotcore.eventloop.opmode.TeleOp
public class Tuner extends LinearOpMode {

    public static int target = 0;
    @Override
    public void runOpMode()
    {

        RobotHardware robot = RobotHardware.getInstance();
        CommandScheduler scheduler = CommandScheduler.getInstance();
        Commands actions = new Commands();

        robot.init(hardwareMap).setGamepads(gamepad1, gamepad2);
        scheduler.schedule(actions.idle());
        waitForStart();

        while(opModeIsActive() && !isStopRequested()){
            robot.read();

            if(gamepad1.a)
            {
                robot.slides.setTargetExtension(target);
            }
            telemetry.addData("fn",robot.slides.getExtensionCm());
            telemetry.update();

            scheduler.run();
            robot.write();
            robot.clearCache();
            telemetry.update();
        }

    }
}