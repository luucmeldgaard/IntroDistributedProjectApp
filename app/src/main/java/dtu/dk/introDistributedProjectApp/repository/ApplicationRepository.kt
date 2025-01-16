package dtu.dk.introDistributedProjectApp.repository

import android.content.Context
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApplicationRepository @Inject constructor(
    @ApplicationContext private val context: Context,
){

    init {
        Log.i("ApplicationRepository", "ApplicationRepository created")
    }

}