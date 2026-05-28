package brickbot.quickstart.recordautonomous

class RecordingData {
    var startTimestamp: Long = 0
    val stateList = mutableListOf<RobotState>()

    fun addState(state: RobotState) {
        state.timestamp -= startTimestamp
        stateList.add(state)
    }
}