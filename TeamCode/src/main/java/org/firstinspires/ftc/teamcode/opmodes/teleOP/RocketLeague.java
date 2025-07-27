package org.firstinspires.ftc.teamcode.opmodes.teleOP;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.common.commandbase.CommandScheduler;
import org.firstinspires.ftc.teamcode.common.commandbase.ConditionalCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.InstantCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.SequentialCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.TimedCommand;
import org.firstinspires.ftc.teamcode.common.hardware.Commands;
import org.firstinspires.ftc.teamcode.common.hardware.Masinuta;
import org.firstinspires.ftc.teamcode.common.hardware.RobotHardware;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp
public class RocketLeague extends LinearOpMode {

    @Override
    public void runOpMode()
    {

        Masinuta octane = Masinuta.getInstance();



        octane.init(hardwareMap).setGamepads(gamepad1, gamepad2);

        waitForStart();

        while(opModeIsActive() && !isStopRequested()){

            telemetry.addData("octane angle" , octane.steer.getPosition());

            if(gamepad1.cross)
            {
                double angle = octane.steer.getPosition();
                if (angle>0.5)
                {
                    //octane.rearLeft.setPower(0);
                    octane.rearLeft.setPower(0);
                }
                else if (angle<0.5)
                {
                    octane.rearRight.setPower(0);
                    //octane.rearRight.setPower(0);
                }
                else if (angle == 0.5)
                {
                    octane.rearLeft.setPower(0);
                    octane.rearRight.setPower(0);
                }


            }
            else {
                if (gamepad1.right_trigger > 0) {
                    octane.rearLeft.setPower(1);
                    octane.rearRight.setPower(-1);

                } else if (gamepad1.left_trigger > 0) {
                    octane.rearLeft.setPower(-1);
                    octane.rearRight.setPower(1);
                } else {
                    octane.rearLeft.setPower(0);
                    octane.rearRight.setPower(0);
                }
            }

            octane.steer.setPosition(Math.min(Math.max((gamepad1.left_stick_x + 1) / 2,0.45), 0.55));
            //
            octane.clearCache();
            telemetry.update();
        }

    }
}