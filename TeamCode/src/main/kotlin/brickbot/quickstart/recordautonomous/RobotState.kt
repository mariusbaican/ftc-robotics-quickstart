package brickbot.quickstart.recordautonomous

class RobotState(
    var timestamp: Long,
    var gamepad1State: ByteArray,
    var gamepad2State: ByteArray
    // TODO: ADD POSE STORING
)