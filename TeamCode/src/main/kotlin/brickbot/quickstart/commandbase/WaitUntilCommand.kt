package brickbot.quickstart.commandbase

import com.qualcomm.robotcore.util.ElapsedTime
import java.util.function.BooleanSupplier

class WaitUntilCommand @JvmOverloads constructor(
    commandName: String = "",
    private var condition: BooleanSupplier,
    private var timeoutMs: Long = Long.MAX_VALUE
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
        return WaitUntilCommand(commandName, condition, timeoutMs)
    }
}