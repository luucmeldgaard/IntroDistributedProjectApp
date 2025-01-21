package dtu.dk.introDistributedProjectApp.data

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class GameStateLocal(
    val round: Int = 0,
    val players: List<Player> = emptyList(),
    val question: Question = Question(),
    val state: GameState = GameState.LOCAL_START,
    val chosenAnswer: String = "",
    val correctAnswer: Int = 0,
    val userUUID: UUID = UUID.randomUUID(),
    val host: Boolean = false
)
