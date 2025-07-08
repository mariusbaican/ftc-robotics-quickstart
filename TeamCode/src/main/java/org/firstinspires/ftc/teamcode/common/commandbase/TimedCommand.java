package org.firstinspires.ftc.teamcode.common.commandbase;

import static org.json.JSONObject.NULL;

import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Usage:
 * Allows both boolean and void functions:
 * new TimedCommand(() -> {return class.method();}, timeoutSeconds);
 */
public class TimedCommand implements Command
{
    private ElapsedTime timer = new ElapsedTime();
    private double timeoutSeconds;
    private BooleanFunction booleanFunction;
    private VoidFunction voidFunction;

    public TimedCommand (BooleanFunction booleanFunction, double timeoutSeconds)
    {
        this.booleanFunction = booleanFunction;
        this.timeoutSeconds = timeoutSeconds;
    }

    public TimedCommand(VoidFunction voidFunction, double timeoutSeconds)
    {
        this.voidFunction = voidFunction;
        this.timeoutSeconds = timeoutSeconds;
    }

    @Override
    public boolean run() {
        if (booleanFunction != NULL)
            booleanFunction.run();
        else
            voidFunction.run();

        if (timer.seconds() > timeoutSeconds)
            return true;
        else
            return false;
    }

    public void startTimer()
    {
        timer.reset();
    }
}
