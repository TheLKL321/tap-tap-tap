package com.thelkl.taptaptap

import android.content.Context
import android.content.SharedPreferences
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
import kotlin.collections.ArrayList


class MainActivity : FragmentActivity(), EndgameDialogFragment.EndgameDialogListener {
    private lateinit var sharedPrefs: SharedPreferences

    // gameplay
    private var taps = 0
    private val encouragementArray = arrayListOf("TAP", "FASTER", "KEEP GOING", "YOU CAN DO IT", "DON'T STOP", "AMAZING")
    private var currentEncouragement = 0
    private lateinit var startCountdownTimer: CountDownTimer
    private lateinit var gameCountdownTimer: CountDownTimer
    private var currentStartTime: Double = START_COUNTDOWN_TIME.toDouble()
    private var currentGameTime: Double = GAMEPLAY_COUNTDOWN_TIME.toDouble()

    // highscore keeping
    private lateinit var highscoreRecyclerView: RecyclerView
    private lateinit var highscoreRecyclerViewManager: RecyclerView.LayoutManager
    private lateinit var highscoreRecyclerViewAdapter: RecyclerView.Adapter<HighscoreRecyclerAdapter.HighscoreViewHolder>
    private lateinit var highscoreRecordArray: ArrayList<Pair<String, String>>  // <taps, timestamp>
    private var lastGameTimestamp = "<TIMESTAMP NOT SET>"
    private var lastGameIfHighscore = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        hideFragment(supportFragmentManager, gameplayFragment)

        // Get shared preferences
        sharedPrefs = applicationContext.getSharedPreferences(SHARED_PREFERENCES_KEY, Context.MODE_PRIVATE)
        val serializedRecordArray = sharedPrefs.getString(SHARED_PREFERENCES_HIGHSCORE_DATASET_KEY, null)
        highscoreRecordArray = if (serializedRecordArray != null) deserialize(serializedRecordArray) else ArrayList()

        // Create timers
        gameCountdownTimer = object: CountDownTimer(GAMEPLAY_COUNTDOWN_TIME.toLong() * 1000, 100) {
            override fun onTick(millisUntilFinished: Long) {
                gameCountdownText.text = Math.ceil(currentGameTime).toInt().toString()
                currentGameTime -= 0.1
            }

            override fun onFinish() {
                gameCountdownText.text = "0"
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

    override fun onPause() {
        super.onPause()

        // Save highscores in shared preferences
        sharedPrefs.edit().putString(SHARED_PREFERENCES_HIGHSCORE_DATASET_KEY, serialize(highscoreRecordArray)).apply()

        // Exit current game
        if (gameplayFragment.isVisible) {
            hideFragment(supportFragmentManager, gameplayFragment)
            resetGame()
        }
    }

    fun onPlayPressed(@Suppress("UNUSED_PARAMETER") view: View) {
        startGame()
    }

    fun onTap(@Suppress("UNUSED_PARAMETER") view: View) {
        taps++
        gameScoreText.text = taps.toString()
        if (taps / 20 > currentEncouragement) {
            currentEncouragement++
            gameMainText.text = encouragementArray[currentEncouragement]
        }
    }

    override fun onBackPressed() {
        if (gameplayFragment.isVisible) {
            hideFragment(supportFragmentManager, gameplayFragment)
            resetGame()
        } else
            super.onBackPressed()
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        endGame()
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        endGame()
        startGame()
    }

    // Begin a new game
    private fun startGame() {
        gameCountdownText.text = "5"
        gameMainText.text = getString(R.string.tap)
        gameScoreText.text = "0"

        val currentDate = Date()
        lastGameTimestamp = DateFormat.format("dd/MM hh:mm:ss", currentDate).toString()

        startCountdownTimer.start()

        showFragment(supportFragmentManager, gameplayFragment)
    }

    // End a current game
    private fun endGame() {
        hideFragment(supportFragmentManager, gameplayFragment)
        saveRecord()
        resetGame()
    }

    // Register a potential highscore
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

    // Reset values after the game ends
    private fun resetGame() {
        gameCountdownTimer.cancel()
        startCountdownTimer.cancel()
        taps = 0
        startCountdownLayout.visibility = View.VISIBLE
        currentStartTime = START_COUNTDOWN_TIME.toDouble()
        currentGameTime = GAMEPLAY_COUNTDOWN_TIME.toDouble()
    }
}