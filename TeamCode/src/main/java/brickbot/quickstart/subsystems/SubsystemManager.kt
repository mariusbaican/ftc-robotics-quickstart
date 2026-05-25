package brickbot.quickstart.subsystems

import com.qualcomm.robotcore.hardware.HardwareMap

object SubsystemManager {
    private val subsystems = mutableListOf<Subsystem>()

    fun add(vararg subsystems: Subsystem) {
        this.subsystems.addAll(subsystems)
    }

    fun clear() {
        subsystems.clear()
    }

    fun init(hwMap: HardwareMap) {
        subsystems.forEach { it.init(hwMap) }
    }

    fun run() {
        val timestamp: Int = (System.nanoTime() * 1e-6).toInt()

        subsystems.forEach { it.update(timestamp) }
    }
}