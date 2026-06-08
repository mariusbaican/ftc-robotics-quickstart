package brickbot.quickstart.opmode

import android.os.Environment
import android.util.Base64
import android.util.Log
import brickbot.quickstart.follower.Localizer
import brickbot.quickstart.commandbase.CommandScheduler
import brickbot.quickstart.devices.DeviceManager
import brickbot.quickstart.follower.Pose
import brickbot.quickstart.subsystems.Robot
import brickbot.quickstart.recordautonomous.Bindings
import brickbot.quickstart.recordautonomous.RecordingData
import brickbot.quickstart.recordautonomous.RobotState
import brickbot.quickstart.subsystems.SubsystemManager
import brickbot.quickstart.updatable.UpdatableManager
import com.qualcomm.robotcore.eventloop.opmode.Autonomous
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode
import com.qualcomm.robotcore.eventloop.opmode.TeleOp
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter
import java.io.InputStreamReader

abstract class BrickOpMode: LinearOpMode() {
    // This is the infrastructure
    protected val commandScheduler = CommandScheduler
    protected val subsystemManager = SubsystemManager
    protected val updatableManager = UpdatableManager

    // This is the robot passed by the user to have its init and update methods called
    private lateinit var internalRobot: Robot

    // This inits all the devices in the hardwareMap
    private val deviceManager = DeviceManager

    // This is used to determine the average loop times
    private var opModeStartTimestamp = 0L
    private var loopCount = 0
    protected var loopFrequency: Double = 0.0

    // This is used to record a TeleOp sequence and play it back as autonomous
    private lateinit var bindings: Bindings
    private lateinit var stateFilename: String
    private lateinit var lastSavedFile: String
    private lateinit var latestRecordingData: RecordingData
    private lateinit var readStates: MutableList<RobotState>
    private lateinit var localizer: Localizer

    private var isRecording = false

    private var teleOpAnnotation: TeleOp? = null
    private var autonomousAnnotation: Autonomous? = null

    private var recordingAnnotation: brickbot.quickstart.opmode.annotations.Recording? = null
    private var playbackAnnotation: brickbot.quickstart.opmode.annotations.Playback? = null
    private var bindingsAnnotation: brickbot.quickstart.opmode.annotations.Bindings? = null
    private var robotAnnotation: brickbot.quickstart.opmode.annotations.Robot? = null
    private var localizerAnnotation: brickbot.quickstart.opmode.annotations.Localizer? = null

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

        // This block calls the default init method and the opMode type specific init methods
        internalRobot.init()
        if (isAutonomous()) {
            internalRobot.autonomousInit()
        } else {
            internalRobot.teleOpInit()
        }

        onInit()
        while (!isStarted() && !isStopRequested) {
            initLoop()
            runInfrastructure()
        }
        waitForStart()

        opModeStartTimestamp = System.nanoTime()
        onStart()

        while (opModeIsActive() && !isStopRequested) {
            // This is the user's run method
            run()

            if (isRecordingOpMode()) {
                if (gamepad1.shareWasReleased()) {
                    // Clear the latest saved recording and invoke the gc to release memory
                    // occupied by the previously saved recording
                    latestRecordingData = RecordingData()
                    System.gc()

                    isRecording = true
                    latestRecordingData.startTimestamp = System.nanoTime()
                    onRecordingStart()
                }

                if (isRecording) {
                    latestRecordingData.addState(
                        RobotState(
                            System.nanoTime(),
                            gamepad1.toByteArray(),
                            gamepad2.toByteArray(),
                            localizer.getPosition()
                        )
                    )
                }

                if (System.nanoTime() - latestRecordingData.startTimestamp > 30e9) {
                    isRecording = false
                    onRecordingEnd()
                    gamepad1.rumble(2000)
                    gamepad2.rumble(2000)
                }

                if (::latestRecordingData.isInitialized && gamepad1.touchpadWasReleased()) {
                    writeFile()
                }
            }

            // TODO: ADD PLAYBACK LOGIC AFTER INTEGRATING WITH PATH FOLLOWER
            if (isPlaybackOpMode()) {

            }

            bindings.update(gamepad1, gamepad2)
            internalRobot.update()

            runInfrastructure()

            loopFrequency = ++loopCount / ((System.nanoTime() - opModeStartTimestamp) * 1e-9)
            telemetry.addData("Loop frequency:", "%ldHz", loopFrequency)

            if (isRecordingOpMode() && ::lastSavedFile.isInitialized) {
                telemetry.addData("Last recording saved in: ", lastSavedFile)
            }
        }

        // This clears all the devices, unless the opMode throws an exception.
        // I haven't yet found a way to clear them in that scenario.
        deviceManager.clear()

