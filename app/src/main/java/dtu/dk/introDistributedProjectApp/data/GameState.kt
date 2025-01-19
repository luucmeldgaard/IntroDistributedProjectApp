package dtu.dk.introDistributedProjectApp.data

enum class GameState(val displayName: String) {
    SHOWING("SHOWING"),
    ANSWERING("ANSWERING"),
    FINAL("SCOREBOARD");

    companion object {
        fun fromDisplayName(name: String): GameState? {
            return entries.find { it.displayName == name }
        }
    }
}