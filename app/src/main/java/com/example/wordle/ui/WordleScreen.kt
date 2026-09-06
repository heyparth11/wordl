package com.example.wordle.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.wordle.domain.GameStatus
import com.example.wordle.ui.components.GameBoard
import com.example.wordle.ui.components.Keyboard

import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.wordle.ui.components.LoseDialog
import com.example.wordle.ui.components.StatsDialog
import com.example.wordle.ui.components.WinDialog
import kotlinx.coroutines.delay
import kotlin.let
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordleScreen(
    viewModel: WordleViewModel,
    onBackClick: () -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    var showMessage by remember { mutableStateOf(false) }
    var showResultDialog by remember { mutableStateOf(false) }
    var showStatsDialog by remember { mutableStateOf(false) }

    LaunchedEffect(state.gameStatus) {
        if (state.gameStatus != GameStatus.PLAYING) {
            delay(1500.milliseconds)
            showResultDialog = true
        } else {
            showResultDialog = false
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.background,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground
                ),
                title = {
                    Text(
                        text = "Wordle",
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onBackClick()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "How to play"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            showStatsDialog = true
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.BarChart,
                            contentDescription = "Statistics"
                        )
                    }

                    IconButton(
                        onClick = {
                            // TODO: show settings
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp)
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                GameBoard(
                    state = state,
                    modifier = Modifier
                )
                state.message?.let { message ->

                    LaunchedEffect(message) {
                        showMessage = true

                        delay(1500.milliseconds)

                        showMessage = false
                    }

                    if (showMessage && state.gameStatus == GameStatus.PLAYING) {

                        Box(
                            modifier = Modifier
                                .border(
                                    width = 2.dp,
                                    color = Color.Gray,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .background(Color.White, shape = RoundedCornerShape(8.dp))
                                .padding(horizontal = 24.dp, vertical = 18.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {

                                Text(
                                    text = message,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }

                if (showResultDialog && state.gameStatus != GameStatus.PLAYING) {
                    val definitionText = state.definition ?: "Loading definition..."
                    if (state.gameStatus == GameStatus.WON) {
                        WinDialog(
                            word = state.targetWord,
                            definition = definitionText,
                            onNewGame = {
                                showResultDialog = false
                                viewModel.restart()
                            }
                        )
                    } else {
                        LoseDialog(
                            word = state.targetWord,
                            definition = definitionText,
                            onNewGame = {
                                showResultDialog = false
                                viewModel.restart()
                            }
                        )
                    }
                }

                if (showStatsDialog) {
                    StatsDialog(
                        stats = state.stats,
                        onDismiss = { showStatsDialog = false }
                    )
                }
            }

            Keyboard(
                keyboardState = state.keyboardState,
                onKeyPressed = viewModel::addLetter,
                onDelete = viewModel::removeLetter,
                onEnter = viewModel::submitGuess
            )

            Spacer(
                modifier = Modifier.height(90.dp)
            )
        }
    }
}