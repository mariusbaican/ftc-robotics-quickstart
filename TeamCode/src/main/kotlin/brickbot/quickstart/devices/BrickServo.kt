package brickbot.quickstart.devices

import com.qualcomm.robotcore.hardware.HardwareDevice
import com.qualcomm.robotcore.hardware.HardwareMap
import com.qualcomm.robotcore.hardware.Servo
import com.qualcomm.robotcore.hardware.ServoController
import com.qualcomm.robotcore.util.Range
import kotlin.math.abs

class BrickServo(deviceName: String): Servo, BrickDevice(deviceName) {
    private lateinit var servo: Servo

    private var initialized = false
    private var lastPosition = 0.0
    private var writeDelta = 0.0

    override fun init(hwMap: HardwareMap) {
        servo = hwMap.get(Servo::class.java, deviceName)
    }

    fun setWriteDelta(newWriteDelta: Double) {
        writeDelta = newWriteDelta
    }

    override fun setPosition(position: Double) {
        val newPosition = Range.clip(position, 0.0, 1.0)

        if (abs(lastPosition - newPosition) > writeDelta || !initialized) {
            initialized = true
            servo.position = newPosition
            lastPosition = newPosition
        }
    }

    override fun getPosition(): Double {
        return lastPosition
    }

    override fun getController(): ServoController? {
        return servo.controller
    }

    override fun getPortNumber(): Int {
        return servo.portNumber
    }

    override fun setDirection(direction: Servo.Direction?) {
        servo.direction = direction
    }

    override fun getDirection(): Servo.Direction? {
        return servo.direction
    }

    override fun scaleRange(min: Double, max: Double) {
        servo.scaleRange(min, max)
    }

    override fun getManufacturer(): HardwareDevice.Manufacturer? {
        return servo.manufacturer
    }

    override fun getConnectionInfo(): String? {
        return servo.connectionInfo
    }

    override fun getVersion(): Int {
        return servo.version
    }

    override fun resetDeviceConfigurationForOpMode() {
        servo.resetDeviceConfigurationForOpMode()
    }

    override fun close() {
        servo.close()
    }
}