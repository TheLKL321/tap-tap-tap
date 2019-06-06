package com.thelkl.taptaptap.model

import android.content.SharedPreferences

class HighscoreDatabase private constructor(sharedPrefs: SharedPreferences) {
    var localHighscoreDAO = LocalHighscoreDAO(sharedPrefs)
        private set

    companion object {
        @Volatile private var instance: HighscoreDatabase? = null

        fun getInstance(sharedPrefs: SharedPreferences) =
            instance ?: synchronized(this) {
                instance ?: HighscoreDatabase(sharedPrefs).also { instance = it }
            }
    }
}