package dtu.dk.introDistributedProjectApp.mvvm

sealed class Screen(val route: String) {
    data object StartScreen: Screen("start")
    data object GameScreen: Screen("game")
    data object ScoreScreen: Screen("score")

}