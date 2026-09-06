package com.example.wordle.domain

data class GuessResult(
    val word: String,
    val letterStates: List<LetterState>
)