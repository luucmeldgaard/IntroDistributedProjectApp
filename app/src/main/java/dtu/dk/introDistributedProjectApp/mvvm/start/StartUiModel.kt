package dtu.dk.introDistributedProjectApp.mvvm.start

data class StartUiModel (
    val createGame: Boolean = false,
    val joinGame: Boolean = false,
    val ipAddress: String = "",
)