package dtu.dk.introDistributedProjectApp.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TupleSpaceConnection @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun retrieveGameState() {
        TODO("Not yet implemented")
    }

    fun sendAnswer(selectedAnswer: Int) {

    }

}