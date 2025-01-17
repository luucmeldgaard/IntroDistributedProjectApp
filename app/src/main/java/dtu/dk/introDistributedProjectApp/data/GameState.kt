package dtu.dk.introDistributedProjectApp.data

enum class GameState(val displayName: String) {
    SHOWING("SHOWING"),
    ANSWERING("ANSWERING"),
    FINAL("FINAL");

    companion object {
        // Function to get an enum constant from the string property
        fun fromDisplayName(name: String): GameState? {
            return entries.find { it.displayName == name }
        }
    }
}