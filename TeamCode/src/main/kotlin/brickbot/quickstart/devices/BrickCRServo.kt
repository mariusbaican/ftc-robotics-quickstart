package brickbot.quickstart.devices

import com.qualcomm.robotcore.hardware.CRServo
import com.qualcomm.robotcore.hardware.DcMotorSimple
import com.qualcomm.robotcore.hardware.HardwareDevice
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.ServoController
import com.qualcomm.robotcore.util.Range
import kotlin.math.abs

class BrickCRServo(deviceName: String): CRServo, BrickDevice(deviceName) {
    private lateinit var crServo: CRServo

    private var lastPower = 0.0
    private var writeDelta = 0.05

    override fun init(hwMap: HardwareMap) {
        crServo = hwMap.get(CRServo::class.java, deviceName)
    }

    fun setWriteDelta(newWriteDelta: Double) {
        writeDelta = newWriteDelta
    }

    override fun setPower(power: Double) {
        val newPower = Range.clip(power, -1.0, 1.0)

        if (abs(newPower - lastPower) > writeDelta || (newPower == 0.0 && lastPower != 0.0)) {
            lastPower = newPower
            crServo.power = newPower
        }
    }

    override fun getPower(): Double {
        return lastPower
    }

    override fun getController(): ServoController? {
        return crServo.controller
    }

    override fun getPortNumber(): Int {
        return crServo.portNumber
    }

    override fun setDirection(direction: DcMotorSimple.Direction?) {
        crServo.direction = direction
    }

    override fun getDirection(): DcMotorSimple.Direction? {
        return crServo.direction
    }

    override fun getManufacturer(): HardwareDevice.Manufacturer? {
        return crServo.manufacturer
    }

    override fun getConnectionInfo(): String? {
        return crServo.connectionInfo
    }

    override fun getVersion(): Int {
        return crServo.version
    }

    override fun resetDeviceConfigurationForOpMode() {
        crServo.resetDeviceConfigurationForOpMode()
    }

    override fun close() {
        crServo.close()
    }
}