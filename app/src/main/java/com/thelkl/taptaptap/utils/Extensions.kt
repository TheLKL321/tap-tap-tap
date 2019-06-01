package com.thelkl.taptaptap.utils

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

fun FragmentManager.showFragment(fragment: Fragment) {
    this.beginTransaction().show(fragment).commit()
}

fun FragmentManager.hideFragment(fragment: Fragment) {
    this.beginTransaction().hide(fragment).commit()
}