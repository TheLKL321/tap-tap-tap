package com.thelkl.taptaptap.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.thelkl.taptaptap.R
import com.thelkl.taptaptap.Record

class HighscoreRecyclerAdapter(private val dataset: ArrayList<Record>) :
    RecyclerView.Adapter<HighscoreRecyclerAdapter.HighscoreViewHolder>() {

    class HighscoreViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HighscoreViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.highscore_recycler_row, parent, false)

        return HighscoreViewHolder(view)
    }

    override fun onBindViewHolder(holder: HighscoreViewHolder, position: Int) {
        holder.view.findViewById<TextView>(R.id.recordScoreText).text = dataset[position].scoreText
        holder.view.findViewById<TextView>(R.id.recordTimestampText).text = dataset[position].timestampText
    }

    override fun getItemCount() = dataset.size
}