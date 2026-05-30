package brickbot.quickstart.opmode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.hardware.Commands;
import org.firstinspires.ftc.teamcode.common.hardware.RobotHardware;

import brickbot.quickstart.commandbase.CommandScheduler;
import brickbot.quickstart.devices.DeviceManager;
import brickbot.quickstart.recordautonomous.Bindings;
import brickbot.quickstart.subsystems.SubsystemManager;

public class CommonTeleOp extends Bindings {
        public RobotHardware robot = RobotHardware.getInstance();
        private CommandScheduler scheduler;
        private DeviceManager deviceManager;
        private Telemetry telemetry;
        SubsystemManager subsystems = SubsystemManager.INSTANCE;

        private FtcDashboard dashboard = FtcDashboard.getInstance();
        TelemetryPacket packet = new TelemetryPacket();

        ElapsedTime elapsedTime = new ElapsedTime();
        ElapsedTime last_scuipat = new ElapsedTime();

        private boolean lockShot = false;
        private boolean lime_has_started = false, rumbled = false, reset_finger = false;
        private boolean IsIntakeUsed = false;
        private static String telemetryCase = "SENSOR";
        private int aXnet,aYnet;
        private int beginxNet = 0, beginyNet = 0;

        public void init(HardwareMap hardwareMap, Telemetry t, Gamepad g1 , Gamepad g2)
        {
            deviceManager = DeviceManager.INSTANCE;
            deviceManager.initDevices(hardwareMap);
            robot.init(hardwareMap).setGamepads(g1,g2);
            scheduler = CommandScheduler.INSTANCE;
            scheduler.reset();
            robot.actions = new Commands();
            telemetry  = t;
            robot.auto = false;
            robot.position = new Pose(robot.x, robot.y, robot.lastHeading);
            robot.setTelemetry(telemetry);
        }

        public void Start(int team , int x , int y, Pose reset_position_close, Pose reset_position_far, Pose gate)
        {
            robot.red = team;
            robot.follower.startTeleopDrive(true);
            robot.reset_position_close = reset_position_close;
            robot.reset_position_far = reset_position_far;
            robot.gate_pos = gate;
            robot.xNet = x;
            robot.yNet = y;
            beginxNet = x;
            beginyNet = y;
        }

        @Override
        public void update(Gamepad gamepad1, Gamepad gamepad2)
        {
            //packet.put("Current Velocity", robot.shooter.currentVelocity);
            //packet.put("Target Velocity", robot.shooter.targetVelocity);

            /*if(robot.position.getY() > 120){
                robot.turret_position_offset = -2 * robot.red;
            }
            else if(robot.position.getY() < 40 && robot.position.getX() < 72){
                robot.turret_position_offset = 2 * robot.red;
            }
            else if(robot.position.getY() < 40 && robot.position.getX() > 72){
                robot.turret_position_offset = -3 * robot.red;
            }
            else{
                robot.turret_position_offset = 2;
            }*/

            robot.setGamepads(gamepad1, gamepad2);

            if (gamepad1.dpadUpWasPressed() || gamepad2.dpadUpWasPressed()){
                robot.velocity_offset += 20;
            }
            if (gamepad1.dpadDownWasPressed() || gamepad2.dpadDownWasPressed()){
                robot.velocity_offset -= 20;
            }
            if (gamepad1.circleWasPressed()) {
                robot.follower.setPose(robot.reset_position_close);
                robot.turret_offset = 0;
            }

            if (gamepad1.squareWasPressed()) {
                robot.follower.setPose(robot.reset_position_far);
            }

            if (gamepad1.triangleWasPressed()) {
                robot.automated_drive = true;
            }

            if(robot.sensors.detected_all && !rumbled){
                gamepad1.rumble(100);
                rumbled = true;
            }

            if(!robot.sensors.detected_all){
                rumbled = false;
            }
            
            handleTelemetry(telemetryCase, gamepad1, gamepad2);
            handleRightBumper(gamepad1);
            handleIntake(gamepad1);
            HandleOffset(gamepad1, gamepad2);
            scheduler.run();
            subsystems.run();
            robot.clearCache();
        }

        private void handleRightBumper(Gamepad gamepad1) {
            if (gamepad1.right_bumper) scheduler.schedule(robot.actions.shoot());
        }

