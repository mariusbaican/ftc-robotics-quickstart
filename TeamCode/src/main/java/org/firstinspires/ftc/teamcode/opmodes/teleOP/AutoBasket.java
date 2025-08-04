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
    private PathChain circle;
    public  PathChain line1, line2, line3, line4, line5, line6, line7;
    private double xPath = 10;
    private  double yPath = 125;
    private double val = 0.1;
    private int pathState = 0;

    /**
     * This initializes the Follower and creates the PathChain for the "circle". Additionally, this
     * initializes the FTC Dashboard telemetry.
     *
     */

    private  void buildpaths() {
        line1 = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(6.049, 101.993, Point.CARTESIAN),
                                new Point(xPath, yPath, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(-45))
                .setPathEndTimeoutConstraint(3000)
                .build();

         line2 = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(xPath, yPath, Point.CARTESIAN),
                                new Point(xPath+val, yPath+val, Point.CARTESIAN)
                        )
                )
                 .setLinearHeadingInterpolation(Math.toRadians(-45), Math.toRadians(18))
                 .setPathEndTimeoutConstraint(3000)
                 .build();

        line3 = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(xPath+val, yPath+val, Point.CARTESIAN),
                                new Point(xPath+val+val/2, yPath+val+val/2, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(18), Math.toRadians(-45))
                .setPathEndTimeoutConstraint(3000)
                .build();

        line4 = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(xPath+val+val/2, yPath+val+val/2, Point.CARTESIAN),
                                new Point(xPath+val, yPath+val, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(-45), Math.toRadians(0))
                .setPathEndTimeoutConstraint(3000)
                .build();

        line5 = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(xPath+val, yPath+val, Point.CARTESIAN),
                                new Point(xPath+val+val/2, yPath+val+val/2, Point.CARTESIAN)
                        )
                )
                .setLinearHeadingInterpolation(Math.toRadians(0), Math.toRadians(-45))
                .setPathEndTimeoutConstraint(3000)
                .build();

         line6 = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(xPath+val+val/2, yPath+val+val/2, Point.CARTESIAN),
                                new Point(xPath+val, yPath+val, Point.CARTESIAN)
                        )
                )
                 .setLinearHeadingInterpolation(Math.toRadians(-45), Math.toRadians(-18))
                 .setPathEndTimeoutConstraint(3000)
                .build();

        line7 = follower.pathBuilder()
                .addPath(
                        new BezierLine(
                                new Point(xPath+val, yPath+val, Point.CARTESIAN),
                                new Point(xPath+val+val/2, yPath+val+val/2, Point.CARTESIAN)
                        )
                )
                .setConstantHeadingInterpolation(Math.toRadians(-45))
                .setPathEndTimeoutConstraint(3000)
                .build();
    }
    @Override
    public void init() {
        follower = new Follower(hardwareMap, FConstants.class, LConstants.class);
        follower.setStartingPose(new Pose(6.217, 101.993, Math.toRadians(0)));
        buildpaths();

    }

    /**
     * This runs the OpMode, updating the Follower as well as printing out the debug statements to
     * the Telemetry, as well as the FTC Dashboard.
     */
    @Override
    public void loop() {
        follower.update();
        autonomousPathUpdate();

        // Feedback to Driver Hub
        telemetry.addData("path state", pathState);
        telemetry.addData("x", follower.getPose().getX());
        telemetry.addData("y", follower.getPose().getY());
        telemetry.addData("heading", follower.getPose().getHeading());
        telemetry.addData("Follower busy", follower.isBusy());

        telemetry.update();
    }

    public void setPathState(int pState) {
        pathState = pState;
    }

    private void autonomousPathUpdate() {
        switch (pathState) {
            case 0:
                follower.followPath(line1, true);
                setPathState(1);
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
        }
    }
}
