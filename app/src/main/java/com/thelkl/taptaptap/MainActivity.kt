package com.thelkl.taptaptap

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.View
import kotlinx.android.synthetic.main.gameplay_fragment.*
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : FragmentActivity() {
    private var taps = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        hideFragment(supportFragmentManager, gameplayFragment)
    }

    fun startGame(@Suppress("UNUSED_PARAMETER") view: View) {
        gameCountdownText.text = "5"
        gameMainText.text = "TAP"
        gameScoreText.text = "0"
        showFragment(supportFragmentManager, gameplayFragment)
    }

    fun tap(@Suppress("UNUSED_PARAMETER") view: View) {
        taps++
        gameScoreText.text = taps.toString()
    }

    override fun onBackPressed() {
        if (gameplayFragment.isVisible) {
            hideFragment(supportFragmentManager, gameplayFragment)
            taps = 0
        } else
            super.onBackPressed()
    }
}