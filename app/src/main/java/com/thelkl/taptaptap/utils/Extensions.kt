package com.thelkl.taptaptap.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

fun FragmentManager.showFragment(fragment: Fragment) {
    this.beginTransaction().show(fragment).commit()
}

fun FragmentManager.hideFragment(fragment: Fragment) {
    this.beginTransaction().hide(fragment).commit()
}