package dtu.dk.introDistributedProjectApp.repository

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import dtu.dk.introDistributedProjectApp.data.GameState
import dtu.dk.introDistributedProjectApp.data.TupleSpaceConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val tupleSpaceConnection: TupleSpaceConnection
){

    init {
        Log.i("GameRepository", "GameRepository created")
    }

    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    fun updateGameState(gameState: GameState) {
        _gameState.value = gameState
    }

    fun sendAndReadyForNextQuestion(selectedAnswer: Int) {
        CoroutineScope(Dispatchers.Default).launch {
            tupleSpaceConnection.sendAnswer(selectedAnswer)
            tupleSpaceConnection.retrieveGameState()
            _gameState.update { currentState ->
                currentState.copy(
                    // get updated game state from tupleSpaceConnection
                )
            }
        }
    }

    fun join() {
        CoroutineScope(Dispatchers.Default).launch {
            tupleSpaceConnection.retrieveGameState()
            _gameState.update { currentState ->
                currentState.copy(
                    // get ID from tupleSpaceConnection and set it here
                )
            }
        }
    }

}