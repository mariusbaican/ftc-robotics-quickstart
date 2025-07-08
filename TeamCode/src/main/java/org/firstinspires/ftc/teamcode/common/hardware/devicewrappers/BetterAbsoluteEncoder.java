package org.firstinspires.ftc.teamcode.common.hardware.devicewrappers;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class BetterAbsoluteEncoder implements HardwareDevice
{
	private AnalogInput absoluteEncoder;

	private boolean wraparound = false;
	private boolean inverted = false;
	private double offset = 0.0;
	private double encoderRange = 3.3;

	private String absoluteEncoderName;

	public BetterAbsoluteEncoder(String absoluteEncoderName)
	{
		this.absoluteEncoderName = absoluteEncoderName;
	}

	public void init(HardwareMap hardwareMap)
	{
		absoluteEncoder = hardwareMap.get(AnalogInput.class, absoluteEncoderName);
	}

	public void setInverted(boolean inverted)
	{
		this.inverted = inverted;
	}

	public void setWraparound(boolean wraparound)
	{
		this.wraparound = wraparound;
	}

	public void setOffset(double offset)
	{
		this.offset = offset;
	}

	public double getCurrentPosition()
	{
		double voltage = absoluteEncoder.getVoltage();
		double position = (inverted ? encoderRange - voltage - offset : voltage - offset);

		if (position < 0.0)
			position += encoderRange;
		if (position > 3.3)
			position -= encoderRange;
		return position;
	}

	@Override
	public Manufacturer getManufacturer() { return null; }

	@Override
	public String getDeviceName() { return "BrickBot"; }

	@Override
	public String getConnectionInfo() { return "BrickBot"; }

	@Override
	public int getVersion() { return 15996;	}

	@Override
	public void resetDeviceConfigurationForOpMode() { }

	@Override
	public void close() { }
}
