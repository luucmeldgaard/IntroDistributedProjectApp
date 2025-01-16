package dtu.dk.introDistributedProjectApp.data

import org.intellij.lang.annotations.Identifier

data class Player(
    val id: Int,
    val name: String,
    val score: Int = 0
)
