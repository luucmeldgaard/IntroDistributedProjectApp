package dtu.dk.introDistributedProjectApp.repository

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import dtu.dk.introDistributedProjectApp.data.GameStateLocal
import dtu.dk.introDistributedProjectApp.data.SpaceName
import dtu.dk.introDistributedProjectApp.data.TupleSpaceConnection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val _gameStateLocal = MutableStateFlow(GameStateLocal())
    val gameStateLocal: StateFlow<GameStateLocal> = _gameStateLocal.asStateFlow()

    private lateinit var tupleSpaceConnection: TupleSpaceConnection

    init {
        Log.i("GameRepository", "GameRepository created")

        CoroutineScope(Dispatchers.IO).launch {
            initializeTupleSpaceConnection()
            //var result: String? = retrieveItemFromSpace(SpaceName.GAMESTATE, String::class.java)
            //Log.i("GameRepository", "Retrieved tuple: $result")
            updateTuple(SpaceName.PLAYER, "test", "tester", 1)
            //result = retrieveItemFromSpace(SpaceName.GAMESTATE, String::class.java)
            //Log.i("GameRepository", "Retrieved tuple: $result")
        }
    }

    private suspend fun initializeTupleSpaceConnection() {
        try {
            tupleSpaceConnection = TupleSpaceConnection()
            Log.i("GameRepository", "TupleSpaceConnection initialized successfully")
        } catch (e: IOException) {
            Log.e("GameRepository", "Failed to initialize TupleSpaceConnection: ${e.message}")
        }
    }

    private suspend fun <T> retrieveItemFromSpace(spaceName: SpaceName, type: Class<T>): T? {
        while (this::tupleSpaceConnection.isInitialized) {
            try {
                // Retrieve tuple from remote space
                val result = tupleSpaceConnection.queryTuple(spaceName, String::class.java)
                val firstResult = result[0]
                if (result != null && result.isNotEmpty()) {
                    // Cast the first element to the specified type
                    val tupleElement = result[0]
                    if (type.isInstance(tupleElement)) {
                        @Suppress("UNCHECKED_CAST")
                        return tupleElement as T
                    } else {
                        println("Unexpected type: ${tupleElement?.javaClass}")
                    }
                } else {
                    println("No tuple found in  space.")
                }

                //val gameState = GameState.fromDisplayName(firstResult)
                // Update the state
                //_gameStateLocal.update { currentState ->
                //    currentState.copy(remoteGameState = gameState)
                //}
            } catch (e: InterruptedException) {
                Log.e("GameRepository", "Tuple retrieval interrupted: ${e.message}")
                break
            } catch (e: Exception) {
                Log.e("GameRepository", "Error retrieving tuple: ${e.message}")
            }
        }
        return null
    }

    @Throws(InterruptedException::class)
    fun updateTuple(spaceName: SpaceName, vararg items: Any) {
        Log.i("GameRepository", "Updating tuple in space: $spaceName")
        tupleSpaceConnection.updateTuple(spaceName, *items)
    }

    fun updateGameState(gameStateLocal: GameStateLocal) {
        //_gameStateLocal.value = gameStateLocal
    }

    fun sendAndReadyForNextQuestion(selectedAnswer: Int) {
        CoroutineScope(Dispatchers.Default).launch {
            /*tupleSpaceConnection.sendAnswer(selectedAnswer)
            tupleSpaceConnection.retrieveGameState()
            _gameStateLocal.update { currentState ->
                currentState.copy(
                    // get updated game state from tupleSpaceConnection
                )
            }*/
        }
    }

    fun join() {
        CoroutineScope(Dispatchers.Default).launch {
            /*tupleSpaceConnection.retrieveGameState()
            _gameStateLocal.update { currentState ->
                currentState.copy(
                    // get ID from tupleSpaceConnection and set it here
                )
            }*/
        }
    }

}