package dtu.dk.introDistributedProjectApp.data

import kotlinx.serialization.Serializable
import java.util.ArrayDeque
import java.util.Queue

@Serializable
data class GameState(
    val round: Int = 0,
    val players: List<Player> = emptyList(),
    val question: Queue<Question> = ArrayDeque<Question>()
)
