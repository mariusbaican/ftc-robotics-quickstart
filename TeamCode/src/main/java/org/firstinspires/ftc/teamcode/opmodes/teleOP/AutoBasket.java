package org.firstinspires.ftc.teamcode.opmodes.teleOP;

import static java.lang.Math.abs;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.pedropathing.localization.Pose;
import com.pedropathing.pathgen.BezierLine;
import com.pedropathing.pathgen.PathBuilder;
import com.pedropathing.util.Constants;
import com.pedropathing.util.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import com.pedropathing.follower.Follower;
import com.pedropathing.pathgen.BezierCurve;
import com.pedropathing.pathgen.PathChain;
import com.pedropathing.pathgen.Point;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.common.commandbase.CommandScheduler;
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
public class AutoBasket extends OpMode {
    private Telemetry telemetryA;


    private Follower follower;
    ElapsedTime lastclosed = new ElapsedTime();
    private PathChain circle;
    public  PathChain line1, line2, line3, line4, line5, line6, line7, line8, line9, line10, line11, line12, line13, line14, line15,line16;
    private double xPath = 10;
    private  double yPath = 125;
    private double val = 0.1;
    private int pathState = 0;
    private boolean scored1 = false, scored2 = false, scored3 = false, scored4 = false, scored5 = false, took2 = false, took3 = false, took4 = false, took5 = false;
    RobotHardware robot;
    Commands actions;
    CommandScheduler scheduler;

    /**
     * This initializes the Follower and creates the PathChain for the "circle". Additionally, this
     * initializes the FTC Dashboard telemetry.
     *
     */

