package dtu.dk.introDistributedProjectApp.mvvm.game.round

import dtu.dk.introDistributedProjectApp.data.Question

data class RoundUiModel (
    val currentScore: Int = 0,
    val currentQuestion: Question = Question("", emptyList(), 0),
    val currentRound: Int = 0,
    val secondsLeft: Int = 0,
    val finished: Boolean = false,
)