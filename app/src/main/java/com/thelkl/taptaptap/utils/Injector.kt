package com.thelkl.taptaptap.utils

import android.app.Application
import android.content.Context
import com.thelkl.taptaptap.model.HighscoreDatabase
import com.thelkl.taptaptap.model.HighscoreRepository
import com.thelkl.taptaptap.viewmodel.MainViewModelFactory

object Injector {

    fun provideMainViewModelFactory(app: Application): MainViewModelFactory {
        val database = HighscoreDatabase.getInstance(app.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE), app)
        val highscoreRepository = HighscoreRepository.getInstance(database.localHighscoreDAO, database.globalHighscoreDAO)
        return MainViewModelFactory(highscoreRepository, app)
    }
}