package dtu.dk.introDistributedProjectApp.mvvm.scoreboard

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dtu.dk.introDistributedProjectApp.data.GameState
import dtu.dk.introDistributedProjectApp.data.GameStateLocal
import dtu.dk.introDistributedProjectApp.data.Player
import dtu.dk.introDistributedProjectApp.repository.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScoreboardViewModel @Inject constructor(
    private val gameRepository: GameRepository
) : ViewModel() {

    private val _uiModel = MutableStateFlow(ScoreboardUiModel())
    val uiModel: StateFlow<ScoreboardUiModel> = _uiModel.asStateFlow()

    init {
        viewModelScope.launch {
            gameRepository.gameStateLocal.collect { gameState ->
                onGameStateUpdate(gameState)
            }
        }

        sortPlayersByScore(gameRepository.getPlayers())
    }

    private fun sortPlayersByScore(players: List<Player>) {
        val sortedPlayers = players.sortedByDescending { it.score }
        _uiModel.update { currentState ->
            currentState.copy(
                players = sortedPlayers
            )
        }
    }

    private fun onGameStateUpdate(gameStateLocal: GameStateLocal) {
        Log.i("ScoreboardViewModel", "GameState: ${gameStateLocal.state}")

        sortPlayersByScore(gameStateLocal.players)

        if (gameStateLocal.state == GameState.ANSWERING) {
            //Log.e("ScoreboardViewModel", "Somehow in ANSWERING state. This should not happen.")
        } else if (gameStateLocal.state == GameState.SHOWING) {
            //Log.e("ScoreboardViewModel", "Somehow in SHOWING state. This should not happen.")
        } else if (gameStateLocal.state == GameState.FINAL) {

        }

    }
}