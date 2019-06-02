package com.thelkl.taptaptap

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class HighscoreRecyclerAdapter(private val dataset: ArrayList<Pair<String, String>>) :
    RecyclerView.Adapter<HighscoreRecyclerAdapter.HighscoreViewHolder>() {

    class HighscoreViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HighscoreViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.highscore_recycler_row, parent, false)

        return HighscoreViewHolder(view)
    }

    override fun onBindViewHolder(holder: HighscoreViewHolder, position: Int) {
        holder.view.findViewById<TextView>(R.id.recordScoreText).text = dataset[position].first
        holder.view.findViewById<TextView>(R.id.recordTimestampText).text = dataset[position].second
    }

    override fun getItemCount() = dataset.size
}