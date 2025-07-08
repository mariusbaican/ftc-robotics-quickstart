package org.firstinspires.ftc.teamcode.common.controltheory;

import com.qualcomm.robotcore.util.ElapsedTime;

public class MotionProfile
{
	private final double max_acceleration;
	private final double acceleration_dt;
	private final double acceleration_dist;
	private final double acceleration_end_t;

	private double max_velocity;
	private final double cruise_dt;
	private final double cruise_dist;
	private final double cruise_end_t;

	private final double max_deceleration;
	private final double deceleration_dt;
	private final double deceleration_dist;
	private final double deceleration_end_t;

	private final double startPosition;
	private final double targetPosition;
	private final double distance;

	private ElapsedTime profileTime = new ElapsedTime();

	public MotionProfile(double startPosition, double targetPosition, ProfileConstraints constraints)
	{
		this.startPosition = startPosition;
		this.targetPosition = targetPosition;
		distance = this.targetPosition - this.startPosition;

		max_acceleration = constraints.getMaxAcceleration();
		max_velocity = constraints.getMaxVelocity();
		max_deceleration = constraints.getMaxDeceleration();

		max_velocity = Math.min (max_velocity, Math.sqrt(2 * distance * max_acceleration * max_deceleration / (max_acceleration + max_deceleration)));

		acceleration_dt = max_velocity / max_acceleration;
		acceleration_dist = 0.5 * max_acceleration * acceleration_dt * acceleration_dt;

		deceleration_dt = max_velocity / max_acceleration;
		deceleration_dist = max_velocity * max_deceleration - 0.5 * max_deceleration * deceleration_dt * deceleration_dt;

		cruise_dist = distance - acceleration_dist - deceleration_dist;
		cruise_dt = cruise_dist / max_velocity;

		acceleration_end_t = acceleration_dt;
		cruise_end_t = acceleration_end_t + cruise_dt;
		deceleration_end_t = cruise_end_t + deceleration_dt;

		profileTime.reset();
	}

	public double calculate()
	{
		double elapsedTime = profileTime.seconds();
		if (elapsedTime > deceleration_end_t)
			return targetPosition;
		else if (elapsedTime < acceleration_end_t)
			return startPosition + 0.5 * max_acceleration * elapsedTime * elapsedTime;
		else if (elapsedTime < cruise_end_t)
			return startPosition + 0.5 * max_acceleration * elapsedTime * elapsedTime + max_velocity * (elapsedTime - acceleration_end_t);
		else
			return startPosition + 0.5 * max_acceleration * elapsedTime * elapsedTime + max_velocity * cruise_dist - 0.5 * max_deceleration * (elapsedTime - cruise_end_t) * (elapsedTime - cruise_end_t);
	}
}
