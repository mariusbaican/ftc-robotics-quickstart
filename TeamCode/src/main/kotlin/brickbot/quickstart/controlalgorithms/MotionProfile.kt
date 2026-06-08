package brickbot.quickstart.controlalgorithms

import brickbot.quickstart.updatable.Updatable
import com.qualcomm.robotcore.util.ElapsedTime
import org.firstinspires.ftc.robotcore.external.Telemetry
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign
import kotlin.math.sqrt

// TODO: Add a Desmos link that shows how a motion profile works
/**
 * Courtesy of team 19043 Cyliis :)
 */
class MotionProfile @JvmOverloads constructor(
    private val maxAcceleration: Double,
    private val maxVelocity: Double,
    private val maxDeceleration: Double = maxAcceleration
): Updatable {
    private var initialVelocity = 0.0
    private var initialPosition = 0.0
    private var finalPosition = 0.0

    private var deltaPose = 0.0
    private var sign = 1.0
    private var maxReachedVelocity = 0.0

    private var t0 = 0.0
    private var t1 = 0.0
    private var t2 = 0.0
    private var t3 = 0.0
    private var t = 0.0

    private var v0 = 0.0

    private var position = 0.0
    private var velocity = 0.0
    private var signedVelocity = 0.0

    val timer = ElapsedTime()

    init {
        setMotion(0.0, 0.0, 0.0)
    }

    constructor(profileConstraints: ProfileConstraints):
            this(profileConstraints.maxAcceleration, profileConstraints.maxVelocity, profileConstraints.maxDeceleration)

    fun setMotion(initialPosition: Double, finalPosition: Double, initialVelocity: Double) {
        this.initialPosition = initialPosition
        this.finalPosition = finalPosition
        this.initialVelocity = sign(initialVelocity) * min(abs(initialVelocity), maxVelocity)

        sign = sign(finalPosition - initialPosition)

        v0 = sign * initialVelocity
        t0 = v0 / maxAcceleration

        deltaPose = t0 * v0 / 2.0 + abs(finalPosition - initialPosition)

        maxReachedVelocity = max(
            if (calculateDeltaIfMaxReachedVelocityIs(maxVelocity) - deltaPose <= 0)
                maxVelocity
            else
                sqrt(deltaPose * 2.0 * maxAcceleration * maxDeceleration / (maxAcceleration + maxDeceleration)),
            v0
        )

        t1 = maxReachedVelocity / maxAcceleration - t0
        t3 = maxReachedVelocity / maxDeceleration
        t2 = abs(min(0.0, calculateDeltaIfMaxReachedVelocityIs(maxVelocity) - deltaPose)) / maxVelocity
        t = t1 + t2 + t3
        timer.reset()
    }

    private fun calculateDeltaIfMaxReachedVelocityIs(v: Double): Double {
        return (v * v / 2.0) * (maxAcceleration + maxDeceleration) / (maxAcceleration * maxDeceleration)
    }

    fun getPhase(): Int {
        if (timer.seconds() <= t1) {
            return 1
        }
        if (timer.seconds() <= t1 + t2) {
            return 2
        }
        if (timer.seconds() <= t1 + t2 + t3) {
            return 3
        }
        return 0
    }

    private fun getVelocity(phase: Int, time: Double): Double {
        when (phase) {
            1 -> return v0 + time * maxAcceleration
            2 -> return getVelocity(1, t1)
            3 -> return maxReachedVelocity - maxDeceleration * (time - t1 - t2)
            else -> return 0.0
        }
    }

    fun getPosition(time: Double): Double {
        if (time <= t1) {
            return initialPosition + sign * v0 * time / 2.0 + sign * getVelocity(1, time) * time / 2.0
        }
        if (time <= t1 + t2) {
            return initialPosition + sign * v0 * t1 / 2.0 + sign * getVelocity(1, t1) * t1 / 2.0 + sign * getVelocity(2, time) * (time - t1)
        }
        if (time <= t1 + t2 + t3) {
            return getPosition(t1 + t2) + sign * maxReachedVelocity * t3 / 2.0 - sign * getVelocity(3, time) * (t1 + t2 + t3 - time) / 2.0
        }
        return finalPosition
    }

    private fun updateVelocity() {
        velocity = getVelocity(getPhase(), timer.seconds())
    }

    private fun updateSignedVelocity() {
        signedVelocity = sign * getVelocity(getPhase(), timer.seconds())
    }

    private fun updatePosition() {
        position = getPosition(timer.seconds())
    }

    fun getVelocity(): Double {
        return velocity
    }

    fun getSignedVelocity(): Double {
        return signedVelocity
    }

    fun getPosition(): Double {
        return position
    }

    fun getTimeToMotionEnd(): Double {
        return max(0.0, t - timer.seconds())
    }

    private fun getTime1(position: Double): Double {
        return (-sign * v0 * sign(v0) * sqrt(v0 * v0 - 2 * sign * maxAcceleration * (initialPosition - position))) / (sign * maxAcceleration)
    }

    private fun getTime2(position: Double): Double {
        return (position - getPosition(t1)) / (sign * (v0 + t1 * maxAcceleration)) + t1
    }

    private fun getTime3(position: Double): Double {
        val qa = -maxDeceleration
        val qb = maxDeceleration * (t + t1 + t2) + maxReachedVelocity
        val qc = (2.0 / sign) * (getPosition(t1 + t2) - position)

        return (-qb + sqrt(qb + qb - 4.0 * qa * qc)) / (2.0 * qa)
    }

    private fun getTime(position: Double): Double {
        if (position >= min(getPosition(t1), getPosition(t1 + t2)) && position <= max(getPosition(t1), getPosition(t1 + t2))) {
            return getTime2(position)
        }

        var s = getTime1(position)

        if (s < 0.0) {
            s = 0.0
        }
        if (s > t1) {
            s = getTime3(position)

            if (s > t1 + t2 + t3) {
                s = t1 + t2 + t3
            }
        }

        return s
    }

    fun getTimeTo(position: Double): Double {
        return max(0.0, getTime(position) - timer.seconds())
    }

    fun telemetry(telemetry: Telemetry) {
        telemetry.addData("Profile initial position", initialPosition)
        telemetry.addData("Profile final position", finalPosition)
        telemetry.addData("Profile initial velocity", initialVelocity)
        telemetry.addData("Profile current velocity", velocity)
        telemetry.addData("Plateau distance", calculateDeltaIfMaxReachedVelocityIs(maxVelocity))
        telemetry.addData("Max Velocity", maxVelocity)
        telemetry.addData("Max reached Velocity", maxReachedVelocity)
        telemetry.addData("Acceleration", maxAcceleration)
        telemetry.addData("Deceleration", maxDeceleration)
        telemetry.addData("Delta pose", deltaPose)
        telemetry.addData("t1", t1)
        telemetry.addData("t2", t2)
        telemetry.addData("t3", t3)
        telemetry.addData("Phase", getPhase())
        telemetry.addData("Time to motion end", getTimeToMotionEnd())
    }

    override fun update() {
        updateVelocity()
        updateSignedVelocity()
        updatePosition()
    }
}