package org.firstinspires.ftc.teamcode.common.hardware.devicewrappers.gamepad;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.HashMap;

public class BetterGamepad
{
    Gamepad gamepad;

    private HashMap<DigitalBindings, DigitalButton> digitalButtons = new HashMap<>();
    private HashMap<AnalogBindings, AnalogButton>  analogButtons = new HashMap<>();


    public BetterGamepad(Gamepad gamepad)
    {
        this.gamepad = gamepad;

        //digitalButtons.put(DigitalBindings.touchpad_finger_1,  new DigitalButton());
        //digitalButtons.put(DigitalBindings.touchpad_finger_2,  new DigitalButton());
        //digitalButtons.put(DigitalBindings.touchpad,           new DigitalButton());
        digitalButtons.put(DigitalBindings.left_stick_button,  new DigitalButton());
        digitalButtons.put(DigitalBindings.right_stick_button, new DigitalButton());
        digitalButtons.put(DigitalBindings.dpad_up,            new DigitalButton());
        digitalButtons.put(DigitalBindings.dpad_down,          new DigitalButton());
        digitalButtons.put(DigitalBindings.dpad_left,          new DigitalButton());
        digitalButtons.put(DigitalBindings.dpad_right,         new DigitalButton());
        digitalButtons.put(DigitalBindings.a,                  new DigitalButton());
        digitalButtons.put(DigitalBindings.b,                  new DigitalButton());
        digitalButtons.put(DigitalBindings.x,                  new DigitalButton());
        digitalButtons.put(DigitalBindings.y,                  new DigitalButton());
        digitalButtons.put(DigitalBindings.cross,              new DigitalButton());
        digitalButtons.put(DigitalBindings.circle,             new DigitalButton());
        digitalButtons.put(DigitalBindings.square,             new DigitalButton());
        digitalButtons.put(DigitalBindings.triangle,           new DigitalButton());
        digitalButtons.put(DigitalBindings.guide,              new DigitalButton());
        digitalButtons.put(DigitalBindings.start,              new DigitalButton());
        digitalButtons.put(DigitalBindings.options,            new DigitalButton());
        digitalButtons.put(DigitalBindings.back,               new DigitalButton());
        digitalButtons.put(DigitalBindings.share,              new DigitalButton());
        digitalButtons.put(DigitalBindings.left_bumper,        new DigitalButton());
        digitalButtons.put(DigitalBindings.right_bumper,       new DigitalButton());

        analogButtons .put(AnalogBindings.left_trigger,        new AnalogButton());
        analogButtons .put(AnalogBindings.right_trigger,       new AnalogButton());
        analogButtons .put(AnalogBindings.left_stick_x,        new AnalogButton());
        analogButtons .put(AnalogBindings.left_stick_y,        new AnalogButton());
        analogButtons .put(AnalogBindings.right_stick_x,       new AnalogButton());
        analogButtons .put(AnalogBindings.right_stick_y,       new AnalogButton());
    }

    public void update()
    {
        //digitalButtons.get(DigitalBindings.touchpad_finger_1)  .set(gamepad.touchpad_finger_1);
        //digitalButtons.get(DigitalBindings.touchpad_finger_2)  .set(gamepad.touchpad_finger_2);
        //digitalButtons.get(DigitalBindings.touchpad)           .set(gamepad.touchpad);
        digitalButtons.get(DigitalBindings.left_stick_button)  .set(gamepad.left_stick_button);
        digitalButtons.get(DigitalBindings.right_stick_button) .set(gamepad.right_stick_button);
        digitalButtons.get(DigitalBindings.dpad_up)            .set(gamepad.dpad_up);
        digitalButtons.get(DigitalBindings.dpad_down)          .set(gamepad.dpad_down);
        digitalButtons.get(DigitalBindings.dpad_left)          .set(gamepad.dpad_left);
        digitalButtons.get(DigitalBindings.dpad_right)         .set(gamepad.dpad_right);
        digitalButtons.get(DigitalBindings.a)                  .set(gamepad.a);
        digitalButtons.get(DigitalBindings.b)                  .set(gamepad.b);
        digitalButtons.get(DigitalBindings.x)                  .set(gamepad.x);
        digitalButtons.get(DigitalBindings.y)                  .set(gamepad.y);
        digitalButtons.get(DigitalBindings.cross)              .set(gamepad.cross);
        digitalButtons.get(DigitalBindings.circle)             .set(gamepad.circle);
        digitalButtons.get(DigitalBindings.square)             .set(gamepad.square);
        digitalButtons.get(DigitalBindings.triangle)           .set(gamepad.triangle);
        digitalButtons.get(DigitalBindings.guide)              .set(gamepad.guide);
        digitalButtons.get(DigitalBindings.start)              .set(gamepad.start);
        digitalButtons.get(DigitalBindings.options)            .set(gamepad.options);
        digitalButtons.get(DigitalBindings.back)               .set(gamepad.back);
        digitalButtons.get(DigitalBindings.share)              .set(gamepad.share);
        digitalButtons.get(DigitalBindings.left_bumper)        .set(gamepad.left_bumper);
        digitalButtons.get(DigitalBindings.right_bumper)       .set(gamepad.right_bumper);

        analogButtons.get(AnalogBindings.left_trigger)         .set(gamepad.left_trigger);
        analogButtons.get(AnalogBindings.right_trigger)        .set(gamepad.right_trigger);
        analogButtons.get(AnalogBindings.left_stick_x)         .set(gamepad.left_stick_x);
        analogButtons.get(AnalogBindings.left_stick_y)         .set(gamepad.left_stick_y);
        analogButtons.get(AnalogBindings.right_stick_x)        .set(gamepad.right_stick_x);
        analogButtons.get(AnalogBindings.right_stick_y)        .set(gamepad.right_stick_y);
    }

    public AnalogButton get(AnalogBindings binding)
    {
        return analogButtons.get(binding);
    }

    public DigitalButton get(DigitalBindings binding)
    {
        return digitalButtons.get(binding);
    }
}
