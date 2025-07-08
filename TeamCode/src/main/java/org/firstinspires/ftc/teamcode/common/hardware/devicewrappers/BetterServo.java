package org.firstinspires.ftc.teamcode.common.hardware.devicewrappers;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.util.Range;

public class BetterServo implements Servo
{
	private Servo servo;

	private boolean isReversed = false;
	private boolean initialized = false;
	private double lastPosition = 0.0;
	private double writeDelta = 0.03;

	private String servoName;

	public BetterServo(String servoName)
	{
		this.servoName = servoName;
	}

	public void init(HardwareMap hardwareMap)
	{
		servo = hardwareMap.get(Servo.class, servoName);
	}

	public void setWriteDelta(double newWriteDelta)
	{
		writeDelta = newWriteDelta;
	}

	public void setReversed(boolean isReversed)
	{
		this.isReversed = isReversed;
	}

	@Override
	public ServoController getController()
	{
		return null;
	}

	@Override
	public int getPortNumber()
	{
		return 0;
	}

	@Override
	public void setDirection(Direction direction)
	{

	}

	@Override
	public Direction getDirection()
	{
		return null;
	}

	@Override
	public void setPosition(double newPosition)
	{
		newPosition = Range.clip(newPosition, 0.0, 1.0);
		if (Math.abs(lastPosition - newPosition) > writeDelta || !initialized) {
			initialized = true;
			servo.setPosition(isReversed ? 1.0 - newPosition : newPosition);
			lastPosition = newPosition;
		}
	}

	@Override
	public double getPosition()
	{
		return lastPosition;
	}

	@Override
	public void scaleRange(double min, double max)
	{

	}

	@Override
	public Manufacturer getManufacturer()
	{
		return null;
	}

	@Override
	public String getDeviceName()
	{
		return "BrickBot";
	}

	@Override
	public String getConnectionInfo()
	{
		return "BrickBot";
	}

	@Override
	public int getVersion()
	{
		return 15996;
	}

	@Override
	public void resetDeviceConfigurationForOpMode()
	{

	}

	@Override
	public void close()
	{

	}
}
