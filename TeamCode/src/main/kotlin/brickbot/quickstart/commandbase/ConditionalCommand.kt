package brickbot.quickstart.commandbase

import java.util.function.BooleanSupplier

class ConditionalCommand @JvmOverloads constructor(
    commandName: String = "",
    private val booleanFunction: BooleanSupplier
): Command(commandName) {

    override fun run(): Boolean {
        return booleanFunction.asBoolean
    }

    override fun clone(): Command {
        return ConditionalCommand(commandName, booleanFunction)
    }
}
