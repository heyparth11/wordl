package com.example.wordle.ui.components


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wordle.domain.LetterState
import com.example.wordle.ui.theme.absent
import com.example.wordle.ui.theme.primary
import com.example.wordle.ui.theme.secondary
import com.example.wordle.ui.theme.unknown

@Composable
fun KeyboardKey(
    modifier: Modifier = Modifier,
    letter: Char? = null,
    text: String? = null,
    state: LetterState = LetterState.UNKNOWN,
    onClick: () -> Unit,
) {

    val backgroundColor = when (state) {
        LetterState.UNKNOWN ->
            unknown

        LetterState.ABSENT ->
            absent

        LetterState.PRESENT ->
            secondary

        LetterState.CORRECT ->
            primary
    }

    Button(
        onClick = onClick,
        modifier = modifier
            .height(45.dp),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(4.dp)

    ) {
        Text(
            text = text ?: letter?.toString() ?: "",
            fontSize = if (text != null) 14.sp else 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}