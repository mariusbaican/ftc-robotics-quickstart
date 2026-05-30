package brickbot.quickstart.opmode.annotations

import brickbot.quickstart.follower.Localizer
import kotlin.reflect.KClass

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class Localizer(
    val localizer: KClass<out Localizer>
)