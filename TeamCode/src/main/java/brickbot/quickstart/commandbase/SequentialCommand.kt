package brickbot.quickstart.commandbase

import kotlin.collections.map

class SequentialCommand(
    commandName: String = "",
    private val commands: MutableList<Command>
): Command(commandName) {

    constructor(commandName: String = "", vararg commands: Command): this(commandName, commands.toMutableList())

    override fun run(): Boolean {
        if (commands.isEmpty())
            return true

        if (commands.first().run())
            commands.removeAt(0)

        return false
    }

    override fun clone(): Command {
        return ParallelCommand(
            commandName, commands.map { it.clone() }.toMutableList()
        )
    }

}