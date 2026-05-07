package org.firstinspires.ftc.teamcode.common.hardware.devicewrappers.gamepad;

public class DigitalButton extends Button
{

    private boolean currentState;
    private boolean previousState;

    public DigitalButton()
    {
        currentState = false;
        previousState = false;
    }

    public void set(boolean currentState)
    {
        previousState = this.currentState;
        this.currentState = currentState;
    }

    public boolean isPressed()
    {
        return currentState;
    }

    public boolean justPressed()
    {
        return (currentState && !previousState);
    }

    public boolean justReleased()
    {
        return (!currentState && previousState);
    }
}
