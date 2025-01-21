package dtu.dk.introDistributedProjectApp.mvvm.scoreboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dtu.dk.introDistributedProjectApp.ui.theme.SpaceCadet


@Composable
fun ScoreboardView(
    viewModel: ScoreboardViewModel,
) {

    val uiModel by viewModel.uiModel.collectAsState()

    Box (
        modifier = Modifier
            .fillMaxSize()
            .background(SpaceCadet),
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 28.dp)
                .padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Text(
                modifier = Modifier.padding(top = 20.dp),
                text = "Scoreboard",
                fontSize = 24.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.padding(vertical = 30.dp))
            HorizontalDivider(thickness = 1.dp, color = Color.White)
            repeat(uiModel.players.size) { index ->
                val currentPlayer = uiModel.players[index]

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = currentPlayer.name, fontSize = 14.sp,
                        color = Color.White,
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(end = 10.dp),
                    )
                    Text(
                        modifier = Modifier.padding(start = 10.dp),
                        text = currentPlayer.score.toString(), fontSize = 14.sp,
                        color = Color.White,

                    )
                }
                HorizontalDivider(thickness = 1.dp, color = Color.White)
            }
        }
    }
}
