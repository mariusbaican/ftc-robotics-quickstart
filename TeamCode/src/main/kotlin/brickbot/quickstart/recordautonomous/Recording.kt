package brickbot.quickstart.recordautonomous

class Recording(
    val startTimestamp: Long
) {
    val stateList = mutableListOf<RobotState>()

    fun addState(state: RobotState) {
        stateList.add(state)
    }
}