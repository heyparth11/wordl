package com.example.wordle.data

import com.example.wordle.data.network.DictionaryApi
import com.example.wordle.data.network.RetrofitInstance

class DictionaryRepository(
    private val api: DictionaryApi = RetrofitInstance.dictionaryApi
) {

    suspend fun getDefinition(word: String): String? {
        return try {

            val response = api.getWordDefinition(word)

            response
                .firstOrNull()
                ?.meanings
                ?.firstOrNull()
                ?.definitions
                ?.firstOrNull()
                ?.definition

        } catch (e: Exception) {
            null
        }
    }
}