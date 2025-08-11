package org.firstinspires.ftc.teamcode.opmodes.teleOP;

import static java.lang.Math.abs;

import com.acmerobotics.dashboard.config.Config;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierLine;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.pedropathing.follower.Follower;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.common.commandbase.CommandScheduler;
import org.firstinspires.ftc.teamcode.common.commandbase.ConditionalCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.SequentialCommand;
import org.firstinspires.ftc.teamcode.common.commandbase.TimedCommand;
import org.firstinspires.ftc.teamcode.common.hardware.Commands;
import org.firstinspires.ftc.teamcode.common.hardware.RobotHardware;
import org.firstinspires.ftc.teamcode.pedroPathing.constants.FConstants;
import org.firstinspires.ftc.teamcode.pedroPathing.constants.LConstants;

/**
 * This is the Circle autonomous OpMode. It runs the robot in a PathChain that's actually not quite
 * a circle, but some Bezier curves that have control points set essentially in a square. However,
 * it turns enough to tune your centripetal force correction and some of your heading. Some lag in
 * heading is to be expected.
 *
 * @author Anyi Lin - 10158 Scott's Bots
 * @author Aaron Yang - 10158 Scott's Bots
 * @author Harrison Womack - 10158 Scott's Bots
 * @version 1.0, 3/12/2024
 */
@Config
@Autonomous (name = "AutoBasket", group = "Examples")
public class _AutoBasket extends OpMode {
    private Telemetry telemetryA;


    private Follower follower;
    ElapsedTime lastclosed = new ElapsedTime();
    ElapsedTime timp = new ElapsedTime();
    ElapsedTime scored = new ElapsedTime();
    private PathChain circle;
    public  PathChain line1, line2, line3, line4, line5, line6, line7, line8, line9, line10, line11, line12, line13, line14, line15,line16;
    private double xPath = 10, dist = 16;
    private  double yPath = 125;
    private double val = 0.1;
    private int pathState = 0;
    private boolean scored1 = false, scored2 = false, amfost = false, scored3 = false, scored4 = false, scored5 = false, took2 = false, took3 = false, took4 = false, took5 = false, closed = false, adjusted = false, pivotat = false, up = false;
    RobotHardware robot;
    private int step = 0;
    private int step_2 = 0;
    Commands actions;
    CommandScheduler scheduler;
    private double x = 13;
    private double y = 128;
    /**
     * This initializes the Follower and creates the PathChain for the "circle". Additionally, this
     * initializes the FTC Dashboard telemetry.
     *
     */

