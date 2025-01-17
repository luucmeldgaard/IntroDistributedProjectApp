package dtu.dk.introDistributedProjectApp.repository

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import dtu.dk.introDistributedProjectApp.data.GameState
import dtu.dk.introDistributedProjectApp.data.GameStateLocal
import dtu.dk.introDistributedProjectApp.data.Question
import dtu.dk.introDistributedProjectApp.data.SpaceName
import dtu.dk.introDistributedProjectApp.data.TupleSpaceConnection
import dtu.dk.introDistributedProjectApp.mvvm.game.round.RoundUiModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.update
import dtu.dk.introDistributedProjectApp.data.Player

@Singleton
class GameRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val _gameStateLocal = MutableStateFlow(GameStateLocal())
    val gameStateLocal: StateFlow<GameStateLocal> = _gameStateLocal.asStateFlow()

    private lateinit var tupleSpaceConnection: TupleSpaceConnection

    init {
        Log.i("GameRepository", "GameRepository created")

        val player = Player(name = "tester", id = gameStateLocal.value.userUUID.toString(), score = 0)
        val otherPlayer = Player(name = "tester2", id = "tester2", score = 0)

        CoroutineScope(Dispatchers.IO).launch {
            initializeTupleSpaceConnection()
            updateTuple(SpaceName.PLAYER, player.name, gameStateLocal.value.userUUID.toString(), player.score)
            updateTuple(SpaceName.PLAYER, "test2", "tester2", 2)

            while (true) {
                val currentGameState = gameStateLocal.value.state
                val gameState: List<String>? = retrieveItemFromSpace(SpaceName.GAMESTATE, String::class.java)
                Log.i("GameRepository", "Gamestate is: $gameState")
                Thread.sleep(1000)

                if (gameState != null) {
                    _gameStateLocal.update { currentState ->
                        currentState.copy(state = GameState.fromDisplayName(gameState[0])!!)
                    }
                }
                if (currentGameState != gameStateLocal.value.state) {
                    if (gameStateLocal.value.state == GameState.ANSWERING) {
                        Log.i("GameRepository", "ANSWERING STATE")
                        nextQuestion()
                    }
                    else if (currentGameState == GameState.SHOWING) {
                        Log.i("GameRepository", "SHOWING STATE")
                        if (gameStateLocal.value.chosenAnswer == "") {
                            Log.i("GameRepository", "No answer was chosen. ")
                            updateTuple(SpaceName.ANSWER, gameStateLocal.value.chosenAnswer, gameStateLocal.value.userUUID.toString())
                            updateTuple(SpaceName.ANSWER, gameStateLocal.value.chosenAnswer, "tester2")
                        }

                        val correctAnswer: List<String>? = retrieveItemFromSpace(SpaceName.ANSWER, String::class.java)
                        if (correctAnswer != null) {
                            _gameStateLocal.update { currentState ->
                                currentState.copy(correctAnswer = correctAnswer[0])
                            }
                        }

                        for (player in _gameStateLocal.value.players) {
                            if (player.id == "tester") {
                                _gameStateLocal.update { currentState ->
                                    currentState.copy(
                                        players = currentState.players.map {
                                            if (it.id == "tester") {
                                                it.copy(score = tupleSpaceConnection.queryScoreUpdate(gameStateLocal.value.userUUID.toString()))
                                            } else {
                                                it
                                            }
                                        }
                                    )
                                }
                            }
                        }

                    }
                }
            }
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

    private suspend fun <T> retrieveItemFromSpace(spaceName: SpaceName, vararg types: Class<*>): List<T>? {
        while (this::tupleSpaceConnection.isInitialized) {
            try {
                // Retrieve tuple from the remote space
                val result = tupleSpaceConnection.queryTuple(spaceName, *types)

                // Ensure the result is not null or empty
                if (result != null && result.isNotEmpty()) {
                    @Suppress("UNCHECKED_CAST")
                    return result.toList() as List<T>
                } else {
                    println("No tuple found in $spaceName space.")
                }
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

    suspend fun nextQuestion() {
        val questionResult: List<String>? = retrieveItemFromSpace(
            SpaceName.QUESTION,
            String::class.java,
            String::class.java,
            String::class.java,
            String::class.java
        )

        if (questionResult != null) {
            _gameStateLocal.update { currentState ->
                currentState.copy(
                    question = Question(
                        question = questionResult[0],
                        answers = listOf(questionResult[1], questionResult[2], questionResult[3]),
                    ))
            }

            Log.i("GameRepository", "Question: ${_gameStateLocal.value.question.question}")
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

    fun getGameState(): GameState {
        return _gameStateLocal.value.state
    }

    fun sendAnswer(answer: String) {
        val currentChosenAnswer = _gameStateLocal.value.chosenAnswer
        _gameStateLocal.update { currentState ->
            currentState.copy(chosenAnswer = answer)
        }
        updateTuple(SpaceName.ANSWER, gameStateLocal.value.chosenAnswer, gameStateLocal.value.userUUID.toString())
        updateTuple(SpaceName.ANSWER, gameStateLocal.value.chosenAnswer, "tester2")
        Log.i("GameRepository", "Chosen answer was changed from: '$currentChosenAnswer' to ${_gameStateLocal.value.chosenAnswer}")
    }

}