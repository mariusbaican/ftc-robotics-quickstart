package brickbot.quickstart.opmode.annotations

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class Recording(
    val filename: String = ""
)