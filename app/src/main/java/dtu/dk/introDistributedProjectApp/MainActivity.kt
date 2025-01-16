package dtu.dk.introDistributedProjectApp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dtu.dk.introDistributedProjectApp.data.TupleSpaceConnection
import dtu.dk.introDistributedProjectApp.mvvm.Screen
import dtu.dk.introDistributedProjectApp.mvvm.topBar.TopBar
import dtu.dk.introDistributedProjectApp.mvvm.game.round.RoundView
import dtu.dk.introDistributedProjectApp.mvvm.game.round.RoundViewModel
import dtu.dk.introDistributedProjectApp.mvvm.start.StartViewModel
import dtu.dk.introDistributedProjectApp.repository.ApplicationRepository
import dtu.dk.introDistributedProjectApp.repository.GameRepository
import dtu.dk.introDistributedProjectApp.ui.theme.IntroDistributedProjectAppTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var applicationRepository: ApplicationRepository

    @Inject
    lateinit var tupleSpaceConnection: TupleSpaceConnection

    @Inject
    lateinit var gameRepository: GameRepository

    private val startViewModel: StartViewModel by viewModels()
    private val roundViewModel: RoundViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IntroDistributedProjectAppTheme {
                MainNavHost(
                    roundViewModel = roundViewModel
                )
            }
        }
    }
}

@Composable
fun MainNavHost(
    roundViewModel: RoundViewModel
) {
    val navController = rememberNavController();

    Scaffold(
        content = { innerPadding ->
            Column() {
                //TopBar(navController)
                //Modifier.padding(innerPadding)
                Box() {
                    NavHost(
                        navController = navController,
                        startDestination = Screen.GameScreen.route
                    ) {
                        composable(route = "test") {
                            Greeting("Android")
                        }
                        composable(route = Screen.GameScreen.route) {
                            RoundView(navController, roundViewModel)
                        }
                        composable(route = Screen.StartScreen.route) {
                            Greeting("Start Screen")
                        }
                        composable(route = Screen.ScoreScreen.route) {
                            Greeting("Score Screen")
                        }
                    }
                }
            }
        }
    )


}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    IntroDistributedProjectAppTheme {
        Greeting("Android")
    }
}