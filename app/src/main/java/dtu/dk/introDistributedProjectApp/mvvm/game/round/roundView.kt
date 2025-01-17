package dtu.dk.introDistributedProjectApp.mvvm.game.round

import android.app.Application
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.twotone.Favorite
import androidx.compose.material.icons.twotone.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.font.FontVariation.weight
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.qualifiers.ApplicationContext
import dtu.dk.introDistributedProjectApp.Greeting
import dtu.dk.introDistributedProjectApp.R
import dtu.dk.introDistributedProjectApp.mvvm.Screen
import dtu.dk.introDistributedProjectApp.repository.GameRepository
import dtu.dk.introDistributedProjectApp.ui.theme.AirForceBlue
import dtu.dk.introDistributedProjectApp.ui.theme.ChineseViolet
import dtu.dk.introDistributedProjectApp.ui.theme.Coral
import dtu.dk.introDistributedProjectApp.ui.theme.IntroDistributedProjectAppTheme
import dtu.dk.introDistributedProjectApp.ui.theme.MyrtleGreen
import dtu.dk.introDistributedProjectApp.ui.theme.SpaceCadet
import dtu.dk.introDistributedProjectApp.ui.theme.Straw
import dtu.dk.introDistributedProjectApp.ui.theme.UrbanDictionaryYellow

@Composable
fun RoundView(
    navController: NavController,
    roundViewModel: RoundViewModel,
) {

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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                IconButton(onClick = { navController.navigate(Screen.StartScreen.route) }) {
                    Icon(Icons.TwoTone.Menu, contentDescription = null, tint = Color.White)
                }

                Text(text = roundUiModel.currentRound.toString(), color = Color.White)

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
                    Text(text = roundUiModel.currentScore.toString(), color = Color.White)
                }
            }
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
                        colors =
                        if (roundUiModel.correctAnswer == roundUiModel.currentQuestion.answers[index])
                            ButtonDefaults.buttonColors(UrbanDictionaryYellow)
                        else if (roundUiModel.selectedAnswer == index + 1 || roundUiModel.selectedAnswer == 0)
                            ButtonDefaults.buttonColors(roundUiModel.buttonColors[index])
                        else ButtonDefaults.buttonColors(
                            Color.DarkGray),

                        contentPadding = PaddingValues(vertical = 4.dp),
                        onClick = {
                            roundViewModel.onAnswerSelected(index)
                        },
                    ) {
                        Text(text = roundUiModel.currentQuestion.answers[index], fontSize = 12.6.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.size(36.dp))
    }
}

