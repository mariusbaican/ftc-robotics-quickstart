package brickbot.quickstart.commandbase

import com.qualcomm.robotcore.util.ElapsedTime
import java.util.function.BooleanSupplier

class TimedCommand(
    commandName: String = "",
    private val timeoutMs: Long,
    private val function: BooleanSupplier
): Command(commandName) {
    private lateinit var timer: ElapsedTime

    constructor(commandName: String = "", timeoutMs: Long, function: Runnable): this(
        commandName,
        timeoutMs,
        BooleanSupplier {
            function.run()
            false
        }
    )

    override fun run(): Boolean {
        if (!::timer.isInitialized) {
            timer = ElapsedTime()
            timer.reset()
        }

        return function.asBoolean || (timer.milliseconds() > timeoutMs)
    }

    override fun clone(): Command {
        return TimedCommand(commandName, timeoutMs, function)
    }
}