package brickbot.quickstart.opmode

import brickbot.quickstart.commandbase.CommandScheduler
import brickbot.quickstart.devices.DeviceManager
import brickbot.quickstart.subsystems.SubsystemManager
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.util.ElapsedTime

abstract class BrickOpMode: LinearOpMode() {
    protected val commandScheduler = CommandScheduler
    protected val subsystemManager = SubsystemManager

    private val deviceManager = DeviceManager
    private val runtime = ElapsedTime()

    private var loopCount = 0
    protected var loopFrequency: Double = 0.0

    /**
     * This method is called once when the driver hits INIT.
     */
    abstract fun onInit()

    /**
     * This method is called repeatedly after driver hits INIT, but before they hit START.
     */
    abstract fun initLoop()

    /**
     * This method is called once when the driver hits START.
     */
    abstract fun onStart()

    /**
     * This method is called repeatedly after driver hits START.
     */
    abstract fun run()

    override fun runOpMode() {
        commandScheduler.reset()
        subsystemManager.clear()

        // This calls the init methods automatically for all devices
        deviceManager.initDevices()

        onInit()
        while (!isStarted() && !isStopRequested()) {
            initLoop()
        }
        waitForStart()

        runtime.reset()
        onStart()

        while (opModeIsActive() && !isStopRequested()) {
            // This is the user's run method
            run()

            // This runs the command queue
            commandScheduler.run()

            // This runs the read, compute, write methods of the subsystems
            subsystemManager.run()

            loopFrequency = ++loopCount / runtime.seconds()
        }
    }
}