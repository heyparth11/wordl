package com.example.wordle.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordle.data.DictionaryRepository
import com.example.wordle.data.StatsRepository
import com.example.wordle.data.WordRepository
import com.example.wordle.domain.GameState
import com.example.wordle.domain.GameStatus
import com.example.wordle.domain.GuessSubmissionResult
import com.example.wordle.domain.LetterState
import com.example.wordle.domain.WordleGame
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WordleViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = WordRepository(application)
    private val dictionaryRepository = DictionaryRepository()
    private val statsRepository = StatsRepository(application)

    private val game = WordleGame(repository)
    private var hasRecordedStatsForCurrentGame = false

    private val _state = MutableStateFlow(
        GameState(
            targetWord = game.targetWord,
            stats = statsRepository.getStats()
        )
    )

    val state: StateFlow<GameState> =
        _state.asStateFlow()

    init {
        fetchDefinition(game.targetWord)
    }

    private fun fetchDefinition(word: String) {
        viewModelScope.launch {
            val def = dictionaryRepository.getDefinition(word.lowercase())
            _state.update {
                it.copy(definition = def ?: "No definition found.")
            }
        }
    }

    fun addLetter(letter: Char) {

        game.addLetter(letter)

        updateState()
    }

    fun removeLetter() {

        game.removeLetter()

        updateState()
    }

    fun submitGuess() {
        val result = game.submitGuess()

        if (result is GuessSubmissionResult.Accepted && !hasRecordedStatsForCurrentGame) {
            if (game.status == GameStatus.WON) {
                hasRecordedStatsForCurrentGame = true
                val updatedStats = statsRepository.recordGameResult(won = true, attempts = game.guessResults.size)
                _state.update { it.copy(stats = updatedStats) }
            } else if (game.status == GameStatus.LOST) {
                hasRecordedStatsForCurrentGame = true
                val updatedStats = statsRepository.recordGameResult(won = false, attempts = game.guessResults.size)
                _state.update { it.copy(stats = updatedStats) }
            }
        }

        var shakeTrigger = _state.value.shakeTrigger
        val message = when (result) {
            GuessSubmissionResult.Accepted -> when (game.status) {
                GameStatus.WON -> "You Win! 🎉"
                GameStatus.LOST -> "Game Over! Word: ${game.targetWord}"
                GameStatus.PLAYING -> null
            }
            GuessSubmissionResult.InvalidWord -> {
                shakeTrigger++
                "Not a valid word"
            }
            GuessSubmissionResult.TooShort -> {
                shakeTrigger++
                "Not enough letters"
            }
        }

        updateState(message = message, shakeTrigger = shakeTrigger)
    }

    fun restart() {

        game.restart()
        hasRecordedStatsForCurrentGame = false

        updateState()
        _state.update { it.copy(definition = null, stats = statsRepository.getStats()) }
        fetchDefinition(game.targetWord)
    }

    private fun updateState(
        message: String? = null,
        shakeTrigger: Int = _state.value.shakeTrigger
    ) {

        _state.value = _state.value.copy(
            targetWord = game.targetWord,
            guesses = game.guessResults,
            currentGuess = game.currentGuess,
            gameStatus = game.status,
            keyboardState = calculateKeyboardState(),
            message = message,
            shakeTrigger = shakeTrigger
        )
    }

    private fun calculateKeyboardState(): Map<Char, LetterState> {

        val states = mutableMapOf<Char, LetterState>()

        for (guess in game.guessResults) {

            for (i in guess.word.indices) {

                val letter = guess.word[i]
                val newState = guess.letterStates[i]

                val oldState = states[letter]

                states[letter] =
                    if (oldState == null ||
                        newState.priority() > oldState.priority()
                    ) {
                        newState
                    } else {
                        oldState
                    }
            }
        }

        return states
    }

    private fun LetterState.priority(): Int {
        return when (this) {
            LetterState.UNKNOWN -> 0
            LetterState.ABSENT -> 1
            LetterState.PRESENT -> 2
            LetterState.CORRECT -> 3
        }
    }
}