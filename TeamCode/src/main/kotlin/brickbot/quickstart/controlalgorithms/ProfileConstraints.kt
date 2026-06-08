package brickbot.quickstart.controlalgorithms

class ProfileConstraints @JvmOverloads constructor(
    val maxAcceleration: Double,
    val maxVelocity: Double,
    val maxDeceleration: Double = maxAcceleration
)