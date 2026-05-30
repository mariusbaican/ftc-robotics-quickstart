package brickbot.quickstart.opmode.annotations

import brickbot.quickstart.subsystems.Robot
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class Robot(val robot: KClass<out Robot>)