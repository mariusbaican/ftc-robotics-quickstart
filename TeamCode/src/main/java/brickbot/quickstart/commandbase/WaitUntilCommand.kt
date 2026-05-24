package brickbot.quickstart.commandbase

import com.qualcomm.robotcore.util.ElapsedTime
import java.util.function.BooleanSupplier

class WaitUntilCommand(
    commandName: String = "",
    private var timeoutMs: Long = Long.MAX_VALUE,
    private var condition: BooleanSupplier
): Command(commandName) {
    private lateinit var timer: ElapsedTime

    override fun run(): Boolean {
        if (!::timer.isInitialized) {
            timer = ElapsedTime()
            timer.reset()
        }

        return condition.asBoolean || (timer.milliseconds() > timeoutMs)
    }

    override fun clone(): Command {
        return WaitUntilCommand(commandName, timeoutMs, condition)
    }
}