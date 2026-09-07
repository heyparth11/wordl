package com.example.wordle

import com.example.wordle.domain.ThemeMode
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class ThemeModeTest {

    @Test
    fun testThemeModeEnumValues() {
        assertEquals(3, ThemeMode.entries.size)
        assertNotNull(ThemeMode.valueOf("SYSTEM"))
        assertNotNull(ThemeMode.valueOf("LIGHT"))
        assertNotNull(ThemeMode.valueOf("DARK"))
    }
}
