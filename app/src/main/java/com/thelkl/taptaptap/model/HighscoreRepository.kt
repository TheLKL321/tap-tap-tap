package com.thelkl.taptaptap.model

import com.thelkl.taptaptap.Record

class HighscoreRepository private constructor(private val localHighscoreDAO: LocalHighscoreDAO, private val globalHighscoreDAO: GlobalHighscoreDAO){

    fun cancelGlobalHighscoreRequests() = globalHighscoreDAO.cancelRequests()
    fun refreshGlobalData() = globalHighscoreDAO.refreshData()

    // Tries to add a highscore, returns true if it made it to the list, otherwise false
    fun addLocalHighscore(record: Record) : Boolean {
        val highscoreRecordArray = getLocalHighscores().value ?: ArrayList()
        val ifHighscore = highscoreRecordArray.size < 5 ||
                ((highscoreRecordArray.minBy { it.scoreText.toInt() })?.scoreText ?: "-1")!!.toInt() < record.scoreText.toInt()
        if (ifHighscore)
            localHighscoreDAO.addHighscore(record)

        return ifHighscore
    }

    // Sends a highscore to the server
    fun addGlobalHighscore(record: Record) = globalHighscoreDAO.addHighscore(record)

    fun getLocalHighscores() = localHighscoreDAO.getHighscores()
    fun getGlobalHighscores() = globalHighscoreDAO.getHighscores()

    companion object {
        @Volatile private var instance: HighscoreRepository? = null

        fun getInstance(localHighscoreDAO: LocalHighscoreDAO, globalHighscoreDAO: GlobalHighscoreDAO) =
                instance ?: synchronized(this) {
                    instance ?: HighscoreRepository(localHighscoreDAO, globalHighscoreDAO).also {
                        instance = it
                    }
                }
    }
}