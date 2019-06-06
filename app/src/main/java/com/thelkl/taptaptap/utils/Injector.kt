package com.thelkl.taptaptap.utils

import android.app.Application
import android.content.Context
import com.thelkl.taptaptap.model.HighscoreDatabase
import com.thelkl.taptaptap.model.HighscoreRepository
import com.thelkl.taptaptap.ui.MainViewModelFactory

object Injector {

    fun provideMainViewModelFactory(app: Application): MainViewModelFactory {
        val highscoreRepository = HighscoreRepository.getInstance(HighscoreDatabase.getInstance(app.getSharedPreferences(
            SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)).localHighscoreDAO)
        return MainViewModelFactory(highscoreRepository, app)
    }
}