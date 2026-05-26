package brickbot.quickstart.subsystems

import com.qualcomm.robotcore.hardware.HardwareMap

object SubsystemManager {
    private val subsystems = mutableMapOf<String, Subsystem>()

    /**
     * This method adds the subsystems to the subsystem map.
     * The subsystem manager does not allow multiple instances of the same subsystem class.
     * @param subsystems The subsystems to add to the map.
     */
    fun add(vararg subsystems: Subsystem) {
        for (subsystem in subsystems) {
            this.subsystems[subsystem::class.java.simpleName] = subsystem
        }
    }

    fun clear() {
        subsystems.clear()
    }

    fun init(hwMap: HardwareMap) {
        subsystems.values.forEach { it.init(hwMap) }
    }

    fun run() {
        val timestamp: Int = (System.nanoTime() * 1e-6).toInt()

        subsystems.values.forEach { it.update(timestamp) }
    }
}