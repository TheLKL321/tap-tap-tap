package com.thelkl.taptaptap.model

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.thelkl.taptaptap.Record
import com.thelkl.taptaptap.utils.SHARED_PREFERENCES_HIGHSCORE_DATASET_KEY
import com.thelkl.taptaptap.utils.deserialize
import com.thelkl.taptaptap.utils.serialize

class LocalHighscoreDAO(private val sharedPrefs: SharedPreferences) {
    private val highscoreRecordArray: ArrayList<Record>  // <taps, timestamp>
    private val localHighscores = MutableLiveData<ArrayList<Record>>()

    init {
        val serializedRecordArray = sharedPrefs.getString(SHARED_PREFERENCES_HIGHSCORE_DATASET_KEY, null)
        highscoreRecordArray = if (serializedRecordArray != null) deserialize(serializedRecordArray) else ArrayList()
        localHighscores.value = highscoreRecordArray
    }

    // Adds a new record and saves it to shared preferences
    fun addHighscore(record: Record) {
        if (highscoreRecordArray.size == 5)
            highscoreRecordArray[4] = record
        else
            highscoreRecordArray.add(record)
        highscoreRecordArray.sortByDescending { it.scoreText.toInt() }
        localHighscores.value = highscoreRecordArray
        sharedPrefs.edit().putString(SHARED_PREFERENCES_HIGHSCORE_DATASET_KEY, serialize(highscoreRecordArray)).apply()
    }

    fun getHighscores() = localHighscores as LiveData<ArrayList<Record>>
}