package com.thelkl.taptaptap

import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.view.View
import kotlinx.android.synthetic.main.gameplay_fragment.*
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : FragmentActivity(), EndgameDialogFragment.EndgameDialogListener {
    private var taps = 0
    private lateinit var startCountdownTimer: CountDownTimer
    private lateinit var gameCountdownTimer: CountDownTimer
    var currentStartTime: Double = START_COUNTDOWN_TIME.toDouble()
    var currentGameTime: Double = GAMEPLAY_COUNTDOWN_TIME.toDouble()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        hideFragment(supportFragmentManager, gameplayFragment)

        gameCountdownTimer = object: CountDownTimer(GAMEPLAY_COUNTDOWN_TIME.toLong() * 1000, 100) {
            override fun onTick(millisUntilFinished: Long) {
                gameCountdownText.text = Math.ceil(currentGameTime).toInt().toString()
                currentGameTime -= 0.1
            }

            override fun onFinish() {
                newEndgameDialogInstance(taps).show(supportFragmentManager, "endgame_dialog")
            }
        }

        startCountdownTimer = object: CountDownTimer(START_COUNTDOWN_TIME.toLong() * 1000, 100) {
            override fun onTick(millisUntilFinished: Long) {
                startCountdownText.text = Math.ceil(currentStartTime).toInt().toString()
                currentStartTime -= 0.1
            }

            override fun onFinish() {
                gameCountdownTimer.start()
                startCountdownLayout.visibility = View.INVISIBLE
            }
        }
    }

    fun onPlayPressed(@Suppress("UNUSED_PARAMETER") view: View) {
        startGame()
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

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        hideFragment(supportFragmentManager, gameplayFragment)
        resetGame()
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        hideFragment(supportFragmentManager, gameplayFragment)
        resetGame()
        startGame()
    }

    private fun startGame() {
        gameCountdownText.text = "5"
        gameMainText.text = "TAP"
        gameScoreText.text = "0"

        startCountdownTimer.start()

        showFragment(supportFragmentManager, gameplayFragment)
    }

    private fun resetGame() {
        startCountdownTimer.cancel()
        taps = 0
        startCountdownLayout.visibility = View.VISIBLE
        currentStartTime = START_COUNTDOWN_TIME.toDouble()
        currentGameTime = GAMEPLAY_COUNTDOWN_TIME.toDouble()
    }
}