package brickbot.quickstart.devices

import com.qualcomm.robotcore.hardware.DcMotor
import com.qualcomm.robotcore.hardware.DcMotorEx
import com.qualcomm.robotcore.hardware.HardwareDevice
import com.qualcomm.robotcore.hardware.HardwareMap

class BrickEncoder(deviceName: String): HardwareDevice, BrickDevice(deviceName) {
    private lateinit var motor: DcMotorEx

    override fun init(hwMap: HardwareMap) {
        motor = hwMap.get(DcMotorEx::class.java, deviceName)
    }

    fun getCurrentPosition(): Int {
        return motor.currentPosition
    }

    fun reset() {
        motor.mode = DcMotor.RunMode.STOP_AND_RESET_ENCODER
        motor.mode = DcMotor.RunMode.RUN_WITHOUT_ENCODER
    }

    override fun getManufacturer(): HardwareDevice.Manufacturer? {
        return motor.manufacturer
    }

    override fun getConnectionInfo(): String? {
        return motor.connectionInfo
    }

    override fun getVersion(): Int {
        return motor.version
    }

    override fun resetDeviceConfigurationForOpMode() {
        motor.resetDeviceConfigurationForOpMode()
    }

    override fun close() {
        motor.close()
    }
}