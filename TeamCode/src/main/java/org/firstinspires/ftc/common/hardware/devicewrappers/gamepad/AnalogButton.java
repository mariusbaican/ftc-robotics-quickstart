package org.firstinspires.ftc.teamcode.common.hardware.devicewrappers.gamepad;

import com.qualcomm.robotcore.util.Range;

public class AnalogButton extends Button
{
    private double currentValue = 0.0;
    private double lastValue = 0.0;

    private double deadzone = 0.0;

    public enum inputModifier
    {
        LINEAR,
        NONLINEAR
    }

    private inputModifier modifier = inputModifier.LINEAR;

    public AnalogButton(inputModifier modifier, double deadzone)
    {
        this.modifier = modifier;
        this.deadzone = Range.clip(deadzone, 0.0, 1.0);
    }

    public AnalogButton(inputModifier modifier)
    {
        this(modifier, 0.0);
    }

    public AnalogButton()
    {
        this(inputModifier.LINEAR, 0.0);
    }

    public void set(double currentValue)
    {
        this.lastValue = this.currentValue;
        this.currentValue = adjustForDeadzone(currentValue);
    }

    public double get()
    {
        return currentValue;
    }

    public boolean isPressed()
    {
        return (currentValue > 0.0);
    }

    public boolean justPressed()
    {
        return (Math.abs(currentValue) > 0.0 && lastValue == 0.0);
    }

    public boolean justReleased()
    {
        return (currentValue == 0.0 && Math.abs(lastValue) > 0.0);
    }

    private double adjustForModifier(double value, inputModifier modifier)
    {
        if (modifier == inputModifier.LINEAR)
            return value;
        else
            return (value + value * value * value) / 2;
    }

    private double adjustForDeadzone(double value)
    {
        return value * (1.0 - deadzone);
    }
}
