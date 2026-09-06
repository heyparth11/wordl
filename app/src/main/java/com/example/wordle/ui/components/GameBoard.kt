package com.example.wordle.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.keyframes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.example.wordle.domain.GameState
import com.example.wordle.domain.LetterState
import com.example.wordle.domain.WordleGame

@Composable
fun GameBoard(
    state: GameState,
    modifier: Modifier = Modifier
) {
    val shakeOffset = remember { Animatable(0f) }

    LaunchedEffect(state.shakeTrigger) {
        if (state.shakeTrigger > 0) {
            shakeOffset.snapTo(0f)
            shakeOffset.animateTo(
                targetValue = 0f,
                animationSpec = keyframes {
                    durationMillis = 400
                    0f at 0
                    (-12f) at 50
                    12f at 100
                    (-9f) at 150
                    9f at 200
                    (-5f) at 250
                    5f at 300
                    (-2f) at 350
                    0f at 400
                }
            )
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        for (row in 0 until WordleGame.MAX_ATTEMPTS) {
            val guess = state.guesses.getOrNull(row)
            val isCurrentRow = row == state.guesses.size

            val letters = when {
                guess != null -> {
                    guess.word.mapIndexed { index, char ->
                        char to guess.letterStates[index]
                    }
                }
                isCurrentRow -> {
                    state.currentGuess.map {
                        it to LetterState.UNKNOWN
                    }
                }
                else -> {
                    emptyList()
                }
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .graphicsLayer {
                        if (isCurrentRow) {
                            translationX = shakeOffset.value
                        }
                    }
                    .padding(vertical = 3.dp)
            ) {
                for (column in 0 until WordleGame.WORD_LENGTH) {
                    val letter = letters.getOrNull(column)?.first ?: ' '
                    val letterState = letters.getOrNull(column)?.second ?: LetterState.UNKNOWN

                    LetterTile(
                        letter = letter,
                        state = letterState,
                        animationDelayMs = column * 200
                    )
                }
            }
        }
    }
}