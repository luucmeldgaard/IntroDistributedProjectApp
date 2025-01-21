package dtu.dk.introDistributedProjectApp.mvvm.game.round

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dtu.dk.introDistributedProjectApp.data.GameState
import dtu.dk.introDistributedProjectApp.data.GameStateLocal
import dtu.dk.introDistributedProjectApp.data.Player
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
                    currentQuestion = gameStateLocal.question,
                    currentState = GameState.ANSWERING,

                )
            }
        } else if (gameStateLocal.state == GameState.SHOWING) {
            //Log.i("RoundViewModel", "I ran 2")
            // TODO: disable buttons
            clear()
            _uiModel.update { currentState ->
                currentState.copy(
                    correctAnswer = gameStateLocal.correctAnswer,
                    currentState = GameState.SHOWING,
                    //currentScore = gameStateLocal.players.find { it.id == gameStateLocal.userUUID.toString() }?.score ?: 0
                )
            }
        } else if (gameStateLocal.state == GameState.FINAL) {
            Log.w("onGameStateUpdate", "Shouldn't something happen here?")
            clear()
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

        /*if (gameStateLocal.state != currentState) {
            if (currentState == GameState.SHOWING && gameStateLocal.state == GameState.ANSWERING) {
                clear()
            }
        }

        for (player in gameStateLocal.players) {
            if (player.id == gameStateLocal.userUUID.toString()) {
                _uiModel.update { currentState ->
                    currentState.copy(
                        currentScore = player.score
                    )
                }
            }
        }

        for (i in 0 until gameStateLocal.question.answers.size) {
            if (gameStateLocal.correctAnswer == uiModel.value.currentQuestion.answers[i]) {
                _uiModel.update { currentState ->
                    currentState.copy(
                        correctAnswer = i + 1
                    )
                }
            }
        }

        _uiModel.update { currentState ->
            currentState.copy(
                currentQuestion = gameStateLocal.question
            )
        }*/
    }

    private fun runTimer() {
        previousTimer?.cancel()
        previousTimer = viewModelScope.launch {
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

    fun onAnswerSelected(answer: Int) {

        var selectedAnswer = answer + 1 // To account for 0-based index
        Log.i("onAnswerSelected", "onAnswerSelected has been called")

        if (selectedAnswer == _uiModel.value.selectedAnswer) { // Answer already selected TODO: Hvorfor kan man de-selecte et svar? Det virker lidt kogt når svaret bliver sendt med det samme
            selectedAnswer = 0 // deselects all by setting to 0
        }

        //gameRepository.sendAndReadyForNextQuestion(selectedAnswer)

        viewModelScope.launch {
            _uiModel.update { currentState ->
                currentState.copy(
                    selectedAnswer = selectedAnswer
                )
            }
        }
        viewModelScope.launch {
            gameRepository.sendAnswer(uiModel.value.currentQuestion.answers[selectedAnswer - 1])
        }
    }

    fun onTimerFinished() {
        viewModelScope.launch {
            //gameRepository.sendAndReadyForNextQuestion(selectedAnswer)
        }
    }

    fun clear() {
        viewModelScope.launch {
            _uiModel.update { currentState ->
                currentState.copy(
                    selectedAnswer = 0,
                    secondsLeft = 30,
                    correctAnswer = 0,
                )
            }
        }
    }

}