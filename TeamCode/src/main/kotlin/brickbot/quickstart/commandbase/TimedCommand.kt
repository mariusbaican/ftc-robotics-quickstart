package brickbot.quickstart.commandbase

import com.qualcomm.robotcore.util.ElapsedTime
import java.util.function.BooleanSupplier

class TimedCommand @JvmOverloads constructor(
    commandName: String = "",
    private val function: BooleanSupplier,
    private val timeoutMs: Long
): Command(commandName) {
    private lateinit var timer: ElapsedTime

    @JvmOverloads
    constructor(commandName: String = "", function: Runnable, timeoutMs: Long): this(
        commandName,
        BooleanSupplier {
            function.run()
            false
        },
        timeoutMs
    )

    override fun run(): Boolean {
        if (!::timer.isInitialized) {
            timer = ElapsedTime()
            timer.reset()
        }

        return function.asBoolean || (timer.milliseconds() > timeoutMs)
    }

    override fun clone(): Command {
        return TimedCommand(commandName, function, timeoutMs)
    }
}