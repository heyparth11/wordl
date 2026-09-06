package com.example.wordle.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun DebugWordleScreen(
    viewModel: WordleViewModel
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Wordle Debug",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            text = "Status: ${state.gameStatus}"
        )

        Text(
            text = "Current: ${state.currentGuess}",
            style = MaterialTheme.typography.titleLarge
        )

        HorizontalDivider()

        // Submitted guesses
        state.guesses.forEach { guess ->

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                guess.word.forEachIndexed { index, letter ->

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = letter.toString(),
                            style = MaterialTheme.typography.titleLarge
                        )

                        Text(
                            text = guess.letterStates[index]
                                .name
                                .take(3)
                        )
                    }
                }
            }
        }

        HorizontalDivider()

        // Keyboard
        val rows = listOf(
            "QWERTYUIOP",
            "ASDFGHJKL",
            "ZXCVBNM"
        )

        rows.forEach { row ->

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                row.forEach { letter ->

                    Button(
                        onClick = {
                            viewModel.addLetter(letter)
                        },
                        modifier = Modifier.size(48.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(letter.toString())
                    }
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Button(
                onClick = {
                    viewModel.removeLetter()
                }
            ) {
                Text("DELETE")
            }

            Button(
                onClick = {
                    viewModel.submitGuess()
                }
            ) {
                Text("ENTER")
            }

            Button(
                onClick = {
                    viewModel.restart()
                }
            ) {
                Text("RESTART")
            }
        }
    }
}