package com.example.wordle.domain

import com.example.wordle.data.WordRepository

sealed class GuessSubmissionResult {

    data object TooShort : GuessSubmissionResult()

    data object InvalidWord : GuessSubmissionResult()

    data object Accepted : GuessSubmissionResult()
}

class WordleGame(
    private val wordRepository: WordRepository
) {

    companion object {
        const val WORD_LENGTH = 5
        const val MAX_ATTEMPTS = 6
    }

//    var targetWord: String = wordRepository.getRandomWord()
//        private set

    var targetWord: String = "APPLE"

    private val guesses = mutableListOf<GuessResult>()

    var currentGuess: String = ""
        private set

    var status: GameStatus = GameStatus.PLAYING
        private set

    val guessResults: List<GuessResult>
        get() = guesses.toList()

    fun addLetter(letter: Char) {

        if (status != GameStatus.PLAYING) return

        if (currentGuess.length >= WORD_LENGTH) return

        if (!letter.isLetter()) return

        currentGuess += letter.uppercaseChar()
    }

    fun removeLetter() {

        if (status != GameStatus.PLAYING) return

        if (currentGuess.isEmpty()) return

        currentGuess = currentGuess.dropLast(1)
    }

    fun submitGuess(): GuessSubmissionResult {

        if (status != GameStatus.PLAYING) {
            return GuessSubmissionResult.Accepted
        }

        if (currentGuess.length != WORD_LENGTH) {
            return GuessSubmissionResult.TooShort
        }

        if (!wordRepository.isValidWord(currentGuess)) {
            return GuessSubmissionResult.InvalidWord
        }

        val result = evaluateGuess(
            guess = currentGuess,
            target = targetWord
        )

        guesses.add(
            GuessResult(
                word = currentGuess,
                letterStates = result
            )
        )

        if (currentGuess == targetWord) {
            status = GameStatus.WON
        } else if (guesses.size >= MAX_ATTEMPTS) {
            status = GameStatus.LOST
        }

        currentGuess = ""

        return GuessSubmissionResult.Accepted
    }

    fun restart() {

        targetWord = wordRepository.getRandomWord()

        guesses.clear()

        currentGuess = ""

        status = GameStatus.PLAYING
    }

    private fun evaluateGuess(
        guess: String,
        target: String
    ): List<LetterState> {

        val result = MutableList(WORD_LENGTH) {
            LetterState.ABSENT
        }

        val remainingLetters = mutableMapOf<Char, Int>()

        // Pass 1: exact matches
        for (i in target.indices) {

            if (guess[i] == target[i]) {
                result[i] = LetterState.CORRECT
            } else {
                remainingLetters[target[i]] =
                    (remainingLetters[target[i]] ?: 0) + 1
            }
        }

        // Pass 2: misplaced matches
        for (i in guess.indices) {

            if (result[i] == LetterState.CORRECT) {
                continue
            }

            val count =
                remainingLetters[guess[i]] ?: 0

            if (count > 0) {

                result[i] = LetterState.PRESENT

                remainingLetters[guess[i]] =
                    count - 1
            }
        }
        return result
    }



}