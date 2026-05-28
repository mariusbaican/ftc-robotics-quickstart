package brickbot.quickstart.commandbase

abstract class Command @JvmOverloads constructor(
    protected var commandName: String = ""
) : Cloneable {
    /**
     * Runs the command
     * @return true if the command finished, false if it needs to be run again
     */
    abstract fun run(): Boolean

    /**
     * Clones the command
     * @return the cloned command
     */
    public abstract override fun clone(): Command

    /**
     * Gets the name of the command
     * @return the name of the command
     */
    fun getCommandName(): String {
        return commandName
    }
}