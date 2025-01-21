package dtu.dk.introDistributedProjectApp.data

data class Question(
    val question: String = "",
    val answers: List<String> = emptyList()
    //val correctAnswer: Int = -1
)
