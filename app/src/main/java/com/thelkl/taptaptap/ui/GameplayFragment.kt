package com.thelkl.taptaptap.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.thelkl.taptaptap.R

class GameplayFragment : Fragment() {
    companion object;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.gameplay_fragment, container, false)
    }
}