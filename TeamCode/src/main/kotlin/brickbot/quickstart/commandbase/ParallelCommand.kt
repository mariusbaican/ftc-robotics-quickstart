package brickbot.quickstart.commandbase

import kotlin.collections.map

class ParallelCommand(
    commandName: String = "",
    private val commands: MutableList<Command>
): Command(commandName) {

    constructor(commandName: String = "", vararg commands: Command): this(commandName, commands.toMutableList())

    override fun run(): Boolean {
        if (commands.isEmpty())
            return true

        commands.removeIf { it.run() }
        return false
    }

    override fun clone(): Command {
        return ParallelCommand(
            commandName, commands.map { it.clone() }.toMutableList()
        )
    }

}