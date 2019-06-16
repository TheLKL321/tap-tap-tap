package com.thelkl.taptaptap.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.thelkl.taptaptap.Record

class GlobalHighscoreDAO {
    private val highscoreRecordArray = ArrayList<Record>()
    private val globalHighscores = MutableLiveData<ArrayList<Record>>()

    init {
        globalHighscores.value = highscoreRecordArray
    }

    fun refreshData() {
        // TODO
    }

    fun addHighscore(record: Record) {
        // TODO
    }

    fun getHighscores() = globalHighscores as LiveData<ArrayList<Record>>
}