        // This clears all the subsystems, unless the opMode throws an exception.
        // I haven't yet found a way to clear them in that scenario.
        subsystemManager.clear()
    }

    /**
     * This method runs the commandScheduler and subsystemManager during init and run.
     * If any other infrastructure gets written, it should be called inside here.
     */
    private fun runInfrastructure() {
        commandScheduler.run()
        subsystemManager.run()
        updatableManager.run()
    }

    private fun handleAnnotations() {
        // OpMode type annotations
        teleOpAnnotation = this.javaClass.getAnnotation(TeleOp::class.java)
        autonomousAnnotation = this.javaClass.getAnnotation(Autonomous::class.java)

        // Additional BrickBot annotations
        recordingAnnotation = this.javaClass.getAnnotation(
            brickbot.quickstart.opmode.annotations.Recording::class.java
        )
        playbackAnnotation = this.javaClass.getAnnotation(
            brickbot.quickstart.opmode.annotations.Playback::class.java
        )
        bindingsAnnotation = this.javaClass.getAnnotation(
            brickbot.quickstart.opmode.annotations.Bindings::class.java
        )
        robotAnnotation = this.javaClass.getAnnotation(
            brickbot.quickstart.opmode.annotations.Robot::class.java
        )
        localizerAnnotation = this.javaClass.getAnnotation(
            brickbot.quickstart.opmode.annotations.Localizer::class.java
        )

        checkAnnotationsMakeSense()

        bindings = bindingsAnnotation!!.bindings.java.getDeclaredConstructor().newInstance()
        localizer = localizerAnnotation!!.localizer.java.getDeclaredConstructor().newInstance()

        handleFilename()
    }

    private fun handleFilename() {
        stateFilename = if (isRecordingOpMode()) recordingAnnotation!!.filename
        else if (isPlaybackOpMode()) playbackAnnotation!!.filename
        else "autonomous"

        val temp = stateFilename.split(".")

        if (temp.last().lowercase() == "json") {
            temp.dropLast(1)
        }

        stateFilename = temp.joinToString()

        if (isPlaybackOpMode()) {
            readFile()
        }
    }

    private fun checkAnnotationsMakeSense() {
        if (!submittedRobot()) {
            throw RuntimeException("What will this opMode do without a robot?")
        }

        if (isAutonomous()) {
            if (isRecordingOpMode()) {
                throw RuntimeException("An Autonomous cannot be a Recording OpMode.")
            }
            if (isPlaybackOpMode() && !submittedBindings()) {
                throw RuntimeException("You did not submit bindings for a playback Autonomous.")
            }
        } else if (isTeleOp()) {
            if (!submittedBindings()) {
                throw RuntimeException("You did not submit bindings for TeleOp. How do you plan on controlling the robot?")
            }
            if (isPlaybackOpMode()) {
                throw RuntimeException("A TeleOp cannot be a Playback OpMode.")
            }
            if (isRecordingOpMode() && !submittedLocalizer()) {
                throw RuntimeException("Cannot record without submitting a localizer.")
            }
        } else {
            throw RuntimeException("OpMode is neither declared as @Autonomous, nor @TeleOp.")
        }
    }

    private fun readFile() {
        val path = File(Environment.getExternalStorageDirectory(), "FIRST/recordings")
        val file = File(path, "$stateFilename.json")

        val output = mutableListOf<RobotState>()

        try {
            // Read the file content into a String
            val fis = FileInputStream(file)
            val reader = BufferedReader(InputStreamReader(fis))
            val sb = StringBuilder()
            var line = reader.readLine()

            while (line != null) {
                sb.append(line)
                line = reader.readLine()
            }

            reader.close()

            // Parse the string as a JSON Array
            val jsonArray = JSONArray(sb.toString())

            // Iterate through the array and reconstruct RobotState objects
            for (i in 0..<jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)

                val timestamp = obj.getLong("timestamp")

                // Decode the Base64 strings back into byte arrays
                val g1 = Base64.decode(obj.getString("gamepad1State"), Base64.NO_WRAP)
                val g2 = Base64.decode(obj.getString("gamepad2State"), Base64.NO_WRAP)

                val x = obj.getDouble("x")
                val y = obj.getDouble("y")
                val heading = obj.getDouble("heading")

                output.add(RobotState(timestamp, g1, g2, Pose(x, y, heading)))
            }
        } catch (e: Exception) {
            Log.e("JsonReader", "Error reading file: " + e.message)
            e.printStackTrace()
        }

        readStates = output
    }

    private fun writeFile() {
        val robotStateArray = JSONArray()

        try {
            for (state in latestRecordingData.stateList) {
                val stateNode = JSONObject()

                stateNode.put("timestamp", state.timestamp)

                // Encode the byte arrays into Base64 strings for JSON compatibility
                stateNode.put("gamepad1State", Base64.encodeToString(state.gamepad1State, Base64.NO_WRAP))
                stateNode.put("gamepad2State", Base64.encodeToString(state.gamepad2State, Base64.NO_WRAP))

                stateNode.put("x", state.pose.x)
                stateNode.put("y", state.pose.y)
                stateNode.put("heading", state.pose.heading)

                robotStateArray.put(stateNode)
            }

            val path = File(Environment.getExternalStorageDirectory(), "FIRST/recordings")

            if (!path.exists()) {
                path.mkdirs()
            }

            var file = File(path, stateFilename)
            var counter = 0

            // Try to find a file that doesn't already exist, to avoid overwriting
            while (file.exists()) {
                file = File(path, stateFilename + (++counter) + ".json")
            }

            lastSavedFile = "$stateFilename$counter.json"

            val writer = FileWriter(file)
            writer.write(robotStateArray.toString(2))
            writer.flush()
            writer.close()
        } catch (e: Exception) {
            Log.e("JsonReader", "Error writing to file: " + e.message)
            e.printStackTrace()
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

    private fun submittedRobot(): Boolean {
        return robotAnnotation != null
    }

    private fun submittedLocalizer(): Boolean {
        return localizerAnnotation != null
    }
}