package com.example.wordle

import com.example.wordle.data.WordRepository
import com.example.wordle.domain.GameStatus
import com.example.wordle.domain.WordleGame
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WordleGameHintTest {

    private val mockRepository = object : WordRepository(null) {
        override fun getRandomWord(): String = "APPLE"
        override fun isValidWord(word: String): Boolean = true
    }

    @Test
    fun testForfeit() {
        val game = WordleGame(mockRepository)
        assertEquals(GameStatus.PLAYING, game.status)

        game.forfeit()
        assertEquals(GameStatus.LOST, game.status)
    }

    @Test
    fun testUnrevealedIndices() {
        val game = WordleGame(mockRepository)
        val indices = game.getUnrevealedIndices()
        assertEquals(listOf(0, 1, 2, 3, 4), indices)

        val filtered = game.getUnrevealedIndices(alreadyRevealedHints = setOf(0, 2))
        assertEquals(listOf(1, 3, 4), filtered)
    }
}
