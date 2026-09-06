package com.example.wordle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wordle.ui.MainMenu
import com.example.wordle.ui.WordleScreen
import com.example.wordle.ui.WordleViewModel
import com.example.wordle.ui.theme.WordleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WordleTheme {
                WordleApp()
            }
        }
    }
}

@Composable
fun WordleApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main_menu"
    ) {
        composable("main_menu") {
            MainMenu(
                onPlayClick = {
                    navController.navigate("wordle_screen")
                },
                onPlayWithFriendClick = {
                    // TODO: Play With Friend
                },
                onSettingsClick = {
                    // TODO: Settings
                }
            )
        }

        composable("wordle_screen") {
            val viewModel: WordleViewModel = viewModel()
            WordleScreen(
                viewModel = viewModel,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}