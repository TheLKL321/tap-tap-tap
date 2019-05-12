package com.thelkl.taptaptap

import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.format.DateFormat
import android.view.View
import kotlinx.android.synthetic.main.gameplay_fragment.*
import kotlinx.android.synthetic.main.main_activity.*
import java.util.*

class MainActivity : FragmentActivity(), EndgameDialogFragment.EndgameDialogListener {
    private var taps = 0
    private lateinit var startCountdownTimer: CountDownTimer
    private lateinit var gameCountdownTimer: CountDownTimer
    var currentStartTime: Double = START_COUNTDOWN_TIME.toDouble()
    var currentGameTime: Double = GAMEPLAY_COUNTDOWN_TIME.toDouble()

    private lateinit var highscoreRecyclerView: RecyclerView
    private lateinit var highscoreRecyclerViewManager: RecyclerView.LayoutManager
    private lateinit var highscoreRecyclerViewAdapter: RecyclerView.Adapter<HighscoreRecyclerAdapter.HighscoreViewHolder>
    private var highscoreRecordArray = arrayListOf<Pair<String, String>>()  // <taps, timestamp>
    private var lastGameTimestamp = "<TIMESTAMP NOT SET>"
    private var lastGameIfHighscore = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        hideFragment(supportFragmentManager, gameplayFragment)

        // Create timers
        gameCountdownTimer = object: CountDownTimer(GAMEPLAY_COUNTDOWN_TIME.toLong() * 1000, 100) {
            override fun onTick(millisUntilFinished: Long) {
                gameCountdownText.text = Math.ceil(currentGameTime).toInt().toString()
                currentGameTime -= 0.1
            }

            override fun onFinish() {
                lastGameIfHighscore = highscoreRecordArray.size < 5 ||
                        ((highscoreRecordArray.minBy { it.first.toInt() })?.first ?: "-1")!!.toInt() < taps
                newEndgameDialogInstance(taps, lastGameIfHighscore).show(supportFragmentManager, "endgame_dialog")
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

        // Create RecyclerView
        highscoreRecyclerViewManager = LinearLayoutManager(this)
        highscoreRecyclerViewAdapter = HighscoreRecyclerAdapter(highscoreRecordArray)
        highscoreRecyclerView = highscoreRecycler.apply {
            setHasFixedSize(true)
            layoutManager = highscoreRecyclerViewManager
            adapter = highscoreRecyclerViewAdapter
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
        saveRecord()
        resetGame()
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        hideFragment(supportFragmentManager, gameplayFragment)
        saveRecord()
        resetGame()
        startGame()
    }

    private fun startGame() {
        gameCountdownText.text = "5"
        gameMainText.text = getString(R.string.tap)
        gameScoreText.text = "0"

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            lastGameTimestamp = DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS")
                .withZone(TimeZone.getDefault().toZoneId())
                .format(Instant.now())
        }*/

        val currentDate = Date()
        lastGameTimestamp = DateFormat.format("dd/MM hh:mm:ss", currentDate).toString()

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

    private fun saveRecord() {
        if (lastGameIfHighscore) {
            val record = Pair(taps.toString(), lastGameTimestamp)
            if (highscoreRecordArray.size == 5)
                highscoreRecordArray[4] = record
            else
                highscoreRecordArray.add(record)
        }
        highscoreRecordArray.sortByDescending { it.first.toInt() }
        highscoreRecyclerViewAdapter.notifyDataSetChanged()
    }
}