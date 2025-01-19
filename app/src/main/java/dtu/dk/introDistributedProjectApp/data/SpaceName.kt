package dtu.dk.introDistributedProjectApp.data

enum class SpaceName(val displayName: String) {
    PLAYER("playerConnectionSpace"),
    QUESTION("questionSpace"),
    GAMESTATE("gameStateSpace"),
    ANSWER("answerSpace"),
    SCOREBOARD("scoreboardSpace"),
    ROOT("");

    companion object {
        fun fromDisplayName(name: String): SpaceName? {
            return entries.find { it.displayName == name }
        }
    }
}