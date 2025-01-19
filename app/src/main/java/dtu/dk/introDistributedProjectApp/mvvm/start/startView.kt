package dtu.dk.introDistributedProjectApp.mvvm.start

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import dtu.dk.introDistributedProjectApp.mvvm.Screen
import dtu.dk.introDistributedProjectApp.ui.theme.UrbanDictionaryYellow

@Composable
fun StartView(
    navController: NavController,
    viewModel: StartViewModel,
) {

    val startUiModel by viewModel.uiModel.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UrbanDictionaryYellow),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        FilledTonalButton(
            onClick = {
                //viewModel.startGame()
            }
        ) {
            Text(text = "Start Game")
        }
    }



}