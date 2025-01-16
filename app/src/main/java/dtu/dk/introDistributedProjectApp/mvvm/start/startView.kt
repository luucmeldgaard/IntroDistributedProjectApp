package dtu.dk.introDistributedProjectApp.mvvm.start

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavController

@Composable
fun StartView(
    navController: NavController,
    viewModel: StartViewModel,
    model: StartUiModel
) {

    val startUiModel by viewModel.uiModel.collectAsState()



}