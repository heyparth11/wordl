package com.example.wordle.data.network

import com.example.wordle.data.model.DictionaryResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface DictionaryApi {

    @GET("api/v2/entries/en/{word}")
    suspend fun getWordDefinition(
        @Path("word") word: String
    ): List<DictionaryResponse>
}