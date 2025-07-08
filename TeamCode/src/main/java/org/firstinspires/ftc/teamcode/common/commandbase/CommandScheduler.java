package org.firstinspires.ftc.teamcode.common.commandbase;

import org.firstinspires.ftc.teamcode.common.hardware.subsystems.Subsystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class schedules and runs multiple types of commands
 */
public class CommandScheduler
{
	private static CommandScheduler scheduler;
	private static ArrayList<Subsystem> subsystems;
	private ArrayList<Command> commands;
	private ArrayList<Command> removedCommands;

	private CommandScheduler()
	{
		this.commands = new ArrayList<>();
		this.removedCommands = new ArrayList<>();
	}

	/**
	 * This function allows you to add your subsystems that implement the Subsystem interface.
	 * The CommandScheduler then calls the periodic function of each subsystem.
	 * @param subsystems The list of subsystems your robot has.
	 */
	public void addSubsystems(List<Subsystem> subsystems)
	{
		CommandScheduler.subsystems = new ArrayList<>(subsystems);
	}

	/**
	 * This function allows you to add your subsystems that implement the Subsystem interface.
	 * The CommandScheduler then calls the periodic function of each subsystem.
	 * @param subsystem A subsystem of your bot.
	 */
	public void addSubsystems(Subsystem... subsystem)
	{
		addSubsystems(Arrays.asList(subsystem));
	}

	/**
	 * This function insures there is only one active instance of the CommandScheduler and is used
	 * to access said instance.
	 * @return The unique instance of CommandScheduler.
	 */
	public static CommandScheduler getInstance()
	{
		if (scheduler == null)
			scheduler = new CommandScheduler();
		return scheduler;
	}

	/**
	 * This function clears the list of active commands, should usually be called in init.
	 */
	public void reset()
	{
		if (commands != null)
			commands.clear();
	}

	/**
	 * This function schedules a command.
	 * @param command The desired command to be scheduled.
	 */
	public void schedule(Command command)
	{
		commands.add(command);
		if (command instanceof TimedCommand)
			((TimedCommand) command).startTimer();
	}

	/**
	 * This function removes an already scheduled command from the scheduler.
	 * @param command The desired command to be removed.
	 */
	public void remove(Command command)
	{
		commands.remove(command);
	}

	/**
	 * This function runs the list of scheduled commands.
	 */
	public void run()
	{
		if (subsystems == null || subsystems.isEmpty())
			return;
		for(Subsystem subsystem : subsystems)
			subsystem.periodic();

		if (commands.isEmpty())
			return;
		for (Command command : commands) {
			if (command.run())
				removedCommands.add(command);
		}

		if (removedCommands.isEmpty())
			return;
		for (Command command : removedCommands)
			commands.remove(command);
		removedCommands.clear();
	}
}
