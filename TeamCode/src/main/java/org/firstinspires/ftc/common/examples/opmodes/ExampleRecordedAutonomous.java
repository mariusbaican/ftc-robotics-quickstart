package org.firstinspires.ftc.common.examples.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.common.manualautonomous.GamepadState;
import org.firstinspires.ftc.common.opmode.BrickOpMode;
import org.firstinspires.ftc.common.examples.robot.ExampleBindings;

import java.io.File;
import java.util.LinkedList;
import java.util.Objects;

@Autonomous(name="ExampleRecordedAutonomous", group="Examples")
public class ExampleRecordedAutonomous extends BrickOpMode {
    private File file = null;
    private LinkedList<GamepadState> gamepadStates = null;
    private GamepadState currentState = null;
    private long startTimestamp = 0;

    // TODO: INSTANTIATE YOUR OWN BINDINGS CLASS
    private final ExampleBindings bindings = new ExampleBindings();

    @Override
    public void onInit() {
        // TODO: UPDATE THIS WITH YOUR OWN SAVE FILE NAME
        file = new File("RedAutonomous.json");
        gamepadStates = new LinkedList<>();

        // TODO: ADD JSON READING LOGIC
    }

    @Override
    public void initLoop() {

    }

    @Override
    public void onStart() {
        startTimestamp = System.nanoTime();
        currentState = gamepadStates.pop();
    }

    @Override
    public void run() {
        long currentTimestamp = System.nanoTime() - startTimestamp;
        while (Objects.requireNonNull(gamepadStates.peek()).getTimestamp() < currentTimestamp) {
            currentState = gamepadStates.pop();
        }

        // TODO: ADD YOUR OWN BINDINGS CLASS CALL
        bindings.update(currentState);
    }
}
