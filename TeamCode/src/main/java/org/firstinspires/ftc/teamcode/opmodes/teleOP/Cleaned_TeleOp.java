package org.firstinspires.ftc.teamcode.opmodes.teleOP;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.common.commandbase.*;
import org.firstinspires.ftc.teamcode.common.hardware.Commands;
import org.firstinspires.ftc.teamcode.common.hardware.RobotHardware;
import org.firstinspires.ftc.teamcode.common.hardware.subsystems.Subsystem;
import org.firstinspires.ftc.teamcode.common.logger.Logger;

@TeleOp
public class Cleaned_TeleOp extends LinearOpMode {

    private RobotHardware robot;
    private CommandScheduler scheduler;
    private Commands actions;

    private boolean open = false;
    private boolean isCareful = false;
    private boolean low = false;

    private RobotState state = RobotState.IDLE;

    private ButtonDebouncer leftTrigger = new ButtonDebouncer(200);
    private ButtonDebouncer rightTrigger = new ButtonDebouncer(200);

    Logger logger = Logger.getInstance();
    SequentialCommand var;

    @Override
    public void runOpMode() {

        robot = RobotHardware.getInstance();
        scheduler = CommandScheduler.getInstance();
        actions = new Commands();
        var = new SequentialCommand("intake_idle", actions.idle(),  actions.intake_idle());
        logger.enableFileLogging(true);

        robot.init(hardwareMap).setGamepads(gamepad1, gamepad2);
        scheduler.reset();

        waitForStart();

        while (opModeIsActive() && !isStopRequested()) {
            robot.read();

            // Telemetry
            telemetry.addData("State", state);
            telemetry.addData("Heading", robot.imu.getRobotYawPitchRollAngles().getPitch());
            telemetry.addData("Heading velocity", robot.drivetrain.headingVelocity);
            telemetry.addData("manually controlled", robot.drivetrain.headingManuallyControlled);
            telemetry.addData("target heading", robot.drivetrain.targetHeading);
            telemetry.addData("curr heading", robot.drivetrain.botHeading);
            telemetry.addData("turn", robot.drivetrain.turn);
            telemetry.addData("P", robot.drivetrain.P);
            telemetry.addData("Pivot Angle", robot.pivot.getPivotAngle());
            telemetry.addData("Pivot Target", robot.pivot.targetangle);
            telemetry.addData("Slides Ext", robot.slides.getExtensionCm());
            telemetry.addData("Slides Target", robot.slides.targetpos);
            telemetry.update();

            // Handle buttons
            if (gamepad1.crossWasPressed())
                handleCross();

            if (gamepad1.triangleWasPressed())
                handleTriangle();

            if (gamepad1.circleWasPressed())
                handleCircle();

            if (gamepad1.rightBumperWasPressed())
                handleRightBumper();

            if (gamepad1.leftBumperWasPressed())
                handleLeftBumper();

            if (leftTrigger.update(gamepad1.left_trigger > 0))
                handleLeftTrigger();

            if (rightTrigger.update(gamepad1.right_trigger > 0))
                handleRightTrigger();

            if (gamepad1.dpadUpWasPressed())
                isCareful = !isCareful;

            if (gamepad1.dpadDownWasPressed())
                low = !low;

            if (gamepad1.squareWasPressed())
                handleSquare();

            if (gamepad2.triangleWasPressed() && !robot.agatat) {
                robot.agatat = true;
                scheduler.schedule(new SequentialCommand(actions.idle(), actions.hang()));
            }

            scheduler.run();
            robot.subsystems.forEach(Subsystem::periodic);
            robot.write();
            robot.clearCache();
        }
    }

    // --- Button Handlers ---

    private void handleCross() {
        switch (state) {
            case INTAKE:
                scheduler.schedule(actions.arm_idle());
                open = true;
                state = RobotState.ARM_IDLE;
                break;

            case ARM_IDLE:
                scheduler.schedule(actions.intake());
                open = false;
                state = RobotState.INTAKE;
                break;

            default:
                // Other states: open/close claw
                if (open){
                    robot.claw.close();
                }
                else {
                    robot.claw.open();
                    if (robot.slides.getExtensionCm() >= 60 && !isCareful) robot.claw.go_down();
                }
                open = !open;
                break;
        }
    }

    private void handleTriangle() {
        scheduler.remove("intake_idle");
        if (!low) scheduler.schedule(new SequentialCommand("basket", actions.idle(), actions.go_basket2()));
        else scheduler.schedule(new SequentialCommand("basket", actions.idle(), actions.go_basket1()));
        open = false;
        state = RobotState.GO_BASKET;
    }

    private void handleCircle() {
        scheduler.schedule(new SequentialCommand(
                new SequentialCommand(
                        new TimedCommand(() -> robot.claw.reset_angle(), 0),
                        new TimedCommand(() -> robot.claw.close(), 0.3),
                        new TimedCommand(() -> robot.arm.reset(), 0.5),
                        new ConditionalCommand(() -> robot.slides.setTargetExtension(0)),
                        new ConditionalCommand(() -> robot.pivot.setTargetangle(90))
                ),
                actions.spec_score()
        ));
        state = RobotState.SPEC_SCORE;
        open = false;
    }

    private void handleRightBumper() {
        scheduler.remove("basket");
        switch (state) {
            case RETRACT:
                scheduler.schedule(actions.intake_idle());
                open = true;
                state = RobotState.ARM_IDLE;
                break;

            case INTAKE:
            case ARM_IDLE:
                scheduler.schedule(actions.retract());
                open = false;
                state = RobotState.RETRACT;
                break;

            default:
                // Other states: open/close claw
                scheduler.schedule(var);
                open = true;
                state = RobotState.ARM_IDLE;
                break;
        }
    }

    private void handleLeftBumper() {
        scheduler.schedule(actions.idle());
        state = RobotState.IDLE;
        open = false;
    }

    private void handleLeftTrigger() {

        switch (state) {
            case SPEC_SCORE:
                robot.slides.setTargetExtension(robot.slides.getExtensionCm() - 1);
                break;

            default:
                robot.claw.go_right();
                break;
        }
    }

    private void handleRightTrigger() {
        if (state == RobotState.SPEC_SCORE) {
            robot.slides.setTargetExtension(robot.slides.getExtensionCm() + 1);
        }
    }

    private void handleSquare() {
        scheduler.schedule(new SequentialCommand(actions.idle(), actions.spec_intake()));
        open = true;
        state = RobotState.SPEC_INTAKE;
    }

    // --- Enums ---
    private enum RobotState {
        IDLE,
        INTAKE,
        ARM_IDLE,
        GO_BASKET,
        SPEC_SCORE,
        SPEC_INTAKE,
        RETRACT,
        SOME_STATE_2
    }

    // --- Button Debouncer ---
    private static class ButtonDebouncer {
        private final double delay;
        private final ElapsedTime timer;

        public ButtonDebouncer(double delayMs) {
            delay = delayMs;
            timer = new ElapsedTime();
        }

        public boolean update(boolean pressed) {
            if (pressed && timer.milliseconds() > delay) {
                timer.reset();
                return true;
            }
            return false;
        }
    }
}
