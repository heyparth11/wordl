package com.example.wordle.data

import android.content.Context
import com.example.wordle.domain.UserStats
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StatsRepository(context: Context) {

    private val prefs = context.getSharedPreferences("wordle_stats_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun getStats(): UserStats {
        val json = prefs.getString("stats_data", null) ?: return UserStats()
        return try {
            val type = object : TypeToken<UserStats>() {}.type
            gson.fromJson<UserStats>(json, type) ?: UserStats()
        } catch (e: Exception) {
            UserStats()
        }
    }

    fun saveStats(stats: UserStats) {
        val json = gson.toJson(stats)
        prefs.edit().putString("stats_data", json).apply()
    }

    fun recordGameResult(won: Boolean, attempts: Int): UserStats {
        val current = getStats()
        val newPlayed = current.gamesPlayed + 1
        val newWon = if (won) current.gamesWon + 1 else current.gamesWon
        val newStreak = if (won) current.currentStreak + 1 else 0
        val newMaxStreak = maxOf(current.maxStreak, newStreak)
        val newBestTry = if (won) {
            if (current.bestTry == null) attempts else minOf(current.bestTry, attempts)
        } else {
            current.bestTry
        }

        val newDist = current.guessDistribution.toMutableMap()
        if (won && attempts in 1..6) {
            newDist[attempts] = (newDist[attempts] ?: 0) + 1
        }

        val updated = current.copy(
            gamesPlayed = newPlayed,
            gamesWon = newWon,
            currentStreak = newStreak,
            maxStreak = newMaxStreak,
            bestTry = newBestTry,
            guessDistribution = newDist
        )

        saveStats(updated)
        return updated
    }
}
