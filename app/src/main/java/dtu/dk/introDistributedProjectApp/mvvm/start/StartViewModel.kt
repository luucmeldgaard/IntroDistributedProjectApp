package dtu.dk.introDistributedProjectApp.mvvm.start

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dtu.dk.introDistributedProjectApp.repository.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@HiltViewModel
class StartViewModel @Inject constructor(
    private val gameRepository: GameRepository
): ViewModel() {

    init {

    }

    private val _uiModel = MutableStateFlow(StartUiModel())
    val uiModel: StateFlow<StartUiModel> = _uiModel.asStateFlow()

    fun onStartGameClicked() {
        gameRepository.join()
    }

    fun startGame() {
        //gameRepository.initializeGame()
    }

}