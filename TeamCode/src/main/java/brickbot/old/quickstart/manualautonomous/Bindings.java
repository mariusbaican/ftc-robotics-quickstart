package brickbot.old.quickstart.manualautonomous;

import com.qualcomm.robotcore.hardware.Gamepad;

public abstract class Bindings {
    public abstract void update(Gamepad gamepad1, Gamepad gamepad2);

    public void update(byte[] gamepad1State, byte[] gamepad2State) {
        Gamepad gamepad1 = new Gamepad();
        Gamepad gamepad2 = new Gamepad();

        gamepad1.fromByteArray(gamepad1State);
        gamepad2.fromByteArray(gamepad2State);

        update(gamepad1, gamepad2);
    }

    public void update(GamepadState gamepadState) {
        update(gamepadState.getGamepad1State(), gamepadState.getGamepad2State());
    }
}
