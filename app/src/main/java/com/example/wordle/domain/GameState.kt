package com.example.wordle.domain

data class GameState(
    val targetWord: String = "",
    val definition: String? = null,
    val guesses: List<GuessResult> = emptyList(),
    val currentGuess: String = "",
    val gameStatus: GameStatus = GameStatus.PLAYING,
    val keyboardState: Map<Char, LetterState> = emptyMap(),
    val message: String? = null,
    val shakeTrigger: Int = 0,
    val stats: UserStats = UserStats(),
    val letterHints: Map<Int, Char> = emptyMap()
)
