package brickbot.quickstart.subsystems

abstract class Robot {
    abstract fun init()
    abstract fun update()

    fun autonomousInit() { }
    fun teleOpInit() { }
}