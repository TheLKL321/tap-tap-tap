package com.thelkl.taptaptap.model

import com.thelkl.taptaptap.Record

class HighscoreRepository private constructor(private val localHighscoreDAO: LocalHighscoreDAO){

    // Tries to add a highscore, returns true if it made it to the list, otherwise false
    fun addHighscore(record: Record) : Boolean {
        val highscoreRecordArray = getLocalHighscores().value ?: ArrayList()
        val ifHighscore = highscoreRecordArray.size < 5 ||
                ((highscoreRecordArray.minBy { it.scoreText.toInt() })?.scoreText ?: "-1")!!.toInt() < record.scoreText.toInt()
        if (ifHighscore)
            localHighscoreDAO.addHighscore(record)

        return ifHighscore
    }

    fun getLocalHighscores() = localHighscoreDAO.getHighscores()

    companion object {
        @Volatile private var instance: HighscoreRepository? = null

        fun getInstance(localHighscoreDAO: LocalHighscoreDAO) =
                instance ?: synchronized(this) {
                    instance ?: HighscoreRepository(localHighscoreDAO).also {
                        instance = it
                    }
                }
    }
}