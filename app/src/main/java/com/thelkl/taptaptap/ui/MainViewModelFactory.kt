package com.thelkl.taptaptap.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.thelkl.taptaptap.model.HighscoreRepository

class MainViewModelFactory(private val highscoreRepository: HighscoreRepository, private val app: Application) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return MainViewModel(highscoreRepository, app) as T
    }
}