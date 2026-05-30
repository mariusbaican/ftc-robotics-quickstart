package brickbot.quickstart.follower

import brickbot.quickstart.updatable.Updatable
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver
import com.qualcomm.robotcore.hardware.HardwareMap
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D
import org.firstinspires.ftc.robotcore.external.navigation.UnnormalizedAngleUnit

class PinpointLocalizer: Updatable, Localizer {
    private lateinit var pinpoint: GoBildaPinpointDriver
    private lateinit var hardwareMap: HardwareMap

    fun setHardwareMap(hwMap: HardwareMap): PinpointLocalizer {
        hardwareMap = hwMap
        return this
    }

    fun setOffsets(xOffset: Double, yOffset: Double, distanceUnit: DistanceUnit) {
        pinpoint.setOffsets(xOffset, yOffset, distanceUnit)
    }

    fun reset() {
        pinpoint.resetPosAndIMU()
    }

    fun setPosition(distanceUnit: DistanceUnit, angleUnit: AngleUnit, x: Double, y: Double, heading: Double) {
        pinpoint.position = Pose2D(distanceUnit, x, y, angleUnit, heading)
    }

    override fun getPosition(): Pose {
        return Pose(pinpoint.position)
    }

    override fun getXVelocity(): Double {
        return pinpoint.getVelX(DistanceUnit.CM)
    }

    override fun getYVelocity(): Double {
        return pinpoint.getVelY(DistanceUnit.CM)
    }

    override fun getAngularVelocity(): Double {
        return pinpoint.getHeadingVelocity(UnnormalizedAngleUnit.DEGREES)
    }

    @JvmOverloads
    fun getX(distanceUnit: DistanceUnit = DistanceUnit.CM): Double {
        return pinpoint.getPosX(distanceUnit)
    }

    @JvmOverloads
    fun getY(distanceUnit: DistanceUnit = DistanceUnit.CM): Double {
        return pinpoint.getPosY(distanceUnit)
    }

    @JvmOverloads
    fun getHeading(unnormalizedAngleUnit: UnnormalizedAngleUnit = UnnormalizedAngleUnit.DEGREES): Double {
        return pinpoint.getHeading(unnormalizedAngleUnit)
    }

    fun setEncoderDirection(directionStrafe: GoBildaPinpointDriver.EncoderDirection, directionForward: GoBildaPinpointDriver.EncoderDirection) {
        pinpoint.setEncoderDirections(directionStrafe, directionForward)
    }

    fun setEncoderOffsets(offsetStrafe: Double, offsetForward: Double, distanceUnit: DistanceUnit) {
        pinpoint.setOffsets(offsetStrafe, offsetForward, distanceUnit)
    }

    fun init() {
        pinpoint = hardwareMap.get(GoBildaPinpointDriver::class.java, "pinpoint")
        pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
    }

    override fun update() {
        pinpoint.update()
    }


}