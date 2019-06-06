package com.thelkl.taptaptap.ui

import android.app.Application
import android.os.CountDownTimer
import android.text.format.DateFormat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.thelkl.taptaptap.R
import com.thelkl.taptaptap.Record
import com.thelkl.taptaptap.model.HighscoreRepository
import com.thelkl.taptaptap.utils.GAMEPLAY_COUNTDOWN_TIME
import com.thelkl.taptaptap.utils.START_COUNTDOWN_TIME
import java.util.*

class MainViewModel(private val highscoreRepository: HighscoreRepository, app: Application) : AndroidViewModel(app) {
    private val encouragementArray = arrayListOf(
        getApplication<Application>().resources.getString(R.string.tap),
        getApplication<Application>().resources.getString(R.string.first_encouragement),
        getApplication<Application>().resources.getString(R.string.second_encouragement),
        getApplication<Application>().resources.getString(R.string.third_encouragement),
        getApplication<Application>().resources.getString(R.string.fourth_encouragement),
        getApplication<Application>().resources.getString(R.string.fifth_encouragement))

    private var tapCount = 0
    private var currentStartTime: Double = START_COUNTDOWN_TIME.toDouble()
    private var currentGameTime: Double = GAMEPLAY_COUNTDOWN_TIME.toDouble()
    private var lastGameTimestamp = "<TIMESTAMP NOT SET>"
    private var currentEncouragementIndex = 0

    var ifHighscore = false     // if the last game ended with a highscore
        private set

    // LiveData
    private val taps = MutableLiveData<Int>()
    private val gameCountdown = MutableLiveData<String>()
    private val startCountdown = MutableLiveData<String>()
    private val ifGameFinished = MutableLiveData<Boolean>()
    private val ifStartFinished = MutableLiveData<Boolean>()
    private val currentEncouragement = MutableLiveData<String>()

    // Timers
    private val gameCountdownTimer = object: CountDownTimer(GAMEPLAY_COUNTDOWN_TIME.toLong() * 1000, 100) {
        override fun onTick(millisUntilFinished: Long) {
            gameCountdown.value = Math.ceil(currentGameTime).toInt().toString()
            currentGameTime -= 0.1
        }

        override fun onFinish() {
            gameCountdown.value = "0"
            ifHighscore = addHighscore(Record(timestampText = lastGameTimestamp, scoreText = tapCount.toString()))
            ifGameFinished.value = true
        }
    }

    private val startCountdownTimer = object: CountDownTimer(START_COUNTDOWN_TIME.toLong() * 1000, 100) {
        override fun onTick(millisUntilFinished: Long) {
            startCountdown.value = Math.ceil(currentStartTime).toInt().toString()
            currentStartTime -= 0.1
        }

        override fun onFinish() {
            gameCountdownTimer.start()
            ifStartFinished.value = true
        }
    }

    init {
        taps.value = tapCount
        gameCountdown.value = currentGameTime.toInt().toString()
        ifGameFinished.value = false
        startCountdown.value = currentStartTime.toInt().toString()
        ifStartFinished.value = false
        currentEncouragement.value = encouragementArray[currentEncouragementIndex]
    }

    fun startGame() {
        lastGameTimestamp = DateFormat.format("dd/MM hh:mm:ss", Date()).toString()
        startCountdownTimer.start()
    }

    fun resetGame() {
        gameCountdownTimer.cancel()
        startCountdownTimer.cancel()

        tapCount = 0
        taps.value = tapCount

        currentGameTime = GAMEPLAY_COUNTDOWN_TIME.toDouble()
        gameCountdown.value = currentGameTime.toInt().toString()
        ifGameFinished.value = false

        currentStartTime = START_COUNTDOWN_TIME.toDouble()
        startCountdown.value = currentStartTime.toInt().toString()
        ifStartFinished.value = false

        currentEncouragementIndex = 0
        currentEncouragement.value = encouragementArray[currentEncouragementIndex]
    }

    fun registerATap() {
        tapCount++
        taps.value = tapCount
        if (tapCount / 20 > currentEncouragementIndex) {
            currentEncouragementIndex++
            currentEncouragement.value = encouragementArray[currentEncouragementIndex]
        }
    }

    fun addHighscore(record: Record) : Boolean = highscoreRepository.addHighscore(record)

    fun getLocalHighscores() = highscoreRepository.getLocalHighscores()
    fun getTaps() = taps as LiveData<Int>
    fun getGameCountdown() = gameCountdown as LiveData<String>
    fun getIfGameFinished() = ifGameFinished as LiveData<Boolean>
    fun getStartCountdown() = startCountdown as LiveData<String>
    fun getIfStartFinished() = ifStartFinished as LiveData<Boolean>
    fun getCurrentEncouragement() = currentEncouragement as LiveData<String>
}