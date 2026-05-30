package brickbot.quickstart.follower

interface Localizer {
    fun getPosition(): Pose
    fun getXVelocity(): Double
    fun getYVelocity(): Double
    fun getAngularVelocity(): Double
}