package com.thelkl.taptaptap

import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.View
import kotlinx.android.synthetic.main.main_activity.*

class MainActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        // Hide the gameplay fragment
        supportFragmentManager.beginTransaction().show(gameplayFragment).commit()
    }

    fun startGame(@Suppress("UNUSED_PARAMETER") view: View) {
        supportFragmentManager.beginTransaction().show(gameplayFragment).commit()
    }
}