package dtu.dk.introDistributedProjectApp.mvvm.game.round

import androidx.compose.ui.graphics.Color
import dtu.dk.introDistributedProjectApp.data.Question
import dtu.dk.introDistributedProjectApp.ui.theme.AirForceBlue
import dtu.dk.introDistributedProjectApp.ui.theme.ChineseViolet
import dtu.dk.introDistributedProjectApp.ui.theme.MyrtleGreen

data class RoundUiModel(
    val currentScore: Int = 2,
    val currentQuestion: Question = Question("When a man has vigorous, unprotected intercourse with a woman. " +
            "He then her eats out to the point of vomiting. After filling her vagina with vomit, " +
            "the man then uses his home economics skills to sew shut the woman's vagina. " +
            "After several weeks of \"brewing,\" the man, or perhaps another very impatient man, " +
            "cuts open the stitches, and uses the mixture to make a pie and eat it with mash potato", listOf("Sad Meep", "Crockpie", "Guston Guston"), 1),
    val currentRound: Int = 3,
    val secondsLeft: Int = 14,
    val finished: Boolean = false,
    val buttonColors: List<Color> = listOf(ChineseViolet, AirForceBlue, MyrtleGreen),
    val selectedAnswer: Int = 0,
    val correctAnswer: String = "",
)