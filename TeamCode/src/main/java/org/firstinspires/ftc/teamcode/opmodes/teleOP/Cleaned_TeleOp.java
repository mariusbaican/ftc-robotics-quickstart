package org.firstinspires.ftc.teamcode.opmodes.teleOP;
import com.qualcomm.hardware.rev.RevColorSensorV3;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
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
        ElapsedTime lasttrigger2 = new ElapsedTime();
        ElapsedTime lastcircle = new ElapsedTime();
        ElapsedTime lastbumper = new ElapsedTime();
        ElapsedTime lastlbumper = new ElapsedTime();
        ElapsedTime lastsquare = new ElapsedTime();
        ElapsedTime lastdpad = new ElapsedTime();


        boolean open = false, specstate = false, isintakeidle = false, right = false, low = false;
        boolean isCareful = false;
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
            telemetry.addData("forntie", robot.sensor.getDistance(DistanceUnit.CM));
            telemetry.update();
            if(gamepad2.dpad_up && lastdpad.milliseconds() > 200)
            {
                isCareful = !isCareful;
                lastdpad.reset();
            }

            if(gamepad1.dpad_down && lastdpad.milliseconds() > 200)
            {
                low = !low;
            }

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

            if(gamepad1.left_trigger > 0 && lasttrigger.milliseconds() > 200 && (state == 6  || state == 2))
            {
                robot.claw.go_right();
                lasttrigger.reset();
            }

            if(gamepad2.left_trigger > 0 && lasttrigger2.milliseconds() > 200)
            {
                robot.slides.setTargetExtension(robot.slides.getExtensionCm() - 1);
                lasttrigger2.reset();
            }

            if(gamepad2.right_trigger > 0 && lasttrigger2.milliseconds() > 200)
            {
                robot.slides.setTargetExtension(robot.slides.getExtensionCm() + 1);
                lasttrigger2.reset();
            }


            if(gamepad1.cross && lastcross2.milliseconds() > 200 && state != 6 && state != 1)
            {
                if(open)
                    robot.claw.close();
                else{

                    robot.claw.open();
                    if(robot.slides.getExtensionCm()>=60 && !isCareful) {
                        robot.claw.go_down();
                    }
                }

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
                if(!low)
                    scheduler.schedule(new SequentialCommand(actions.idle(), actions.go_basket2()));
                else
                    scheduler.schedule(new SequentialCommand(actions.idle(), actions.go_basket1()));
                lasttriangle.reset();
                open = false;
                state = 2;
            }


            if(gamepad1.circle && lastcircle.milliseconds() > 200 && state != 5)
            {
                scheduler.schedule(new SequentialCommand(new SequentialCommand(new TimedCommand(() -> {
                    return robot.claw.reset_angle();
                }, 0), new TimedCommand(() -> {
                    return robot.claw.close();
                }, 0.3), new TimedCommand(() -> {
                    return robot.arm.reset();
                }, 0.5), new ConditionalCommand(() -> {
                    return robot.slides.setTargetExtension(0);
                }), new ConditionalCommand(() -> {
                    return robot.pivot.setTargetangle(90);
                })), actions.spec_score()));
                lastcircle.reset();
                state = 5;
            }

            if(gamepad1.right_bumper && lastbumper.milliseconds() > 200 && state != 6 && state != 1)
            {
                scheduler.reset();
                if(state == 20)
                    scheduler.schedule(actions.intake_idle());
                else{
                    scheduler.schedule(new SequentialCommand(actions.idle(),  actions.intake_idle()));
                }
                open = true;
                lastbumper.reset();
                state = 6;
            }
            else if(gamepad1.right_bumper && lastbumper.milliseconds() > 400 && (state == 6 || state == 1))
            {
                scheduler.reset();
                scheduler.schedule(actions.retract());
                open = false;
                lastbumper.reset();
                state = 20;
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