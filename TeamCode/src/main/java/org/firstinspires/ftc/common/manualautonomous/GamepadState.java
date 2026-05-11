package org.firstinspires.ftc.common.manualautonomous;

public class GamepadState {
    private long timestamp;
    private byte[] gamepad1State;
    private byte[] gamepad2State;

    public GamepadState(long timestamp, byte[] gamepad1State, byte[] gamepad2State) {
        this.timestamp = timestamp;
        this.gamepad1State = gamepad1State;
        this.gamepad2State = gamepad2State;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public byte[] getGamepad1State() {
        return gamepad1State;
    }

    public byte[] getGamepad2State() {
        return gamepad2State;
    }
}
