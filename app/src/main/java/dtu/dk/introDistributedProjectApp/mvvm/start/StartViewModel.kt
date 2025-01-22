package dtu.dk.introDistributedProjectApp.mvvm.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dtu.dk.introDistributedProjectApp.data.GameStateLocal
import dtu.dk.introDistributedProjectApp.repository.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val gameRepository: GameRepository
): ViewModel() {

    init {
        viewModelScope.launch {
            gameRepository.gameStateLocal.collect { gameState ->
                onGameStateUpdate(gameState)
            }
        }
    }

    private fun onGameStateUpdate(gameStateLocal: GameStateLocal) {
        if (gameStateLocal.joinError) {
            _uiModel.update { currentState ->
                currentState.copy(
                    displayJoinError = true
                )
            }
        } else {
            _uiModel.update { currentState ->
                currentState.copy(
                    displayJoinError = false
                )
            }
        }
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