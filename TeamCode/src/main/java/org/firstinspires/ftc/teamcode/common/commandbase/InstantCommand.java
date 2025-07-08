package org.firstinspires.ftc.teamcode.common.commandbase;

/**
 * Usage:
 * new InstantCommand(() -> {class.method();});
 */
public class InstantCommand implements Command
{
	private VoidFunction voidFunction;

	public InstantCommand (VoidFunction voidFunction)
	{
		this.voidFunction = voidFunction;
	}

	@Override
	public boolean run()
	{
		voidFunction.run();
		return true;
	}
}
