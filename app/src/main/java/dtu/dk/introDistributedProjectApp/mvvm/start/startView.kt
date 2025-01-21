package dtu.dk.introDistributedProjectApp.mvvm.start

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dtu.dk.introDistributedProjectApp.ui.theme.Coral
import dtu.dk.introDistributedProjectApp.ui.theme.UrbanDictionaryYellow

@Composable
fun StartView(
    //navController: NavController,
    viewModel: StartViewModel,
) {

    val startUiModel by viewModel.uiModel.collectAsState()
    var buttonClicked by remember { mutableStateOf(false) }

    var enteredIP by remember { mutableStateOf("") }
    var enteredName by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(UrbanDictionaryYellow),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        if (startUiModel.createGame) {
            Text(
                text = "IP-address to join: ",
                color = Color.White,
                fontStyle = FontStyle.Italic,
                fontSize = 20.sp,
            )

            Text(
                text = startUiModel.ipAddress,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
            )

            TextField(

                value = enteredName,
                onValueChange = { enteredName = it },
                placeholder = { Text(text = "Enter your name", fontSize = 20.sp) },
                textStyle = TextStyle(color = Color.White, fontSize = 20.sp),
                singleLine = true,

                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp)
            )

            FilledTonalButton(
                onClick = {
                    buttonClicked = true
                    viewModel.startGame(enteredName)
                },
                enabled = !buttonClicked
            ) {
                Text(text = "Start Game")
            }
        } else if (startUiModel.joinGame) {
            Text(
                text = "Enter IP address to join",
                color = Color.White,
                fontStyle = FontStyle.Italic,
                fontSize = 20.sp,
            )

            Box(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(0.8f)
                    .padding(4.dp)
            )

            TextField(
                value = enteredName,
                onValueChange = { enteredName = it },
                textStyle = TextStyle(color = Color.White, fontSize = 20.sp),
                singleLine = true,
                placeholder = { Text(text = "Enter you name", fontSize = 20.sp) },
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(16.dp)
            )

            TextField(
                    value = enteredIP,
                    onValueChange = { enteredIP = it },
                textStyle = TextStyle(color = Color.White, fontSize = 20.sp),
                    singleLine = true,
                label = { Text(text = "Enter IP", fontSize = 20.sp) },
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .background(
                            color = Color.White,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp)
                )



            FilledTonalButton(
                onClick = {
                    buttonClicked = true
                    viewModel.joinGame(enteredIP, enteredName)
                },
                enabled = !buttonClicked
            ) {
                Text(text = "Join")

            }

        } else {
            FilledTonalButton(
                onClick = {
                    viewModel.onCreateGamePressed()
                }
            ) {
                Text(text = "Create Game")
            }
            FilledTonalButton(
                onClick = {
                    viewModel.onJoinGamePressed()
                }
            ) {
                Text(text = "Join Game")
            }
        }
    }



}