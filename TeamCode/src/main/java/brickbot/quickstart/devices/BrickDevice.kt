package brickbot.quickstart.devices

import com.qualcomm.robotcore.hardware.HardwareDevice

interface BrickDevice: HardwareDevice {
    fun init()
}