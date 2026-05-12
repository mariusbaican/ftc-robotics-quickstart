package org.firstinspires.ftc.common.examples.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.common.manualautonomous.GamepadState;
import org.firstinspires.ftc.common.manualautonomous.JsonReader;
import org.firstinspires.ftc.common.opmode.BrickOpMode;
import org.firstinspires.ftc.common.examples.robot.ExampleBindings;

import java.util.LinkedList;
import java.util.Objects;

@Autonomous(name="ExampleRecordedAutonomous", group="Examples")
public class ExampleRecordedAutonomous extends BrickOpMode {
    private LinkedList<GamepadState> gamepadStates = null;
    private GamepadState currentState = null;
    private long startTimestamp = 0;

    // TODO: INSTANTIATE YOUR OWN BINDINGS CLASS
    private final ExampleBindings bindings = new ExampleBindings();

    @Override
    public void onInit() {
        // TODO: UPDATE THIS WITH YOUR OWN SAVE FILE NAME
        gamepadStates = new JsonReader<GamepadState>().readFile("RedAutonomous.json", GamepadState.class);
    }

    @Override
    public void initLoop() {

    }

    @Override
    public void onStart() {
        // Saves the system time at the start of autonomous to align with the gamepad state timestamps
        startTimestamp = System.nanoTime();
        currentState = gamepadStates.pop();
    }

    @Override
    public void run() {
        long currentTimestamp = System.nanoTime() - startTimestamp;
        // Removes all past gamepad states from the queue
        while (Objects.requireNonNull(gamepadStates.peek()).getTimestamp() < currentTimestamp) {
            currentState = gamepadStates.pop();
        }

        // TODO: ADD YOUR OWN BINDINGS CLASS CALL
        bindings.update(currentState);
    }
}
