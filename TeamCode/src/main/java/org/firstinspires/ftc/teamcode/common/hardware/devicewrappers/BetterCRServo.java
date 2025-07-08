package org.firstinspires.ftc.teamcode.common.hardware.devicewrappers;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.util.Range;

public class BetterCRServo implements CRServo
{
	private CRServo crservo;

	private double lastPower = 0.0;
	private double writeDelta = 0.05;

	private String crServoName;

	public BetterCRServo(String crServoName)
	{
		this.crServoName = crServoName;
	}

	public void init(HardwareMap hardwareMap)
	{
		crservo = hardwareMap.get(CRServo.class, crServoName);
	}

	public void setWriteDelta (double newWriteDelta)
	{
		writeDelta = newWriteDelta;
	}

	@Override
	public ServoController getController() { return null; }

	@Override
	public int getPortNumber() { return 15996; }

	@Override
	public void setDirection(Direction direction) {
		crservo.setDirection(direction);
	}

	@Override
	public Direction getDirection()
	{
		return crservo.getDirection();
	}

	@Override
	public void setPower(double newPower)
	{
		newPower = Range.clip(newPower, -1.0, 1.0);
		if (Math.abs(newPower - lastPower) > writeDelta || (newPower == 0.0 && lastPower != 0.0)) {
			lastPower = newPower;
			crservo.setPower(newPower);
		}
	}

	@Override
	public double getPower()
	{
		return lastPower;
	}

	@Override
	public Manufacturer getManufacturer() { return null; }

	@Override
	public String getDeviceName() { return "BrickBot"; }

	@Override
	public String getConnectionInfo() { return "BrickBot"; }

	@Override
	public int getVersion() { return 15996; }

	@Override
	public void resetDeviceConfigurationForOpMode() { }

	@Override
	public void close() { }
}
