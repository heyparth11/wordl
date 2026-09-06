package com.example.wordle.data

import android.content.Context
import com.example.wordle.R

class WordRepository(private val context: Context) {

    private val words: Set<String> by lazy {
        context.resources.openRawResource(R.raw.words)
            .bufferedReader()
            .useLines { lines ->
                lines.map { it.trim().uppercase() }
                    .filter { it.length == 5 }
                    .toSet()
            }
    }

    private val wordsList: List<String> by lazy {
        words.toList()
    }

    fun getRandomWord(): String {
        return if (wordsList.isNotEmpty()) wordsList.random() else "APPLE"
    }

    fun isValidWord(word: String): Boolean {
        return word.uppercase() in words
    }
}