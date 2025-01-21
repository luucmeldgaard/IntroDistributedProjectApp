package dtu.dk.introDistributedProjectApp.mvvm.topBar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dtu.dk.introDistributedProjectApp.mvvm.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavController,
) {
    Box(modifier = Modifier
        .height(56.dp)
        .fillMaxWidth()
        .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = { navController.navigate(Screen.StartScreen.route) }) {
                Icon(Icons.Filled.Menu, contentDescription = null)
            }

            Text(text = "14", color = Color.White)

            IconButton(onClick = {  }) {
                Icon(Icons.Filled.Favorite, contentDescription = null)
            }

        }
    }
}