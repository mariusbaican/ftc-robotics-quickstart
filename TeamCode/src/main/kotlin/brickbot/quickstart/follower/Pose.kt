package brickbot.quickstart.follower

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D

class Pose(
    var x: Double,
    var y: Double,
    var heading: Double
) {
    constructor(pose2D: Pose2D): this(pose2D.getX(DistanceUnit.CM), pose2D.getY(DistanceUnit.CM), pose2D.getHeading(AngleUnit.DEGREES))
}