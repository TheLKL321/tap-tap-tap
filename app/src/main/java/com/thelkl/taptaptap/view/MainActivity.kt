package com.thelkl.taptaptap.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.thelkl.taptaptap.R
import com.thelkl.taptaptap.Record
import com.thelkl.taptaptap.utils.Injector
import com.thelkl.taptaptap.utils.hideFragment
import com.thelkl.taptaptap.utils.newEndgameDialogInstance
import com.thelkl.taptaptap.utils.showFragment
import com.thelkl.taptaptap.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.gameplay_fragment.*
import kotlinx.android.synthetic.main.main_activity.*


class MainActivity : FragmentActivity(), EndgameDialogFragment.EndgameDialogListener {

    // Highscore table
    private lateinit var highscoreRecyclerView: RecyclerView
    private lateinit var highscoreRecyclerViewManager: RecyclerView.LayoutManager
    private lateinit var localHighscoreRecyclerViewAdapter: RecyclerView.Adapter<HighscoreRecyclerAdapter.HighscoreViewHolder>
    private lateinit var globalHighscoreRecyclerViewAdapter: RecyclerView.Adapter<HighscoreRecyclerAdapter.HighscoreViewHolder>
    private var globalHighscoreRecordArray = ArrayList<Record>()
    private var localHighscoreRecordArray = ArrayList<Record>()

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        supportFragmentManager.hideFragment(gameplayFragment)

        initializeUI()
    }

    private fun initializeUI() {
        viewModel = ViewModelProviders.of(this, Injector.provideMainViewModelFactory(application))
            .get(MainViewModel::class.java)

        // Views
        highscoreRecyclerViewManager = LinearLayoutManager(this)
        localHighscoreRecyclerViewAdapter = HighscoreRecyclerAdapter(localHighscoreRecordArray)
        highscoreRecyclerView = highscoreRecycler.apply {
            setHasFixedSize(true)
            this.layoutManager = highscoreRecyclerViewManager
            this.adapter = localHighscoreRecyclerViewAdapter
        }

        globalHighscoreRecyclerViewAdapter = HighscoreRecyclerAdapter(globalHighscoreRecordArray)

        globalHighscoreSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewModel.refreshGlobalData()
                highscoreRecyclerView.adapter = globalHighscoreRecyclerViewAdapter
            } else {
                highscoreRecyclerView.adapter = localHighscoreRecyclerViewAdapter
            }
        }

        // Observers
        viewModel.getLocalHighscores().observe(this, Observer { localHighscores ->
            localHighscoreRecordArray.clear()
            localHighscoreRecordArray.addAll(localHighscores)
            localHighscoreRecyclerViewAdapter.notifyDataSetChanged()
        })

        viewModel.getGlobalHighscores().observe(this, Observer { globalHighscores ->
            globalHighscoreRecordArray.clear()
            globalHighscoreRecordArray.addAll(globalHighscores)
            globalHighscoreRecyclerViewAdapter.notifyDataSetChanged()
        })

        viewModel.getTaps().observe(this, Observer { taps -> gameScoreText.text = taps.toString() })

        viewModel.getGameCountdown().observe(this, Observer { gameCountDownString ->
            gameCountdownText.text = gameCountDownString
        })

        viewModel.getIfGameFinished().observe(this, Observer { ifGameFinished ->
            if (ifGameFinished) {
                newEndgameDialogInstance(viewModel.getTaps().value ?: 0, viewModel.ifHighscore)
                    .show(supportFragmentManager, "endgame_dialog")
            }
        })

        viewModel.getStartCountdown().observe(this, Observer { startCountDownString ->
            startCountdownText.text = startCountDownString
        })

        viewModel.getIfStartFinished().observe(this, Observer { ifStartFinished ->
            if (ifStartFinished)
                startCountdownLayout.visibility = View.INVISIBLE
            else
                startCountdownLayout.visibility = View.VISIBLE
        })

        viewModel.getCurrentEncouragement().observe(this, Observer { encouragement ->
            gameMainText.text = encouragement
        })
    }

    override fun onPause() {
        super.onPause()

        // Exit current game
        if (gameplayFragment.isVisible) {
            supportFragmentManager.hideFragment(gameplayFragment)
            resetGame()
        }
    }

    fun onPlayPressed(@Suppress("UNUSED_PARAMETER") view: View) = startGame()

    fun onTap(@Suppress("UNUSED_PARAMETER") view: View) = viewModel.registerATap()

    override fun onBackPressed() {
        if (gameplayFragment.isVisible) {
            supportFragmentManager.hideFragment(gameplayFragment)
            resetGame()
        } else
            super.onBackPressed()
    }

    override fun onStop() {
        //TODO: cancel all requests
        super.onStop()
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) { endGame() }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        endGame()
        startGame()
    }

    private fun startGame() {
        viewModel.startGame()
        supportFragmentManager.showFragment(gameplayFragment)
    }

    private fun endGame() {
        supportFragmentManager.hideFragment(gameplayFragment)
        resetGame()
    }

    private fun resetGame() = viewModel.resetGame()
}