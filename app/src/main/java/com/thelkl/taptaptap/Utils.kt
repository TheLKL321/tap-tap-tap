package com.thelkl.taptaptap

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

fun showFragment(fm: FragmentManager, fragment: Fragment) {
    fm.beginTransaction().show(fragment).commit()
}

fun hideFragment(fm: FragmentManager, fragment: Fragment) {
    fm.beginTransaction().hide(fragment).commit()
}