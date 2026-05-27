package brickbot.quickstart.opmode.annotations

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class Playback(
    val filename: String = ""
)