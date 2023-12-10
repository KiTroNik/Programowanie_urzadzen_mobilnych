package com.example.weatherapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.R
import com.example.weatherapp.db.FavDb
import kotlinx.android.synthetic.main.activity_favorites.*

class FavoritesActivity : AppCompatActivity() {

    private lateinit var db: FavDb

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        db = FavDb(this)
        val listCities: ArrayList<String> = db.favoriteList

        val adapter = FavAdapter(listCities, this)
        listFavorites.adapter = adapter
        listFavorites.layoutManager = LinearLayoutManager(this)
    }
}
