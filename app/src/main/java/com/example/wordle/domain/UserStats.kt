package com.example.wordle.domain

data class UserStats(
    val gamesPlayed: Int = 0,
    val gamesWon: Int = 0,
    val currentStreak: Int = 0,
    val maxStreak: Int = 0,
    val bestTry: Int? = null,
    val guessDistribution: Map<Int, Int> = mapOf(
        1 to 0,
        2 to 0,
        3 to 0,
        4 to 0,
        5 to 0,
        6 to 0
    )
) {
    val winPercentage: Int
        get() = if (gamesPlayed > 0) (gamesWon * 100) / gamesPlayed else 0

    val bestTryDisplay: String
        get() = when (bestTry) {
            1 -> "1st"
            2 -> "2nd"
            3 -> "3rd"
            4 -> "4th"
            5 -> "5th"
            6 -> "6th"
            else -> "—"
        }
}
