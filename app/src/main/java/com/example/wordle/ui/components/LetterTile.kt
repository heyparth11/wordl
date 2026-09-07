package com.example.wordle.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordle.domain.LetterState
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

import com.example.wordle.ui.theme.absent
import com.example.wordle.ui.theme.borderColor
import com.example.wordle.ui.theme.curBorderColor
import com.example.wordle.ui.theme.primary
import com.example.wordle.ui.theme.secondary

import com.example.wordle.ui.theme.LocalWordleColors

@Composable
fun LetterTile(
    letter: Char,
    state: LetterState,
    modifier: Modifier = Modifier,
    animationDelayMs: Int = 0
) {
    val customColors = LocalWordleColors.current

    // Pop animation when entering a letter
    var scale by remember { mutableFloatStateOf(1f) }

    LaunchedEffect(letter) {
        if (letter != ' ' && state == LetterState.UNKNOWN) {
            scale = 1.15f
            delay(80.milliseconds)
            scale = 1f
        }
    }

    val animatedScale by animateFloatAsState(
        targetValue = scale,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "tileScale"
    )

    // 3D Flip rotation animation around X-axis when revealing evaluated state
    val rotationX = remember { Animatable(0f) }
    var displayedState by remember { mutableStateOf(if (state == LetterState.UNKNOWN) LetterState.UNKNOWN else state) }

    LaunchedEffect(state) {
        if (state != LetterState.UNKNOWN && displayedState == LetterState.UNKNOWN) {
            delay(animationDelayMs.milliseconds)
            // Flip up to 90 degrees (edge-on view)
            rotationX.animateTo(
                targetValue = 90f,
                animationSpec = tween(durationMillis = 250, easing = LinearOutSlowInEasing)
            )
            // Reveal the evaluated color at the midpoint
            displayedState = state
            // Flip back to 0 degrees to show the revealed color
            rotationX.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
            )
        } else if (state == LetterState.UNKNOWN) {
            displayedState = LetterState.UNKNOWN
            rotationX.snapTo(0f)
        }
    }

    val backgroundColor = when (displayedState) {
        LetterState.UNKNOWN ->
            MaterialTheme.colorScheme.surface

        LetterState.ABSENT ->
            customColors.absent

        LetterState.PRESENT ->
            customColors.secondary

        LetterState.CORRECT ->
            customColors.primary
    }

    val tileBorderColor = when (displayedState) {
        LetterState.UNKNOWN ->
            if (letter != ' ') customColors.curBorderColor else customColors.borderColor

        else ->
            backgroundColor
    }

    val textColor = if (displayedState == LetterState.UNKNOWN) {
        MaterialTheme.colorScheme.onSurface
    } else {
        Color.White
    }

    Box(
        modifier = modifier
            .graphicsLayer {
                this.rotationX = rotationX.value
                cameraDistance = 16f * density // 3D perspective depth
            }
            .scale(animatedScale)
            .size(56.dp)
            .border(
                width = 2.dp,
                color = tileBorderColor
            )
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (letter == ' ') "" else letter.toString(),
            fontSize = 24.sp,
            fontWeight = FontWeight.ExtraBold,
            color = textColor
        )
    }
}