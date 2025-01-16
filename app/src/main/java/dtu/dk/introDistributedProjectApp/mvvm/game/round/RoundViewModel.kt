package dtu.dk.introDistributedProjectApp.mvvm.game.round

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dtu.dk.introDistributedProjectApp.data.GameState
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

    private val gameState: StateFlow<GameState> = gameRepository.gameState

    init {

    }

    private fun observeGameState() {
        viewModelScope.launch {
            gameState.collect { gameState ->
                onGameStateUpdate(gameState)
            }
        }
    }

    private fun onGameStateUpdate(gameState: GameState) {
        if (gameState.question.isNotEmpty()) {
            _uiModel.update { currentState ->
                currentState.copy(
                    currentQuestion = gameState.question.poll()
                )
            }
        }
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
    }

    fun onTimerFinished() {
        viewModelScope.launch {
            //gameRepository.sendAndReadyForNextQuestion(selectedAnswer)
        }
    }

}