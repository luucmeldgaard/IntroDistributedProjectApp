package dtu.dk.introDistributedProjectApp.data

import kotlinx.serialization.Serializable
import java.util.ArrayDeque
import java.util.Queue
import java.util.UUID

@Serializable
data class GameStateLocal(
    val round: Int = 0,
    val players: List<Player> = emptyList(),
    val question: Question = Question(),
    val state: GameState = GameState.SHOWING,
    val chosenAnswer: String = "",
    val correctAnswer: String = "",
    val userUUID: UUID = UUID.randomUUID()
)
