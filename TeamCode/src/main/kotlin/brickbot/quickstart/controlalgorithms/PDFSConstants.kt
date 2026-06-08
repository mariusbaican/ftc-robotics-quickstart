package brickbot.quickstart.controlalgorithms

class PDFSConstants(
    var kP: Double,
    var kD: Double,
    var minKF: Double,
    var maxKF: Double,
    var kStatic: Double
) {
    constructor(kP: Double, kD: Double, kF: Double, kStatic: Double): this(kP, kD, kF, kF, kStatic)
}