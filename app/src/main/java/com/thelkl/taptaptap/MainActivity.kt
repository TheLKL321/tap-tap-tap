package com.thelkl.taptaptap

import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.FragmentActivity
import android.view.View
import kotlinx.android.synthetic.main.gameplay_fragment.*
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : FragmentActivity() {
    private var taps = 0
    private lateinit var startCountdownTimer: CountDownTimer
    var currentTime: Double = START_COUNTDOWN_TIME.toDouble()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        hideFragment(supportFragmentManager, gameplayFragment)

        startCountdownTimer = object: CountDownTimer(START_COUNTDOWN_TIME.toLong() * 1000, 100) {
            override fun onTick(millisUntilFinished: Long) {
                startCountdownText.text = Math.ceil(currentTime).toInt().toString()
                currentTime -= 0.1
            }

            override fun onFinish() {
                startCountdownLayout.visibility = View.INVISIBLE
            }
        }
    }

    fun startGame(@Suppress("UNUSED_PARAMETER") view: View) {
        gameCountdownText.text = "5"
        gameMainText.text = "TAP"
        gameScoreText.text = "0"

        startCountdownTimer.start()

        showFragment(supportFragmentManager, gameplayFragment)
    }

    fun tap(@Suppress("UNUSED_PARAMETER") view: View) {
        taps++
        gameScoreText.text = taps.toString()
    }

    override fun onBackPressed() {
        if (gameplayFragment.isVisible) {
            hideFragment(supportFragmentManager, gameplayFragment)
            resetGame()
        } else
            super.onBackPressed()
    }

    private fun resetGame() {
        startCountdownTimer.cancel()
        taps = 0
        startCountdownLayout.visibility = View.VISIBLE
        currentTime = START_COUNTDOWN_TIME.toDouble()
    }
}