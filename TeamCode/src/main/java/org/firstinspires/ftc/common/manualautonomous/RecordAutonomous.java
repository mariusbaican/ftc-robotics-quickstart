package org.firstinspires.ftc.common.manualautonomous;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.common.opmode.BrickOpMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@TeleOp(name="RecordAutonomous", group="Utilities")
public class RecordAutonomous extends BrickOpMode {
    private enum AutonomousAlliance {
        RED("Red"),
        BLUE("Blue");

        private final String name;

        AutonomousAlliance(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    private List<GamepadState> gamepadStateHistory;
    private boolean recording = false;
    private AutonomousAlliance alliance = AutonomousAlliance.RED;

    private long startTimestamp;

    @Override
    public void onInit() {
        gamepadStateHistory = new ArrayList<>();
        startTimestamp = 0;
    }

    @Override
    public void initLoop() {

    }

    @Override
    public void onStart() {

    }

    @Override
    public void run() {
        // Start recording after share is pressed
        if (gamepad1.shareWasReleased()) {
            gamepadStateHistory = new ArrayList<>();

            // Deletes the saved gamepad states from memory
            // The garbage collector would've deleted them whenever it needed more memory
            // but this avoids it happening mid-recording and ruining the timestamps
            System.gc();

            recording = true;
            startTimestamp = System.nanoTime();
        }

        // Toggle alliance via dpad_left or dpad_right
        if (gamepad1.dpadLeftWasReleased() || gamepad1.dpadRightWasReleased()) {
            alliance = (alliance == AutonomousAlliance.RED) ? AutonomousAlliance.BLUE : AutonomousAlliance.RED;
        }

        if (recording) {
            // Read the time in nanoseconds
            long currentTimestamp = System.nanoTime();

            // Add another state entry
            gamepadStateHistory.add(new GamepadState(currentTimestamp, gamepad1.toByteArray(), gamepad2.toByteArray()));

            // If 30 seconds have passed, stop recording and notify through haptic feedback
            if (startTimestamp > 30 * 1e9) {
                recording = false;
                gamepad1.rumble(1000);
                gamepad2.rumble(1000);
            }
        } else if (gamepad1.touchpadWasReleased()) {
            ObjectMapper mapper = new ObjectMapper();
            ArrayNode gamepadStateArray = mapper.createArrayNode();

            // Go through each saved state and create a JSON node
            for (GamepadState state : gamepadStateHistory) {
                ObjectNode gamepadStateNode = mapper.createObjectNode();

                gamepadStateNode.put("timestamp", state.getTimestamp());
                gamepadStateNode.put("gamepad1State", state.getGamepad1State());
                gamepadStateNode.put("gamepad2State", state.getGamepad2State());

                gamepadStateArray.add(gamepadStateNode);
            }

            ObjectWriter objectWriter = mapper.writerWithDefaultPrettyPrinter();

            try {
                File file = new File(alliance.getName() + "Autonomous.json");
                int counter = 0;

                // Avoid overwriting an already existing save file by adding a number to the end of the name
                // TODO: VERIFY file.exists() BEHAVES AS EXPECTED
                // This might throw an exception if it finds an already existing file, which would stop the
                // intended behavior of avoiding file conflicts and exit the try catch block without
                // finding a new file name
                while (file.exists()) {
                    file = new File(alliance.getName() + "Autonomous" + (++counter) + ".json");
                }

                // Writes to the file created above
                objectWriter.writeValue(file, gamepadStateArray);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        telemetry.addData("Current alliance: ", alliance.getName());
        telemetry.update();
    }
}
