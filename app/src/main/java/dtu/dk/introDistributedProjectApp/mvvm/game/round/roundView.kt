package dtu.dk.introDistributedProjectApp.mvvm.game.round

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dtu.dk.introDistributedProjectApp.R
import dtu.dk.introDistributedProjectApp.data.GameState
import dtu.dk.introDistributedProjectApp.ui.theme.SpaceCadet
import dtu.dk.introDistributedProjectApp.ui.theme.UrbanDictionaryYellow

@Composable
fun RoundView(
    roundViewModel: RoundViewModel,
) {

    //val roundViewModel: RoundViewModel by viewModels()
    val roundUiModel by roundViewModel.uiModel.collectAsState()

    LaunchedEffect(Unit) {
        roundViewModel.clear()
    }

    Box (
        modifier = Modifier
        .fillMaxSize()
        .background(SpaceCadet),
    ) {
        Column(
            modifier = Modifier
                .padding(top = 20.dp)
                .padding(horizontal = 8.dp),
            verticalArrangement = Arrangement.Top,
        ) {
            Row(
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                /*
                IconButton(onClick = { navController.navigate(Screen.StartScreen.route) }) {
                    Icon(Icons.TwoTone.Menu, contentDescription = null, tint = Color.White)}
                 */

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    IconButton(onClick = { }) {
                        Icon(
                            painter = painterResource(id = R.drawable.trophy),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Text(text = roundUiModel.player.score.toString(),
                        color = Color.White,
                        fontSize = 22.sp
                    )
                }
            }

            LinearProgressIndicator(
                progress = {
                    (roundUiModel.secondsLeft.toFloat() / 30f).coerceIn(0f, 1f)
                },
                modifier = Modifier.fillMaxWidth(),
                color = if(roundUiModel.secondsLeft > 10) Color.Green else Color.Red,
                trackColor = Color.LightGray,
            )

            Text(text = (roundUiModel.secondsLeft.toString() + " seconds left"),
                color = Color.White, fontSize = 12.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally))


        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 12.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp),
            ) {
                Text(
                    text = "Question " + roundUiModel.currentRound.toString(),
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth(),
                )
                Text(
                    text = roundUiModel.currentQuestion.question,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier
                        .padding(vertical = 14.dp)
                        .fillMaxWidth(),
                )
            }

            val getButtonColors: @Composable (Int) -> ButtonColors = { index ->

                if(roundUiModel.currentState == GameState.ANSWERING){ //TODO: Find nicer colors
                    if (roundUiModel.selectedAnswer == -1){
                        ButtonDefaults.buttonColors(UrbanDictionaryYellow)
                    } else {
                        if (index == roundUiModel.selectedAnswer){
                            ButtonDefaults.buttonColors(UrbanDictionaryYellow)
                        } else {
                            ButtonDefaults.buttonColors(Color.DarkGray)
                        }
                    }

                } else{
                    if (index == roundUiModel.correctAnswer){
                        ButtonDefaults.buttonColors(Color.Green)
                    } else if (index == roundUiModel.selectedAnswer){
                        ButtonDefaults.buttonColors(Color.Red)
                    }
                    else {
                        ButtonDefaults.buttonColors(Color.DarkGray)
                    }
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 28.dp)
                    .padding(horizontal = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                repeat(roundUiModel.currentQuestion.answers.size) { index ->
                    FilledTonalButton(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = getButtonColors(index), // Button color logic
                        contentPadding = PaddingValues(vertical = 4.dp),
                        onClick = {

                            if (roundUiModel.currentState == GameState.ANSWERING) {
                                roundViewModel.onAnswerSelected(index)
                            } else {
                                Log.e("roundView", "Button was clicked outside of answering state")
                            }
                            roundViewModel.uiModel.value.selectedAnswer = index
                        },
                        enabled = true//roundUiModel.currentState == GameState.ANSWERING // Always enabled to retain the button appearance. Should probably be disabled
                    ) {
                        Text(
                            text = roundUiModel.currentQuestion.answers[index], fontSize = 14.sp,
                            color = Color.DarkGray
                            /*
                            color = if (roundUiModel.currentState == GameState.SHOWING) {
                                if (index == roundUiModel.correctAnswer) {
                                    Color.Black
                                } else {
                                    Color.White
                                }
                            } else {
                                Color.White
                            }

                             */
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.size(36.dp))
    }
}

