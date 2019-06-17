package com.thelkl.taptaptap.model

import android.content.Context
import android.content.SharedPreferences

class HighscoreDatabase private constructor(sharedPrefs: SharedPreferences, context: Context) {
    var localHighscoreDAO = LocalHighscoreDAO(sharedPrefs)
        private set
    var globalHighscoreDAO = GlobalHighscoreDAO(context)
        private set

    companion object {
        @Volatile private var instance: HighscoreDatabase? = null

        fun getInstance(sharedPrefs: SharedPreferences, context: Context) =
            instance ?: synchronized(this) {
                instance ?: HighscoreDatabase(sharedPrefs, context).also { instance = it }
            }
    }
}