    private void buildpaths() {


        line1 = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(8.905, 65.363, Point.CARTESIAN),
                                new Point(48, 65.531, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        line2 = follower.pathBuilder()
                .addPath(
                        new BezierCurve(
                                new Point(48, 65.531, Point.CARTESIAN),
                                new Point(23.020, 42.343, Point.CARTESIAN),
                                new Point(62, 25.036, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        line3 = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(62, 25.036, Point.CARTESIAN),
                                new Point(21.340, 21.700, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        line4 = follower.pathBuilder()
                .addPath(
                        new BezierCurve(
                                new Point(21.340, 21.700, Point.CARTESIAN),
                                new Point(51.249, 23.700, Point.CARTESIAN),
                                new Point(60, 13.442, Point.CARTESIAN)
                        )
                )

                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        line5 = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(60, 13.442, Point.CARTESIAN),
                                new Point(21.340, 12.770, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        line6 = follower.pathBuilder()
                .addPath(
                        new BezierCurve(
                                new Point(21.340, 12.770, Point.CARTESIAN),
                                new Point(40.663, 14.618, Point.CARTESIAN),
                                new Point(60, 5, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        line7 = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(58, 5, Point.CARTESIAN),
                                new Point(30, 5, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .setPathEndTimeoutConstraint(1000)
                .addPath(
                        new BezierLine(
                                new Point(30, 5, Point.CARTESIAN),
                                new Point(10, 33.438, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .setPathEndTimeoutConstraint(200)
                .build();

        line8 = follower.pathBuilder()
                .addPath(
                        new BezierCurve(
                                new Point(10, 33.438, Point.CARTESIAN),
                                new Point(17.97899649941657, 67.8833138856476, Point.CARTESIAN),
                                new Point(42, 68, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        line9 = follower.pathBuilder()
                .addPath(
                        new BezierCurve(
                                new Point(42, 68, Point.CARTESIAN),
                                new Point(32.4294049008168, 29.740956826137694, Point.CARTESIAN),
                                new Point(10, 33.438, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .setPathEndTimeoutConstraint(200)
                .build();

        line10 = follower.pathBuilder()
                .addPath(
                        new BezierCurve(
                                new Point(10, 33.438, Point.CARTESIAN),
                                new Point(17.97899649941657, 67.8833138856476, Point.CARTESIAN),
                                new Point(42, 70, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        line11 = follower.pathBuilder()
                .addPath(
                        new BezierCurve(
                                new Point(42, 70, Point.CARTESIAN),
                                new Point(31.925320886814472, 29.740956826137694, Point.CARTESIAN),
                                new Point(10, 33.438, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .setPathEndTimeoutConstraint(200)
                .build();

        line12 = follower.pathBuilder()
                .addPath(
                        new BezierCurve(
                                new Point(10, 33.438, Point.CARTESIAN),
                                new Point(17.97899649941657, 67.8833138856476, Point.CARTESIAN),
                                new Point(42, 72, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        line13 = follower.pathBuilder()
                .addPath(
                        new BezierCurve(
                                new Point(42, 72, Point.CARTESIAN),
                                new Point(31.925320886814472, 29.57292882147025, Point.CARTESIAN),
                                new Point(10, 33.774, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .setPathEndTimeoutConstraint(200)
                .build();

        line14 = follower.pathBuilder()                .addPath(
                        new BezierCurve(
                                new Point(10, 33.774, Point.CARTESIAN),
                                new Point(17.97899649941657, 67.8833138856476, Point.CARTESIAN),
                                new Point(42, 73, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .build();

        line15

                = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(42, 73, Point.CARTESIAN),
                                new Point(10, 33.606, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(180))
                .setPathEndTimeoutConstraint(200)
                .build();
    }

    @Override
    public void init() {
        lastclosed.reset();
        follower = new Follower(hardwareMap, FConstants.class, LConstants.class);
        follower.setStartingPose(new Pose(8.905, 65.363, Math.toRadians(180)));
        buildpaths();
        robot = RobotHardware.getInstance();
        scheduler = CommandScheduler.getInstance();
        actions = new Commands();
        robot.init(hardwareMap).setGamepads(gamepad1, gamepad2);
        scheduler.reset();
        scheduler.schedule(new SequentialCommand(actions.idle(), actions.spec_score_auto()));

    }

    /**
     * This runs the OpMode, updating the Follower as well as printing out the debug statements to
     * the Telemetry, as well as the FTC Dashboard.
     */
    @Override
    public void loop() {
        robot.read();
        follower.update();
        autonomousPathUpdate();

        if(pathState == 2 && !scored1)
        {
            scheduler.schedule(new SequentialCommand(new TimedCommand(() -> {
                return robot.claw.open();
            }, 0.2),actions.idle(), actions.spec_intake()));
            scored1 = true;
        }

        if(pathState == 10 && !scored2)
        {
            scheduler.schedule(new SequentialCommand(new TimedCommand(() -> {
                return robot.claw.open();
            }, 0.2),actions.idle(), actions.spec_intake()));
            scored2 = true;
        }
        if(pathState == 12 && !scored3)
        {
            scheduler.schedule(new SequentialCommand(new TimedCommand(() -> {
                return robot.claw.open();
            }, 0.2),actions.idle(), actions.spec_intake()));
            scored3 = true;
        }
        if(pathState == 14 && !scored4)
        {
            scheduler.schedule(new SequentialCommand(new TimedCommand(() -> {
                return robot.claw.open();
            }, 0.2),actions.idle(), actions.spec_intake()));
            scored4 = true;
        }
        if(pathState == 16 && !scored5)
        {
            scheduler.schedule(new SequentialCommand(new TimedCommand(() -> {
                return robot.claw.open();
            }, 0.2),actions.idle(), actions.spec_intake()));
            scored5 = true;
        }

        if(follower.getPose().getX() < 11 && follower.getPose().getY() < 60 && lastclosed.milliseconds() > 2000)
        {
            scheduler.schedule(new TimedCommand(() -> {
                return robot.claw.close();
            }, 0));
            lastclosed.reset();
        }

        if(pathState == 8 && !took2)
        {
            scheduler.schedule(actions.spec_score());
            took2 = true;
        }

        if(pathState == 11 && !took3)
        {
            scheduler.schedule(actions.spec_score());
            took3 = true;
        }
        if(pathState == 13 && !took4)
        {
            scheduler.schedule(actions.spec_score());
            took4 = true;
        }
        if(pathState == 15 && !took5)
        {
            scheduler.schedule(actions.spec_score());
            took5 = true;
        }

        if(follower.getPose().getX() < 30 && follower.getPose().getY() < 40) {
            follower.setMaxPower(0.5);
        }
        else {
            follower.setMaxPower(1);
        }

        // Feedback to Driver Hub
        telemetry.addData("path state", pathState);
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.addData("Follower busy", follower.isBusy());

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
                if(robot.slides.getExtensionCm() >10)
                {
                    follower.followPath(line1, true);
                    setPathState(1);
                }
                break;

            case 1:
                if(!follower.isBusy()) {
                    follower.followPath(line2, true);
                    setPathState(2);
                }
                break;
            case 2:
                if(!follower.isBusy()) {
                    follower.followPath(line3, true);
                    setPathState(3);
                }
                break;
            case 3:
                if(!follower.isBusy()) {
                    follower.followPath(line4, true);
                    setPathState(4);
                }
                break;
            case 4:
                if(!follower.isBusy()) {
                    follower.setMaxPower(0.8);
                    follower.followPath(line5, true);
                    setPathState(5);
                }
                break;
            case 5:
                if(!follower.isBusy()) {
                    follower.followPath(line6, true);
                    setPathState(6);
                }
                break;
            case 6:
                if(!follower.isBusy()) {
                    follower.followPath(line7, true);
                    setPathState(7);
                }
               break;
            case 7:
                if(!follower.isBusy()) {
                    follower.setMaxPower(1);
                    follower.followPath(line8, true);
                    setPathState(8);
                }
                break;
            case 8:
                if(!follower.isBusy()) {
                    follower.followPath(line8, true);
                    setPathState(9);
                }
                break;
            case 9:
                if(!follower.isBusy()) {
                    follower.followPath(line9, true);
                    setPathState(10);
                }
                break;
            case 10:
                if(!follower.isBusy()) {
                    follower.followPath(line10, true);
                    setPathState(11);
                }
                break;
            case 11:
                if(!follower.isBusy()) {
                    follower.followPath(line11, true);
                    setPathState(12);
                }
                break;
            case 12:
                if(!follower.isBusy()) {
                    follower.followPath(line12, true);
                    setPathState(13);
                }
                break;
            case 13:
                if(!follower.isBusy()) {
                    follower.followPath(line13, true);
                    setPathState(14);
                }
                break;
            case 14:
                if(!follower.isBusy()) {
                    follower.followPath(line14, true);
                    setPathState(15);
                }
                break;
            case 15:
                if(!follower.isBusy()) {
                    follower.followPath(line15, true);
                    setPathState(16);
                }
                break;
        }
    }
}
