package com.example.wordle

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wordle.data.ThemeRepository
import com.example.wordle.domain.ThemeMode
import com.example.wordle.ui.MainMenu
import com.example.wordle.ui.WordleScreen
import com.example.wordle.ui.WordleViewModel
import com.example.wordle.ui.theme.WordleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val themeRepository = ThemeRepository(applicationContext)

        setContent {
            var currentTheme by remember { mutableStateOf(themeRepository.getThemeMode()) }

            WordleTheme(themeMode = currentTheme) {
                WordleApp(
                    currentTheme = currentTheme,
                    onThemeSelected = { newTheme ->
                        currentTheme = newTheme
                        themeRepository.setThemeMode(newTheme)
                    }
                )
            }
        }
    }
}

@Composable
fun WordleApp(
    currentTheme: ThemeMode = ThemeMode.SYSTEM,
    onThemeSelected: (ThemeMode) -> Unit = {}
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "main_menu"
    ) {
        composable("main_menu") {
            MainMenu(
                currentTheme = currentTheme,
                onThemeSelected = onThemeSelected,
                onPlayClick = {
                    navController.navigate("wordle_screen")
                },
                onPlayWithFriendClick = {
                    // TODO: Play With Friend
                }
            )
        }

        composable("wordle_screen") {
            val viewModel: WordleViewModel = viewModel()
            WordleScreen(
                viewModel = viewModel,
                currentTheme = currentTheme,
                onThemeSelected = onThemeSelected,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}