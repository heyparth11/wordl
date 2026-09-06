package com.example.wordle.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.wordle.domain.LetterState
import com.example.wordle.R

@Composable
fun Keyboard(
    keyboardState: Map<Char, LetterState>,
    onKeyPressed: (Char) -> Unit,
    onDelete: () -> Unit,
    onEnter: () -> Unit
) {

    val rows = listOf(
        "QWERTYUIOP",
        "ASDFGHJKL",
        "-ZXCVBNM+"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {

        rows.forEach { row ->

            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {

                row.forEach { letter ->

                    when (letter) {
                        '-' -> {
                            KeyboardKey(
                                text = "⌫",
                                onClick = onDelete,
                                modifier = Modifier.width(54.dp)
                            )
                        }
                        '+' -> {
                            KeyboardKey(
                                text = "EN",
                                onClick = onEnter,
                                modifier = Modifier.width(54.dp)
                            )
                        }
                        else -> {
                            KeyboardKey(
                                letter = letter,
                                state = keyboardState[letter]
                                    ?: LetterState.UNKNOWN,
                                onClick = {
                                    onKeyPressed(letter)
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
    }
}