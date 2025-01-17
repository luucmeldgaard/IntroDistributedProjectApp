package dtu.dk.introDistributedProjectApp.mvvm.game.round

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dtu.dk.introDistributedProjectApp.data.GameState
import dtu.dk.introDistributedProjectApp.data.GameStateLocal
import dtu.dk.introDistributedProjectApp.repository.GameRepository
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

    init {
        viewModelScope.launch {
            gameRepository.gameStateLocal.collect { gameState ->
                onGameStateUpdate(gameState)
            }
        }
    }

    private fun onGameStateUpdate(gameStateLocal: GameStateLocal) {

        _uiModel.update { currentState ->
            currentState.copy(
                currentQuestion = gameStateLocal.question,
                //correctAnswer = gameStateLocal.correctAnswer,
                //currentScore = gameStateLocal.players.find { it.id == gameStateLocal.userUUID.toString() }?.score ?: 0
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

    fun onAnswerSelected(answer: Int) {

        var selectedAnswer = answer + 1 // To account for 0-based index

        if (selectedAnswer == _uiModel.value.selectedAnswer) { // Answer already selected
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
                )
            }
        }
    }

}