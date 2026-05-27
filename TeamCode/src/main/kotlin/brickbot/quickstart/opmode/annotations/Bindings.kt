package brickbot.quickstart.opmode.annotations

import brickbot.quickstart.recordautonomous.Bindings
import kotlin.reflect.KClass

annotation class Bindings(
    val bindings: KClass<out Bindings>
)