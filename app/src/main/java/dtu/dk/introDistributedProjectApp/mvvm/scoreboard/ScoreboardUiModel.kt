package dtu.dk.introDistributedProjectApp.mvvm.scoreboard

import dtu.dk.introDistributedProjectApp.data.Player

data class ScoreboardUiModel(
    val players: List<Player> = emptyList()
)
