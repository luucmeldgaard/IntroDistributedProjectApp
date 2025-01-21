package dtu.dk.introDistributedProjectApp.mvvm.game.round

import androidx.compose.ui.graphics.Color
import dtu.dk.introDistributedProjectApp.data.GameState
import dtu.dk.introDistributedProjectApp.data.Player
import dtu.dk.introDistributedProjectApp.data.Question
import dtu.dk.introDistributedProjectApp.ui.theme.AirForceBlue
import dtu.dk.introDistributedProjectApp.ui.theme.ChineseViolet
import dtu.dk.introDistributedProjectApp.ui.theme.MyrtleGreen

data class RoundUiModel(
    val currentScore: Int = 2,
    val currentQuestion: Question = Question("Loading...", listOf("Option one", "Option two", "Option three")/*, 1*/), //TODO: Revert
    val currentRound: Int = 3,
    val secondsLeft: Int = 30,
    val finished: Boolean = false,
    val buttonColors: List<Color> = listOf(ChineseViolet, AirForceBlue, MyrtleGreen),
    var selectedAnswer: Int = -1,
    val correctAnswer: Int = -1,
    val currentState: GameState = GameState.ANSWERING,
    val player: Player = Player(
        id = "9987",
        name = "TORBEN",
        score = 99
    ),
    val hosting: Boolean = false,
    val ipAddress: String = "",
)