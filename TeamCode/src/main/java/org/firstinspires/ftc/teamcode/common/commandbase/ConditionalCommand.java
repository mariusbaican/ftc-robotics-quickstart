package org.firstinspires.ftc.teamcode.common.commandbase;

/**
 * Usage:
 * If using a boolean returning function:
 * new ConditionalCommand(() -> {return class.method();});
 */
public class ConditionalCommand implements Command
{
    private BooleanFunction function;

    public ConditionalCommand (BooleanFunction function) {
        this.function = function;
    }

    @Override
    public boolean run() {
        return function.run();
    }
}