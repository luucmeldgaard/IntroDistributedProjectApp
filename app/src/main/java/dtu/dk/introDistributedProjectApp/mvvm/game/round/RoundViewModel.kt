package dtu.dk.introDistributedProjectApp.mvvm.game.round

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dtu.dk.introDistributedProjectApp.data.GameState
import dtu.dk.introDistributedProjectApp.data.GameStateLocal
import dtu.dk.introDistributedProjectApp.data.Player
import dtu.dk.introDistributedProjectApp.mvvm.start.StartUiModel
import dtu.dk.introDistributedProjectApp.repository.GameRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoundViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _uiModel = MutableStateFlow(RoundUiModel())
    val uiModel: StateFlow<RoundUiModel> = _uiModel.asStateFlow()
    private var previousTimer: Job? = null;

    init {
        viewModelScope.launch {
            gameRepository.gameStateLocal.collect { gameState ->
                onGameStateUpdate(gameState)
            }
        }
    }

    private fun onGameStateUpdate(gameStateLocal: GameStateLocal) {
        Log.i("RoundViewModel", "GameState: ${gameStateLocal.state}")

        if (gameStateLocal.state == GameState.ANSWERING) {
            runTimer()
            //Log.i("RoundViewModel", "I ran 1")
            // TODO: enable buttons
            _uiModel.update { currentState ->
                currentState.copy(
                    hasAnswered = false,
                    currentQuestion = gameStateLocal.question,
                    currentState = GameState.ANSWERING,
                    correctAnswer = gameStateLocal.correctAnswer,
                    hosting = gameStateLocal.host,
                )
            }
        } else if (gameStateLocal.state == GameState.SHOWING) {
            //Log.i("RoundViewModel", "I ran 2")
            // TODO: disable buttons
            //clear()
            _uiModel.update { currentState ->
                currentState.copy(
                    correctAnswer = gameStateLocal.correctAnswer,
                    currentState = GameState.SHOWING,
                    hosting = gameStateLocal.host,
                    //currentScore = gameStateLocal.players.find { it.id == gameStateLocal.userUUID.toString() }?.score ?: 0
                )
            }
        } else if (gameStateLocal.state == GameState.FINAL) {
            Log.w("onGameStateUpdate", "Shouldn't something happen here?")
            //clear()
        }

        _uiModel.update { currentState ->
            currentState.copy(
                player = gameRepository.getLocalPlayer() ?: Player(
                    id = "9987",
                    name = "TORBEN",
                    score = 99
                )
            )
        }
    }

    private fun runTimer() {
        previousTimer = viewModelScope.launch {
            previousTimer?.cancel()
            for (i in 30 downTo 0) {
                if(_uiModel.value.currentState != GameState.ANSWERING){
                    _uiModel.update { currentState ->
                        currentState.copy(
                            secondsLeft = 30
                        )
                    }
                    break;
                }
                _uiModel.update { currentState ->
                    currentState.copy(
                        secondsLeft = i
                    )
                }
                kotlinx.coroutines.delay(1000)
            }

        }
    }

    fun onAnswerSelected(selectedAnswer: Int) {

        Log.i("onAnswerSelected", "onAnswerSelected has been called")

        _uiModel.update { currentState ->
            currentState.copy(
                hasAnswered = true
                //selectedAnswer = selectedAnswer
            )
        }
        viewModelScope.launch {
            gameRepository.sendAnswer(uiModel.value.currentQuestion.answers[selectedAnswer])
        }
    }

    fun clear() {
        viewModelScope.launch {
            _uiModel.update { currentState ->
                currentState.copy(
                    selectedAnswer = -1,
                    secondsLeft = 30,
                    correctAnswer = -1,
                )
            }
        }
    }

}