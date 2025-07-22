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
public class  teleOp extends LinearOpMode {

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
        double ext = 0;
        int state = 0;

        robot.init(hardwareMap).setGamepads(gamepad1, gamepad2);
        scheduler.reset();

        waitForStart();

        while(opModeIsActive() && !isStopRequested()){
            telemetry.addData("pozitie servo rotit ", robot.set_angle.getPosition());//0.8
            telemetry.addData("tung tung sahur bat ", robot.set_arm.getPosition());//0.1
            telemetry.addData("Cimpanzini bannanini",robot.wrist.getPosition());//0.3
            if(gamepad1.right_bumper && lastbumper.milliseconds() > 500)
            {
                lastbumper.reset();
                robot.set_angle.setPosition(robot.set_angle.getPosition() + 0.05);
            }

            if(gamepad1.left_bumper && lastbumper.milliseconds() > 500)
            {
                lastbumper.reset();
                robot.set_angle.setPosition(robot.set_angle.getPosition() - 0.05);
            }
            if(gamepad1.left_trigger > 0 && lasttrigger.milliseconds() > 500)
            {
                lasttrigger.reset();
                robot.set_arm.setPosition(robot.set_arm.getPosition() + 0.05);
            }
            if(gamepad1.right_trigger  > 0 && lasttrigger.milliseconds() > 500)
            {
                lasttrigger.reset();
                robot.set_arm.setPosition(robot.set_arm.getPosition() - 0.05);
            }
            if(gamepad1.cross && lastcross.milliseconds() > 500)
            {
                lastcross.reset();
                robot.state.setPosition(robot.state.getPosition() + 0.05);
            }
            if(gamepad1.circle && lastcircle.milliseconds() > 500)
            {
                lastcircle.reset();
                robot.state.setPosition(robot.state.getPosition() - 0.05);
            }
            if(gamepad1.triangle && lasttriangle.milliseconds()>500){
                lasttriangle.reset();
                robot.wrist.setPosition(robot.wrist.getPosition()- 0.05);
            }
            if(gamepad1.square && lastsquare.milliseconds()>500){
                lastsquare.reset();
                robot.wrist.setPosition(robot.wrist.getPosition()+ 0.05);
            }

            robot.clearCache();
            telemetry.update();
        }

    }
}