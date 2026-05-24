package brickbot.quickstart.commandbase

import com.qualcomm.robotcore.util.ElapsedTime

class SleepCommand(
    commandName: String = "",
    private val timeoutMs: Long
): Command(commandName) {
    private lateinit var timer: ElapsedTime

    override fun run(): Boolean {
        if (!::timer.isInitialized) {
            timer = ElapsedTime()
            timer.reset()
        }

        return timer.milliseconds() > timeoutMs
    }

    override fun clone(): Command {
        return SleepCommand(commandName, timeoutMs)
    }
}