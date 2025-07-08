package org.firstinspires.ftc.teamcode.opmodes.teleOP;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.common.commandbase.CommandScheduler;
import org.firstinspires.ftc.teamcode.common.commandbase.ConditionalCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.SequentialCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.TimedCommand;
import org.firstinspires.ftc.teamcode.common.hardware.RobotHardware;
@Autonomous
public class Spec_Park extends LinearOpMode {
    RobotHardware robot = RobotHardware.getInstance();
    ElapsedTime time = new ElapsedTime();
    CommandScheduler scheduler = CommandScheduler.getInstance();
    boolean spec = false, ridicat = false;

    @Override
    public void runOpMode() throws InterruptedException {
        robot.init(hardwareMap).setGamepads(gamepad1,gamepad2);
        waitForStart();
        time.reset();
        while(opModeIsActive())
        {
            robot.read();
            if(time.milliseconds() < 1000 && !ridicat)
            {
                scheduler.schedule(new SequentialCommand(new SequentialCommand(new TimedCommand(() -> {
                    return robot.claw.reset_angle();
                }, 0), new TimedCommand(() -> {
                    return robot.claw.close();
                }, 0.3), new TimedCommand(() -> {
                    return robot.claw.wrist_basket();
                }, 0), new TimedCommand(() -> {
                    return robot.arm.reset();
                }, 0.5), new ConditionalCommand(() -> {
                    return robot.slides.setTargetExtension(0);
                }), new ConditionalCommand(() -> {
                    return robot.pivot.setTargetangle(90);
                })), new SequentialCommand(new ConditionalCommand(() -> {
                    return robot.slides.setTargetExtension(38);
                }), new TimedCommand(() -> {
                    return robot.arm.spec_score();
                }, 0.8), new TimedCommand(()->{return  robot.claw.wrist_spec();}, 0), new TimedCommand(() -> {return  robot.claw.spec_angle();}, 0))));
                ridicat = true;
            }
            if(time.milliseconds() > 5000 && time.milliseconds() < 6000)
            {
                robot.frontLeft.setPower(0);
                robot.rearRight.setPower(0);
                robot.rearLeft.setPower(0);
                robot.frontRight.setPower(0);
                if(!spec)
                {
                    spec = true;
                    scheduler.schedule(new SequentialCommand(new TimedCommand(() -> { return robot.slides.addtoTargetExtension( -15);}, 0.3), new TimedCommand(() -> {
                        return robot.claw.open();
                    }, 0.2), new ConditionalCommand(() -> { return robot.slides.setTargetExtension(0);}), new SequentialCommand(new TimedCommand(() -> {
                        return robot.claw.reset_angle();
                    }, 0), new TimedCommand(() -> {
                        return robot.claw.wrist_basket();
                    }, 0), new TimedCommand(() -> {
                        return robot.claw.close();
                    }, 0), new TimedCommand(() -> {
                        return robot.arm.reset();
                    }, 0.5), new ConditionalCommand(() -> {
                        return robot.slides.setTargetExtension(0);
                    }), new ConditionalCommand(() -> {
                        return robot.pivot.setTargetangle(90);
                    }))));
                }
            }
            else if(time.milliseconds() <= 5000)
            {
                robot.frontLeft.setPower(0.5);
                robot.rearRight.setPower(0.5);
                robot.rearLeft.setPower(0.5);
                robot.frontRight.setPower(0.5);
            }
            else if(time.milliseconds() > 6000 && time.milliseconds() < 11000)
            {
                robot.frontLeft.setPower(-0.5);
                robot.rearRight.setPower(-0.5);
                robot.rearLeft.setPower(-0.5);
                robot.frontRight.setPower(-0.5);
            }
            else if(time.milliseconds() >= 11000 && time.milliseconds() < 20000)
            {
                robot.frontLeft.setPower(0.7);
                robot.rearRight.setPower(0.7);
                robot.rearLeft.setPower(-0.7);
                robot.frontRight.setPower(-0.7);
            }
            else if(time.milliseconds() >= 20000 && time.milliseconds() < 30000)
            {
                robot.frontLeft.setPower(-0.7);
                robot.rearRight.setPower(-0.7);
                robot.rearLeft.setPower(-0.7);
                robot.frontRight.setPower(-0.7);
            }
            telemetry.addData("ridica ", ridicat);
            telemetry.update();
            scheduler.run();
            robot.write();
            robot.clearCache();
        }
    }
}
