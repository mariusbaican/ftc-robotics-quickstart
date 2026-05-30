package brickbot.quickstart.devices

import com.qualcomm.robotcore.hardware.HardwareDevice
import com.qualcomm.robotcore.hardware.HardwareMap

abstract class BrickDevice(private val deviceName: String): HardwareDevice {
    init {
        DeviceManager.addDevice(this)
    }

    abstract fun init(hwMap: HardwareMap)

    /**
     * @return A string containing the user-attributed device name.
     */
    override fun getDeviceName(): String {
        return deviceName
    }
}