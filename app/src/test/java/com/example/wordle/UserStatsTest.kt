package com.example.wordle

import com.example.wordle.domain.UserStats
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class UserStatsTest {

    @Test
    fun testDefaultStats() {
        val stats = UserStats()
        assertEquals(0, stats.gamesPlayed)
        assertEquals(0, stats.gamesWon)
        assertEquals(0, stats.currentStreak)
        assertEquals(0, stats.maxStreak)
        assertNull(stats.bestTry)
        assertEquals("—", stats.bestTryDisplay)
        assertEquals(0, stats.winPercentage)
    }

    @Test
    fun testWinPercentageCalculation() {
        val stats = UserStats(
            gamesPlayed = 10,
            gamesWon = 8
        )
        assertEquals(80, stats.winPercentage)
    }

    @Test
    fun testBestTryDisplay() {
        assertEquals("1st", UserStats(bestTry = 1).bestTryDisplay)
        assertEquals("2nd", UserStats(bestTry = 2).bestTryDisplay)
        assertEquals("3rd", UserStats(bestTry = 3).bestTryDisplay)
        assertEquals("4th", UserStats(bestTry = 4).bestTryDisplay)
        assertEquals("5th", UserStats(bestTry = 5).bestTryDisplay)
        assertEquals("6th", UserStats(bestTry = 6).bestTryDisplay)
        assertEquals("—", UserStats(bestTry = null).bestTryDisplay)
    }
}
