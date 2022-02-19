package ru.androidlearning.caranimation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    private val liveData: MutableLiveData<UiState> by lazy { MutableLiveData() }
    private var uiState: UiState = UiState()

    fun getLiveData(): LiveData<UiState> {
        return liveData
    }

    fun handleOnCarClick() {
        if (!uiState.isActionInProgress) {
            uiState = getNewPositionUiState()
            liveData.postValue(uiState)
        }
    }

    fun onAnimationFinished() {
        uiState = uiState.copy(
            isActionInProgress = false,
            currentPosition = uiState.nextPosition
        )
    }

    private fun getNewPositionUiState(): UiState {
        return if (uiState.isActionInProgress) uiState
        else uiState.copy(
            isActionInProgress = true,
            nextPosition = getNextPosition()
        )
    }

    private fun getNextPosition(): UiState.Position {
        return when (uiState.currentPosition) {
            UiState.Position.TOP_LEFT -> UiState.Position.TOP_RIGHT
            UiState.Position.TOP_RIGHT -> UiState.Position.BOTTOM_RIGHT
            UiState.Position.BOTTOM_RIGHT -> UiState.Position.BOTTOM_LEFT
            UiState.Position.BOTTOM_LEFT -> UiState.Position.TOP_LEFT
        }
    }
}