    private void buildpaths() {

/// timpul de stopare ?????????????????????????????????????????????

        line1 = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(6.385, 113.083, Point.CARTESIAN),
                                new Point(x, y-2, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(-45))
                .setPathEndTimeoutConstraint(4000)
                .build();

        line2 = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(x, y-2, Point.CARTESIAN),
                                new Point(31.597, 122.660, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(-45), Math.toRadians(0))
                .setPathEndTimeoutConstraint(4000)
                .build();

        line3 = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(31.597, 122.660, Point.CARTESIAN),
                                new Point(x, y-2, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(-45))
                .setPathEndTimeoutConstraint(4000)
                .build();

        line4 = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(x, y-2, Point.CARTESIAN),
                                new Point(31.093, 131.734, Point.CARTESIAN)
                        )
                )

                .setLinearHeadingInterpolation(Math.toRadians(-45), Math.toRadians(1))
                .setPathEndTimeoutConstraint(4000)
                .build();

        line5 = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(31.093, 131.734, Point.CARTESIAN),
                                new Point(x, y-2, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(1), Math.toRadians(-45))
                .setPathEndTimeoutConstraint(4000)
                .build();

        line6 = follower.pathBuilder()
                .addPath(
                        new BezierCurve(
                                new Point(x, y-2, Point.CARTESIAN),
                                new Point(38.646, 115.267, Point.CARTESIAN),
                                new Point(45.368, 129.566, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(-45), Math.toRadians(90))
                .setPathEndTimeoutConstraint(4000)
                .build();

        line7 = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(45.368, 129.566, Point.CARTESIAN),
                                new Point(x, y-2, Point.CARTESIAN)

                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(90), Math.toRadians(-45))
                .setPathEndTimeoutConstraint(4000)
                .build();


        line8 = follower.pathBuilder()
                .addPath(
                        new BezierCurve(
                                new Point(x, y-2, Point.CARTESIAN),
                                new Point(56.457, 129.886, Point.CARTESIAN),
                                new Point(65.415, 101.190, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(-45), Math.toRadians(90))
                .setPathEndTimeoutConstraint(2000)
                .build();


    }

    @Override
    public void init() {
        follower = new Follower(hardwareMap, FConstants.class, LConstants.class);
        follower.setStartingPose(new Pose(6.385064177362894, 113.0828471411902, Math.toRadians(0)));
        buildpaths();
        robot = RobotHardware.getInstance();
        scheduler = CommandScheduler.getInstance();
        actions = new Commands();
        robot.init(hardwareMap).setGamepads(gamepad1, gamepad2);
        scheduler.reset();
        scheduler.schedule(new SequentialCommand(actions.idle(), new ConditionalCommand(() -> {
            return robot.pivot.setTargetangle(0);
        })));
        follower.setMaxPower(0.65);
    }

    /**
     * This runs the OpMode, updating the Follower as well as printing out the debug statements to
     * the Telemetry, as well as the FTC Dashboard.
     */
    @Override
    public void loop() {
        robot.read();
        follower.update();
        telemetry.update();

        if(follower.getPose().getX() < 17 && follower.getPose().getY() > 125 && timp.milliseconds() > 500 && !up && ((pathState == 1 || pathState == 2) || (pathState == 3 || pathState == 4) || (pathState == 5 || pathState == 6) || (pathState == 7 || pathState == 8)))
        {
            if(!amfost)
            {
                amfost=true;
                timp.reset();
            }
            else if(timp.milliseconds() > 500 && !up)
            {
                up=true;
                scheduler.schedule(new SequentialCommand(actions.idle(), actions.go_basket2(), new TimedCommand(() -> {
                    return robot.claw.open();
                }, 0), actions.idle(), new ConditionalCommand(() -> {
                    return robot.pivot.setTargetangle(0);
                })));
            }
        }

        if(follower.getPose().getX() > 20)
        {
            amfost = false;
            up = false;
        }

        // Feedback to Driver Hub
        telemetry.addData("closed", lastclosed.milliseconds());
        telemetry.addData("path state", pathState);
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.addData("Follower busy", follower.isBusy());
        telemetry.addData("ext", robot.slides.getExtensionCm());
        autonomousPathUpdate();

        telemetry.update();
        scheduler.run();
        robot.write();
        robot.clearCache();
    }

    public void setPathState(int pState) {
        pathState = pState;
    }

    private void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                if (robot.pivot.getPivotAngle() < 15) {
                    follower.followPath(line1, true);
                    setPathState(1);
                }
                break;

            case 1:
                if (!follower.isBusy() && robot.slides.getExtensionCm() < 10) {
                    follower.followPath(line2, true);
                    setPathState(2);
                }
                break;
            case 2:
                if (!follower.isBusy()) {
                    follower.followPath(line3, true);
                    setPathState(3);
                }
                break;
            case 3:
                if (!follower.isBusy() && robot.slides.getExtensionCm() < 10) {
                    follower.followPath(line4, true);
                    setPathState(4);
                }
                break;
            case 4:
                if (!follower.isBusy()) {
                    follower.followPath(line5, true);
                    setPathState(5);
                }
                break;
            case 5:
                if (!follower.isBusy() && robot.slides.getExtensionCm() < 10) {
                    follower.followPath(line6, true);
                    setPathState(6);
                }
                break;
            case 6:
                if (!follower.isBusy()) {
                    follower.followPath(line7, true);
                    setPathState(7);
                }
                break;
            case 7:
                if (!follower.isBusy() && robot.slides.getExtensionCm() < 10) {
                    follower.followPath(line8, true);
                    setPathState(8);
                }
                break;

        }
    }
}
