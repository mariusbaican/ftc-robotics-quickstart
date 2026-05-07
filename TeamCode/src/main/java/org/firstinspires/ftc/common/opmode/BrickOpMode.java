package org.firstinspires.ftc.common.opmode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.common.commandbase.CommandScheduler;
import org.firstinspires.ftc.common.hardware.subsystems.SubsystemManager;
import org.firstinspires.ftc.common.hardware.subsystems.Updatable;
import org.firstinspires.ftc.common.logicnodes.NodeManager;

import java.util.ArrayList;
import java.util.List;

public abstract class BrickOpMode extends LinearOpMode {
    public enum OpModeType {
        TELE_OP,
        AUTONOMOUS
    }

    public static CommandScheduler commandScheduler = CommandScheduler.getInstance();
    public static NodeManager nodeManager = NodeManager.getInstance();
    public static SubsystemManager subsystemManager = SubsystemManager.getInstance();

    public static List<Updatable> updatableList = new ArrayList<>();

    private static final ElapsedTime runtime = new ElapsedTime();

    // TODO: TEST THIS AS IT PROBABLY WON'T WORK
    public OpModeType opModeType = this.getClass().isAnnotationPresent(Autonomous.class) ? OpModeType.AUTONOMOUS : OpModeType.TELE_OP;

    private long loopCount = 0;
    private double loopFrequency;

    /**
     * This method is called once when the driver hits INIT
     */
    public abstract void onInit();

    /**
     * This method is called repeatedly after driver hits INIT, but before they hit START
     */
    public abstract void initLoop();

    /**
     * This method is called once when the driver hits START
     */
    public abstract void onStart();

    /**
     * This method is called repeatedly after driver hits START
     */
    public abstract void run();

    @Override
    public void runOpMode() {
        commandScheduler.reset();
        subsystemManager.clear();
        nodeManager.clear();

        onInit();
        while (!isStarted() && !isStopRequested()) {
            initLoop();
        }
        waitForStart();

        runtime.reset();
        onStart();

        while (opModeIsActive() && !isStopRequested()) {
            // This runs the autonomous nodes logic
            if (opModeType == OpModeType.AUTONOMOUS) {
                nodeManager.run();
            }

            // This is the user's run method
            run();

            // This runs the command queue
            commandScheduler.run();

            // This runs the read, compute, write methods of the subsystems
            subsystemManager.run();

            // This runs the update method for each updatable object
            updatableList.forEach(Updatable::update);

            loopFrequency = ++loopCount / runtime.seconds();
        }
    }
}