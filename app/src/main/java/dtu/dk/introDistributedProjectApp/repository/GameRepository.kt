package dtu.dk.introDistributedProjectApp.repository

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import dtu.dk.introDistributedProjectApp.data.GameState
import dtu.dk.introDistributedProjectApp.data.GameStateLocal
import dtu.dk.introDistributedProjectApp.data.Question
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
import kotlinx.coroutines.flow.update
import dtu.dk.introDistributedProjectApp.data.Player
import dtu.dk.introDistributedProjectApp.server.*

@Singleton
class GameRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    // The local game state which reflects the current state of the game
    private val _gameStateLocal = MutableStateFlow(GameStateLocal())

    // Same as above, but this is used by any observers of the game state (ViewModels and MainActivity eg.)
    val gameStateLocal: StateFlow<GameStateLocal> = _gameStateLocal.asStateFlow()

    //
    private lateinit var tupleSpaceConnection: TupleSpaceConnection

    init {
        Log.i("GameRepository", "GameRepository created")

        // TODO: Uncomment this when the server should be run from app
        //launchServer()
        Thread.sleep(2000)

        val player = Player(name = "tester", id = gameStateLocal.value.userUUID.toString(), score = 0)
        val otherPlayer = Player(name = "tester2", id = "tester2", score = 0)

        _gameStateLocal.update { currentState ->
            currentState.copy(
                players = listOf(player, otherPlayer) // TODO: DET HER ER DE LOKALE SPILLERE I GAMESTATELOCAL
            )
        }

        CoroutineScope(Dispatchers.IO).launch {
            initializeTupleSpaceConnection()

            for (p in gameStateLocal.value.players) {
                Log.i("GameRepository", "Adding player to shared space: ${p.name}, ID: ${p.id}")
                updateTuple(SpaceName.PLAYER, p.name, p.id)
                //tupleSpaceConnection.setPlayerInScoreboard(p.score, p.id)
            }

            var nextState: String

            while (true) {
                Log.i("GameRepository", "Waiting for next ANSWERING game state")
                nextState = tupleSpaceConnection.queryGameStateAsString(GameState.ANSWERING.displayName) // TODO: <------------ HER SmiDer JEg SÅ SPILlerNE inD i TUPPleSpaceT
                setLocalGameState(nextState)

                _gameStateLocal.update { currentState ->
                    currentState.copy(state = GameState.fromDisplayName(nextState)!!)
                }
                Log.i("GameRepository", "ANSWERING STATE")
                nextQuestion()

                Log.i("GameRepository", "Waiting for next SHOWING game state")
                nextState = tupleSpaceConnection.queryGameStateAsString(GameState.SHOWING.displayName)
                setLocalGameState(nextState)

                Log.i("GameRepository", "SHOWING STATE")
                if (gameStateLocal.value.chosenAnswer == "") {
                    Log.i("GameRepository", "No answer was chosen. ")
                    updateTuple(
                        SpaceName.ANSWER,
                        gameStateLocal.value.chosenAnswer,
                        gameStateLocal.value.userUUID.toString()
                    )
                    val answerText = gameStateLocal.value.question.answers.get(gameStateLocal.value.chosenAnswer.toInt())
                    updateTuple(SpaceName.ANSWER, answerText, "tester2")
                }

                nextState = tupleSpaceConnection.queryGameStateAsString(GameState.FINAL.displayName)
                setLocalGameState(nextState)

                updatePlayerScore() // TODO: This should be turned on! ANTIN DET HER ER TIL DIG <------------------------
                Log.i("GameRepository", "Player[0] score updated to: ${gameStateLocal.value.players[0].score}")
            }



            /*while (true) {
                var currentGameState = gameStateLocal.value.state
                val gameState: List<String>? = retrieveItemFromSpace(SpaceName.GAMESTATE, String::class.java)
                val gameState: String = tupleSpaceConnection.queryGameStateAsString(GameState.ANSWERING.name)
                Log.i("GameRepository", "Gamestate is: $gameState")

                if (gameState != null) {
                    _gameStateLocal.update { currentState ->
                        //currentState.copy(state = GameState.fromDisplayName(gameState[0])!!) - was used for retrieve
                        currentState.copy(state = GameState.fromDisplayName(gameState)!!)
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
            }*/
        }
    }

    private fun updatePlayerScore() {

        val updatedPlayers = mutableListOf<Player>()
        Log.i("GameRepository", "The fuck?")

        for (i in 0 until gameStateLocal.value.players.size) {
            val currentPlayer = gameStateLocal.value.players[i]
            Log.i("GameRepository", "I AM HEREeeeeEEEe ---- $i")
            val updatedScore = tupleSpaceConnection.queryScoreUpdate(currentPlayer.id)
            Log.i("GameRepository", "I AM HERE ---- $i")
            val updatedPlayer = currentPlayer.copy(
                score = updatedScore
            )
            Log.i("GameRepository", "I AM HERE 2")
            updatedPlayers.add(updatedPlayer)
            Log.i("GameRepository", "Player[${i}] score updated to: ${updatedPlayer.score}")
        }

        _gameStateLocal.update { currentState ->
            currentState.copy(
                players = updatedPlayers
            )
        }
    }

    private fun launchServer() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Main.main(arrayOf())
            } catch (e: Exception) {
                Log.e("GameRepository", "Failed to launch server: ${e.message}")
            }
        }
    }

    private fun setLocalGameState(gameState: GameState) {
        _gameStateLocal.update { currentState ->
            currentState.copy(state = gameState)
        }
    }

    private fun setLocalGameState(gameStateName: String) {
        _gameStateLocal.update { currentState ->
            currentState.copy(state = GameState.fromDisplayName(gameStateName)!!)
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
                    Log.e("GameRepository", "No tuple found in space: $spaceName")
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

    fun awaitNextGameState(nextGameState: GameState) {
        val gameState: String = tupleSpaceConnection.queryGameStateAsString(nextGameState.name)

        _gameStateLocal.update { currentState ->
            currentState.copy(state = GameState.fromDisplayName(gameState)!!)
        }
        Log.i("GameRepository", "Gamestate is now: $gameState")
    }

    private suspend fun nextQuestion() {
        val questionResult = tupleSpaceConnection.queryQuestion()
        var questionText = ""
        val answers = mutableListOf<String>()
        var correctAnswerText = ""

        if (questionResult != null) {

            for (pair in questionResult) {
                Log.i("GameRepository", "Key: ${pair.key}, Value: ${pair.value}")
                if (pair.value == 0) {
                    answers.add(pair.key)
                } else if (pair.value == 1) {
                    answers.add(pair.key)
                    correctAnswerText = pair.key
                } else if (pair.value == 2) {
                    questionText = pair.key
                }
            }

            var question: Question = Question(
                question = questionText,
                answers = answers,
            )

            question = shuffleQuestionAnswers(question)

            val correctAnswerPosition = question.answers.indexOf(correctAnswerText)

            _gameStateLocal.update { currentState ->
                currentState.copy(
                    question = question,
                    correctAnswer = correctAnswerPosition
                )
            }

            Log.i("GameRepository", "Question: ${_gameStateLocal.value.question.question}")
        }
    }

    private fun shuffleQuestionAnswers(question: Question): Question {
        val shuffledAnswers = question.answers.shuffled()
        return question.copy(answers = shuffledAnswers)
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
        if (gameStateLocal.value.state != GameState.ANSWERING) {
            Log.e("GameRepository", "Cannot send answer when not in ANSWERING state")
            return
        }

        val currentChosenAnswer = _gameStateLocal.value.chosenAnswer
        _gameStateLocal.update { currentState ->
            currentState.copy(chosenAnswer = answer)
        }
        updateTuple(SpaceName.ANSWER, answer, gameStateLocal.value.userUUID.toString())
        updateTuple(SpaceName.ANSWER, answer, "tester2")
        Log.i("GameRepository", "Chosen answer was changed from: '$currentChosenAnswer' to ${_gameStateLocal.value.chosenAnswer}")
    }

    fun getLocalPlayer(): Player? {
        return gameStateLocal.value.players.find { it.id == gameStateLocal.value.userUUID.toString() }
    }

    fun getPlayers(): List<Player> {
        return gameStateLocal.value.players
    }

    /*fun initializeGame() {
        lauchServer()
        while (!tupleSpaceConnection.getConnected()) {
            CoroutineScope(Dispatchers.IO).launch {
                initializeTupleSpaceConnection()
            }
            Thread.sleep(100)
        }

        setLocalGameState(tupleSpaceConnection.queryGameStateAsString(GameState.ANSWERING.name))

        updateTuple(SpaceName.PLAYER, "test", gameStateLocal.value.userUUID.toString(), 1)
        updateTuple(SpaceName.PLAYER, "test2", "tester2", 2)
    }*/

}