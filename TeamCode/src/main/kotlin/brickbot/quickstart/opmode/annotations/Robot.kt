package brickbot.quickstart.opmode.annotations

import brickbot.quickstart.subsystems.Robot
import kotlin.reflect.KClass

annotation class Robot(val robot: KClass<out Robot>) {
}