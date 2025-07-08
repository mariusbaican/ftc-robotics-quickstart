package org.firstinspires.ftc.teamcode.common.hardware.devicewrappers;

import androidx.annotation.NonNull;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDCoefficients;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

public class BetterMotor implements DcMotorEx
{
	private DcMotorEx motor;

	private double writeDelta = 0.05;
	private double lastPower = 0.0;

	private String motorName;

	public BetterMotor(String motorName)
	{
		this.motorName = motorName;
	}

	public void init(HardwareMap hardwareMap)
	{
		motor = hardwareMap.get(DcMotorEx.class, motorName);
	}

	public void setWriteDelta (double newWriteDelta)
	{
		writeDelta = newWriteDelta;
	}

	@Override
	public void setPower(double newPower)
	{
		newPower = Range.clip(newPower, -1.0, 1.0);
		if (Math.abs(newPower - lastPower) > writeDelta || (newPower == 0.0 && lastPower != 0.0)) {
			lastPower = newPower;
			motor.setPower(newPower);
		}
	}

	@Override
	public int getCurrentPosition()
	{
		return motor.getCurrentPosition();
	}

	@Override
	public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior newBehaviour)
	{
		motor.setZeroPowerBehavior(newBehaviour);
	}

	@Override
	public void setDirection(DcMotorSimple.Direction newDirection)
	{
		motor.setDirection(newDirection);
	}

	@Override
	public double getPower() {
		return lastPower;
	}

	@Override
	public MotorConfigurationType getMotorType() {
		return null;
	}

	@Override
	public void setMotorType(MotorConfigurationType motorType) {}

	@Override
	public DcMotorController getController() { return null; }

	@Override
	public int getPortNumber() { return 15996; }

	@Override
	public ZeroPowerBehavior getZeroPowerBehavior() { return null; }

	@Override
	public void setPowerFloat() {}

	@Override
	public boolean getPowerFloat() { return false; }

	@Override
	public void setTargetPosition(int position) {}

	@Override
	public int getTargetPosition() { return 15996; }

	@Override
	public boolean isBusy() { return false; }

	@Override
	public void setMode(DcMotor.RunMode newMode) { motor.setMode(newMode); }

	@Override
	public RunMode getMode() { return null; }

	@Override
	public Direction getDirection() { return null; }

	@Override
	public Manufacturer getManufacturer() { return null; }

	@Override
	public String getDeviceName() { return "BrickBot"; }

	@Override
	public String getConnectionInfo() { return "BrickBot"; }

	@Override
	public int getVersion() { return 15996; }

	@Override
	public void resetDeviceConfigurationForOpMode() {}

	@Override
	public void close() {}

	@Override
	public void setMotorEnable() {

	}

	@Override
	public void setMotorDisable() {

	}

	@Override
	public boolean isMotorEnabled() {
		return false;
	}

	@Override
	public void setVelocity(double angularRate) {

	}

	@Override
	public void setVelocity(double angularRate, AngleUnit unit) {

	}

	@Override
	public double getVelocity() {
		return 0;
	}

	@Override
	public double getVelocity(AngleUnit unit) {
		return 0;
	}

	@Override
	public void setPIDCoefficients(RunMode mode, PIDCoefficients pidCoefficients) {

	}

	@Override
	public void setPIDFCoefficients(RunMode mode, PIDFCoefficients pidfCoefficients) throws UnsupportedOperationException {

	}

	@Override
	public void setVelocityPIDFCoefficients(double p, double i, double d, double f) {

	}

	@Override
	public void setPositionPIDFCoefficients(double p) {

	}

	@Override
	public PIDCoefficients getPIDCoefficients(RunMode mode) {
		return null;
	}

	@Override
	public PIDFCoefficients getPIDFCoefficients(RunMode mode) {
		return null;
	}

	@Override
	public void setTargetPositionTolerance(int tolerance) {

	}

	@Override
	public int getTargetPositionTolerance() {
		return 0;
	}

	@Override
	public double getCurrent(CurrentUnit unit) {
		return 0;
	}

	@Override
	public double getCurrentAlert(CurrentUnit unit) {
		return motor.getCurrentAlert(CurrentUnit.AMPS);
	}

	@Override
	public void setCurrentAlert(double current, CurrentUnit unit) {
		motor.setCurrentAlert(current, CurrentUnit.AMPS);
	}

	@Override
	public boolean isOverCurrent() {
		return motor.isOverCurrent();
	}
}
