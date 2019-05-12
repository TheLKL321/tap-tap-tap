package com.thelkl.taptaptap

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class HighscoreRecyclerAdapter(private val dataset: ArrayList<Pair<String, String>>) :
    RecyclerView.Adapter<HighscoreRecyclerAdapter.HighscoreViewHolder>() {

    class HighscoreViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    // invoked by the layout manager
    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): HighscoreViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.highscore_recycler_row, parent, false)

        return HighscoreViewHolder(view)
    }

    // invoked by the layout manager
    override fun onBindViewHolder(holder: HighscoreViewHolder, position: Int) {
        holder.view.findViewById<TextView>(R.id.recordScoreText).text = dataset[position].first
        holder.view.findViewById<TextView>(R.id.recordTimestampText).text = dataset[position].second
    }

    // invoked by the layout manager
    override fun getItemCount() = dataset.size
}