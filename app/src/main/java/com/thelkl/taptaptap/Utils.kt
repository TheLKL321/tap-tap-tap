package com.thelkl.taptaptap

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

fun showFragment(fm: FragmentManager, fragment: Fragment) {
    fm.beginTransaction().show(fragment).commit()
}

fun hideFragment(fm: FragmentManager, fragment: Fragment) {
    fm.beginTransaction().hide(fragment).commit()
}

fun newEndgameDialogInstance(taps: Int, ifHighscore: Boolean): EndgameDialogFragment {
    val fragment = EndgameDialogFragment()
    val bundle = Bundle()
    bundle.putInt(ENDGAME_DIALOG_FRAGMENT_TAPS_KEY, taps)
    bundle.putBoolean(ENDGAME_DIALOG_FRAGMENT_IFHIGHSCORE_KEY, ifHighscore)
    fragment.arguments = bundle

    return fragment
}