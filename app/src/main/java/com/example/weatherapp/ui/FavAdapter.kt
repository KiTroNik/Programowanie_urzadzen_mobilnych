package com.example.weatherapp.ui


import android.content.Context
import android.content.Intent

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.utils.Constants
import kotlinx.android.synthetic.main.item_favorite.view.*

class FavAdapter(
    var cities: ArrayList<String>,
    var context: Context
) : RecyclerView.Adapter<FavAdapter.FavViewHolder>() {
    inner class FavViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var button: Button = itemView.findViewById(R.id.btnGo)

        init {
            button.setOnClickListener {
                var position = adapterPosition
                Constants.CITY = cities[position]
                context.startActivity(Intent(context, MainActivity::class.java ))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favorite, parent, false)
       return FavViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavViewHolder, position: Int) {
        holder.itemView.apply {
            tvTitle.text = cities[position]
        }
    }

    override fun getItemCount(): Int {
        return cities.size
    }
}
