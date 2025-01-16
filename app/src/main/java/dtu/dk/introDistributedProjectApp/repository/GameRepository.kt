package dtu.dk.introDistributedProjectApp.repository

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import dtu.dk.introDistributedProjectApp.data.GameState
import dtu.dk.introDistributedProjectApp.data.TupleSpaceConnection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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

}