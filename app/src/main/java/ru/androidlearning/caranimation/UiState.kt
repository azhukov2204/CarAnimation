package ru.androidlearning.caranimation

data class UiState(
    val currentPosition: Position = Position.TOP_LEFT,
    val nextPosition: Position = Position.TOP_RIGHT,
    val isActionInProgress: Boolean = false
) {

    enum class Position {
        TOP_LEFT,
        TOP_RIGHT,
        BOTTOM_RIGHT,
        BOTTOM_LEFT
    }
}
