package org.firstinspires.ftc.teamcode.common.controltheory;

public class ProfileConstraints
{
	private double max_acceleration;
	private double max_velocity;
	private double max_deceleration;

	public ProfileConstraints(double max_acceleration, double max_velocity, double max_deceleration)
	{
		this.max_acceleration = max_acceleration;
		this.max_velocity = max_velocity;
		this.max_deceleration = max_deceleration;
	}

	public ProfileConstraints(double max_acceleration, double max_velocity)
	{
		this(max_acceleration, max_velocity, max_acceleration);
	}

	public double getMaxAcceleration()
	{
		return max_acceleration;
	}

	public double getMaxVelocity()
	{
		return max_velocity;
	}

	public double getMaxDeceleration()
	{
		return max_deceleration;
	}
}