        private void handleIntake(Gamepad gamepad1) {
            IsIntakeUsed = gamepad1.right_trigger > 0.3;

            if(IsIntakeUsed){
                robot.intake.on();
                robot.index.intake();
            }
            else if(!robot.actions.isShooting){
                robot.intake.off();
                robot.index.off();
            }

        }

        private void handleTelemetry(String telemetryCase, Gamepad gamepad1, Gamepad gamepad2) {
            switch (telemetryCase) {
                case "SENSOR":
                    telemetry.addData("all", robot.sensors.detected_all);
                    telemetry.addData("shooter", robot.sensors.detected_shooter);
                    telemetry.addData("middle", robot.sensors.detected_middle1 || robot.sensors.detected_middle2);
                    telemetry.addData("middle1", robot.sensors.detected_middle1);
                    telemetry.addData("middle2", robot.sensors.detected_middle2);
                    telemetry.addData("intake", robot.sensors.detected_intake);
                    telemetry.addData("targetsped", robot.shooter.targetVelocity);
                    telemetry.addData("delta", robot.shooter.targetVelocity - robot.shooter.currentVelocity);
                    telemetry.addData("encoder1", robot.frontLeft.getVelocity());
                    telemetry.addData("encoder2", robot.frontRight.getVelocity());
                    telemetry.addData("finger_pos", robot.shooter.get_pos_stopper());

                case "LIME":
                    telemetry.addData("X coordinate (IN)", robot.follower.getPose().getX());
                    telemetry.addData("Y coordinate (IN)", robot.follower.getPose().getY());
                    telemetry.addData("Heading angle (DEGREES)", Math.toDegrees(robot.follower.getPose().getHeading()));
                    telemetry.addData("lime?", robot.useLime);
                    telemetry.addData("time_since_lime_is_started?", elapsedTime.seconds());
                    telemetry.addData("lime_is_started?", lime_has_started);
                    telemetry.addData("circle?", gamepad1.circleWasPressed());
                    break;

                case "SHOOTER":
                    telemetry.addData("Flywheel currentVelocity:", robot.shooter.currentVelocity);
                    telemetry.addData("Flywheel targetvelocity:", robot.shooter.targetVelocity);
                    telemetry.addData("robot heading:", Math.toDegrees(robot.follower.getHeading()));
                    telemetry.addData("targethood", robot.shooter.target_angle);
                    telemetry.addData("xNet", robot.xNet);
                    telemetry.addData("yNet", robot.yNet);
                    break;
                case "DRIVE":
                    telemetry.addData("ticks turret", robot.turret_encoder.getCurrentPosition());
                    telemetry.addData("disgtance", robot.distance);
                    telemetry.addData("robot heading:", Math.toDegrees(robot.position.getHeading()));
                    telemetry.addData("turret target:", robot.turret.turretTarget);
                    telemetry.addData("robot theta:", robot.turret.thetaRobot);
                    telemetry.addData("turret alpha:", robot.turret.get_turret_angle());
                    telemetry.addData("robot target:", robot.turret.angleToNetField);
                    telemetry.addData("position X",robot.follower.getPose().getX());
                    telemetry.addData("position Y",robot.follower.getPose().getY());
                    telemetry.addData("follower", robot.follower.isBusy());
                    if(robot.follower.isBusy()) {
                        telemetry.addData("atend", robot.follower.atParametricEnd());
                    }

                    //telemetry.addData("X component of vel", robot.follower.getVelocity().getXComponent());
                    //telemetry.addData("Y component of vel", robot.follower.getVelocity().getYComponent());
                    //telemetry.addData("c x", robot.gamepad1.left_stick_x);
                    //telemetry.addData("c y", robot.gamepad1.left_stick_y);
                    //telemetry.addData("c right x", robot.gamepad1.right_stick_x);
                default:
                    break;
            }
            telemetry.update();


        }

        ElapsedTime time = new ElapsedTime();

        private void HandleOffset(Gamepad gamepad1, Gamepad gamepad2)
        {
            if((gamepad1.dpad_right || gamepad2.dpad_right) && time.seconds() > 0.2)
            {
                time.reset();
                robot.turret_offset -= 2;
            }
            if((gamepad1.dpad_left || gamepad2.dpad_left)  && time.seconds() > 0.2)
            {
                time.reset();
                robot.turret_offset += 2;
            }
        }
}
