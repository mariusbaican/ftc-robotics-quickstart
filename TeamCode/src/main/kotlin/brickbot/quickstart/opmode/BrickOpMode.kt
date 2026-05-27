package brickbot.quickstart.opmode

import brickbot.quickstart.commandbase.CommandScheduler
import brickbot.quickstart.devices.DeviceManager
import brickbot.quickstart.recordautonomous.Bindings
import brickbot.quickstart.opmode.annotations.Playback
import brickbot.quickstart.recordautonomous.Recording
import brickbot.quickstart.subsystems.SubsystemManager
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import com.qualcomm.robotcore.util.ElapsedTime

abstract class BrickOpMode: LinearOpMode() {
    protected val commandScheduler = CommandScheduler
    protected val subsystemManager = SubsystemManager

    private val deviceManager = DeviceManager
    private val runtime = ElapsedTime()

    private var loopCount = 0
    protected var loopFrequency: Double = 0.0

    private lateinit var bindings: Bindings
    private lateinit var stateFilename: String
    private lateinit var latestRecording: Recording

    private var teleOpAnnotation: TeleOp? = null
    private var autonomousAnnotation: Autonomous? = null
    private var recordingAnnotation: brickbot.quickstart.opmode.annotations.Recording? = null
    private var playbackAnnotation: Playback? = null
    private var bindingsAnnotation: brickbot.quickstart.opmode.annotations.Bindings? = null

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

    /**
     * This method is called at the start of a TeleOp recording.
     */
    abstract fun onRecordingStart()

    /**
     * This method is called at the end of a TeleOp recording.
     */
    abstract fun onRecordingEnd()

    override fun runOpMode() {
        commandScheduler.reset()

        // This calls the init methods automatically for all devices
        deviceManager.initDevices(hardwareMap)

        // This checks for all annotations of interest, ensures they make sense
        // and then reads the data from them
        handleAnnotations()

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

            // TODO: ADD THE RECORDING START AND END BEHAVIOURS
            
            bindings.update(gamepad1, gamepad2)

            // This runs the command queue
            commandScheduler.run()

            // This runs the read, compute, write methods of the subsystems
            subsystemManager.run()

            loopFrequency = ++loopCount / runtime.seconds()
        }

        // This clears all the devices, unless the opMode throws an exception.
        // I don't think there is a way to clear them in that scenario.
        deviceManager.clear()

        // This clears all the subsystems, unless the opMode throws an exception.
        // I don't think there is a way to clear them in that scenario.
        subsystemManager.clear()
    }

    fun readFile(filename: String) {
        // TODO: Implement something separate to handle the JSON reading and writing logic
    }

    private fun handleAnnotations() {
        // OpMode type annotations
        teleOpAnnotation = this.javaClass.getAnnotation(TeleOp::class.java)
        autonomousAnnotation = this.javaClass.getAnnotation(Autonomous::class.java)

        // Additional BrickBot annotations
        recordingAnnotation = this.javaClass.getAnnotation(brickbot.quickstart.opmode.annotations.Recording::class.java)
        playbackAnnotation = this.javaClass.getAnnotation(Playback::class.java)
        bindingsAnnotation = this.javaClass.getAnnotation(
            brickbot.quickstart.opmode.annotations.Bindings::class.java)

        checkAnnotationsMakeSense()

        bindings = bindingsAnnotation!!.bindings.java.getDeclaredConstructor().newInstance()

        sanitizeFilename()
    }

    private fun sanitizeFilename() {
        stateFilename = if (isRecordingOpMode()) recordingAnnotation!!.filename
        else if (isPlaybackOpMode()) playbackAnnotation!!.filename
        else "autonomous"

        val temp = stateFilename.split(".")

        if (temp.last().lowercase() == "json") {
            temp.dropLast(1)
        }

        stateFilename = temp.joinToString()
    }

    private fun checkAnnotationsMakeSense() {
        if (isAutonomous()) {
            if (isRecordingOpMode()) {
                throw RuntimeException("An Autonomous cannot be a Recording OpMode.")
            }
        } else if (isTeleOp()) {
            if (!submittedBindings()) {
                throw RuntimeException("You did not submit bindings for TeleOp. How do you plan on controlling the robot?")
            }
            if (isPlaybackOpMode()) {
                throw RuntimeException("A TeleOp cannot be a Playback OpMode.")
            }
        } else {
            throw RuntimeException("OpMode is neither declared as @Autonomous, nor @TeleOp.")
        }
    }

    private fun isAutonomous(): Boolean {
        return autonomousAnnotation != null
    }

    private fun isTeleOp(): Boolean {
        return teleOpAnnotation != null
    }

    private fun isRecordingOpMode(): Boolean {
        return recordingAnnotation != null
    }

    private fun isPlaybackOpMode(): Boolean {
        return playbackAnnotation != null
    }

    private fun submittedBindings(): Boolean {
        return bindingsAnnotation != null
    }
}