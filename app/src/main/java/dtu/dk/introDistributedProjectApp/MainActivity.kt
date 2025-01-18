package dtu.dk.introDistributedProjectApp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dtu.dk.introDistributedProjectApp.data.GameState
import dtu.dk.introDistributedProjectApp.mvvm.Screen
import dtu.dk.introDistributedProjectApp.mvvm.game.round.RoundView
import dtu.dk.introDistributedProjectApp.mvvm.game.round.RoundViewModel
import dtu.dk.introDistributedProjectApp.mvvm.start.StartView
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
    lateinit var gameRepository: GameRepository

    private val startViewModel: StartViewModel by viewModels()
    private val roundViewModel: RoundViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            IntroDistributedProjectAppTheme {
                MainNavHost(
                    roundViewModel = roundViewModel,
                    gameRepository = gameRepository,
                    startViewModel = startViewModel,
                )
            }
        }
    }
}

@Composable
fun MainNavHost(
    roundViewModel: RoundViewModel,
    startViewModel: StartViewModel,
    gameRepository: GameRepository
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
                        startDestination = Screen.StartScreen.route
                    ) {
                        composable(route = "test") {
                            Greeting("Android")
                        }
                        composable(route = Screen.GameScreen.route) {
                            RoundView(navController, roundViewModel)
                        }
                        composable(route = Screen.StartScreen.route) {
                            StartView(navController, startViewModel)
                        }
                        composable(route = Screen.ScoreScreen.route) {
                            Greeting("Score Screen")
                        }
                    }
                }
            }
        }
    )

    val gameStateLocal by gameRepository.gameStateLocal.collectAsState()

    LaunchedEffect(gameStateLocal.state) {
        Log.i("MainActivity", "GameState updated: ${gameStateLocal.state}")
        val currentDestination = navController.currentBackStackEntry?.destination?.route
        when (gameStateLocal.state) {
            GameState.SHOWING -> {
                if (currentDestination != Screen.GameScreen.route) {
                    navController.navigate(Screen.GameScreen.route)
                }
            }
            GameState.ANSWERING -> {
                if (currentDestination != Screen.GameScreen.route) {
                    navController.navigate(Screen.GameScreen.route)
                }
            }
            GameState.FINAL -> {
                if (currentDestination != Screen.ScoreScreen.route) {
                    navController.navigate(Screen.ScoreScreen.route)
                }
            }
        }
    }


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