package com.example.wordle.data.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.dictionaryapi.dev/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val dictionaryApi: DictionaryApi =
        retrofit.create(DictionaryApi::class.java)
}