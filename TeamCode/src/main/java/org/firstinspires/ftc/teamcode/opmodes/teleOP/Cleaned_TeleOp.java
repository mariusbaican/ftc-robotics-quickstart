package org.firstinspires.ftc.teamcode.opmodes.teleOP;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.common.commandbase.CommandScheduler;
import org.firstinspires.ftc.teamcode.common.commandbase.ConditionalCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.InstantCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.SequentialCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.TimedCommand;
import org.firstinspires.ftc.teamcode.common.hardware.Commands;
import org.firstinspires.ftc.teamcode.common.hardware.RobotHardware;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp
public class  Cleaned_TeleOp extends LinearOpMode {
    @Override
    public void runOpMode()
    {

        RobotHardware robot = RobotHardware.getInstance();
        CommandScheduler scheduler = CommandScheduler.getInstance();
        Commands actions = new Commands();
        ElapsedTime lastcross = new ElapsedTime();
        ElapsedTime lastcross2   = new ElapsedTime();
        ElapsedTime lasttriangle = new ElapsedTime();
        ElapsedTime lasttrigger = new ElapsedTime();
        ElapsedTime lastcircle = new ElapsedTime();
        ElapsedTime lastbumper = new ElapsedTime();
        ElapsedTime lastlbumper = new ElapsedTime();
        ElapsedTime lastsquare = new ElapsedTime();


        boolean open = false, specstate = false, isintakeidle = false, right = false;
        int state = 0;

        robot.init(hardwareMap).setGamepads(gamepad1, gamepad2);
        scheduler.reset();

        waitForStart();

        while(opModeIsActive() && !isStopRequested()){
            robot.read();
            telemetry.addData("heading ", robot.imu.getRobotYawPitchRollAngles().getPitch());
            telemetry.addData("state ", state);
            telemetry.addData("angle ", robot.pivot.getPivotAngle());
            telemetry.addData("targetangle ", robot.pivot.targetangle);
            telemetry.addData("ext ", robot.slides.getExtensionCm());
            telemetry.addData("targetext ", robot.slides.targetpos);
            telemetry.update();


            if(gamepad1.cross && lastcross.milliseconds() > 200 && (state == 6 || state == 1))
            {
                if(state == 6) {
                    scheduler.schedule(actions.intake());
                    open = false;
                    state = 1;
                }
                else
                {
                    scheduler.schedule(actions.arm_idle());
                    open = true;
                    state = 6;
                }
                lastcross.reset();
            }

            if(gamepad1.left_trigger > 0 && lasttrigger.milliseconds() > 200 && state == 6)
            {
                if(!right)
                    robot.claw.go_right();
                else
                    robot.claw.reset_angle();
                right = !right;
                lasttrigger.reset();
            }


            if(gamepad1.cross && lastcross2.milliseconds() > 200 && state != 6 && state != 1)
            {
                if(open)
                    robot.claw.close();
                else
                    robot.claw.open();
                open = !open;
                lastcross2.reset();
            }


            if(gamepad1.left_bumper && lastlbumper.milliseconds() > 200 && state != 8)
            {
                scheduler.schedule(actions.idle());
                state = 8;
                lastlbumper.reset();
                open = false;
            }

            if(gamepad1.triangle && lasttriangle.milliseconds() > 200 && state != 2)
            {
                scheduler.reset();
                scheduler.schedule(new SequentialCommand(actions.idle(), actions.go_basket2()));
                lasttriangle.reset();
                open = false;
                state = 2;
            }


            if(gamepad1.circle && lastcircle.milliseconds() > 200 && state != 5)
            {
                scheduler.schedule(new SequentialCommand(actions.idle(), actions.spec_score()));
                lastcircle.reset();
                state = 5;
            }

            if(gamepad1.right_bumper && lastbumper.milliseconds() > 200 && state != 6)
            {
                scheduler.reset();
                scheduler.schedule(new SequentialCommand(actions.idle(),  actions.intake_idle()));
                open = true;
                lastbumper.reset();
                state = 6;
            }

            if(gamepad1.square && lastsquare.milliseconds() > 200 && state != 7)
            {

                scheduler.schedule(new SequentialCommand(actions.idle(),  actions.spec_intake()));
                lastsquare.reset();
                open = false;
                state = 7;
            }

            if(gamepad2.triangle && !robot.agatat)
            {
                robot.agatat = true;
                scheduler.schedule(new SequentialCommand(actions.idle(), actions.hang()));
            }

            scheduler.run();
            robot.write();
            robot.clearCache();
        }

    }
}