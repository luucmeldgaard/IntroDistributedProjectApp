package dtu.dk.introDistributedProjectApp.data

data class Player(
    val id: String,
    val name: String,
    var score: Int = 0
)
