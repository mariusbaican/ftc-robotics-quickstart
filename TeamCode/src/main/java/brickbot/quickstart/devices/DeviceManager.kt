package brickbot.quickstart.devices

object DeviceManager {
    private var deviceList = mutableListOf<BrickDevice>()

    // This technically never gets cleared so it just accepts an infinite list of devices
    // which isn't great, but it works. How many opModes are you gonna run to the point
    // where it actually matters that you never clear the devices?
    fun addDevice(device: BrickDevice) {
        deviceList.add(device)
    }

    fun initDevices() {
        deviceList.forEach { it.init() }
    }
}