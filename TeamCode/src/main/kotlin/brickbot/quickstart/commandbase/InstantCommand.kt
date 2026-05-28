package brickbot.quickstart.commandbase

class InstantCommand @JvmOverloads constructor(
    commandName: String = "",
    private val unitFunction: Runnable
): Command(commandName) {

    override fun run(): Boolean {
        unitFunction.run()
        return true;
    }

    override fun clone(): Command {
        return InstantCommand(commandName, unitFunction)
    }
}