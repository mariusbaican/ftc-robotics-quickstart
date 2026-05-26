package brickbot.quickstart.devices

import com.qualcomm.robotcore.hardware.HardwareMap

object DeviceManager {
    private var deviceList = mutableMapOf<String, BrickDevice>()

    /**
     * This method adds a device to the device map.
     * The DeviceManager does not allow multiple devices with the same name.
     * @param device The device to add to the map.
     */
    fun addDevice(device: BrickDevice) {
        deviceList[device.deviceName] = device
    }

    fun clear() {
        deviceList.clear()
    }

    fun initDevices(hardwareMap: HardwareMap) {
        deviceList.values.forEach { it.init(hardwareMap) }
    }
}