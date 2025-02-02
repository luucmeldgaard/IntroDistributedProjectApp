package dtu.dk.introDistributedProjectApp.mvvm.start

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dtu.dk.introDistributedProjectApp.repository.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val gameRepository: GameRepository
): ViewModel() {

    init {

    }

    private val _uiModel = MutableStateFlow(StartUiModel())
    val uiModel: StateFlow<StartUiModel> = _uiModel.asStateFlow()

    fun onStartGameClicked() {
        gameRepository.join()
    }

    fun onCreateGamePressed() {
        val ipAddress = gameRepository.getLocalIpAddress() ?: "Not connected"

        _uiModel.update { currentState ->
            currentState.copy(
                joinGame = false,
                createGame = true,
                ipAddress = ipAddress
            )
        }
    }

    fun onJoinGamePressed() {

        _uiModel.update { currentState ->
            currentState.copy(
                joinGame = true,
                createGame = false
            )
        }
    }

    fun startGame(enteredName: String) {
        gameRepository.setHosting(true)
        gameRepository.joinGame(uiModel.value.ipAddress, enteredName)
    }

    fun joinGame(enteredIp: String, enteredName: String) {
        gameRepository.setHosting(false)
        gameRepository.joinGame(enteredIp, enteredName)
    }

}