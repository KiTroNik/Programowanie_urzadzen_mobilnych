package com.example.weatherapp.ui

import android.content.Intent
import android.net.ConnectivityManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.weatherapp.R
import com.example.weatherapp.databinding.MainActivityBinding
import com.example.weatherapp.db.FavDb
import com.example.weatherapp.repository.WeatherRepository
import com.example.weatherapp.utils.Constants
import com.example.weatherapp.viewmodel.WeatherViewModel

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.main_activity.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var db: FavDb
    internal var lstFavorites: List<String> = ArrayList<String>()
    private lateinit var binding: MainActivityBinding
    private val viewModel:WeatherViewModel by viewModels()
    private lateinit var repository: WeatherRepository
    var connectivity: ConnectivityManager? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        db = FavDb(this)
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bundle = Bundle()

        updateBtns()

        btnFav.setOnClickListener {
            val listCities: List<String> = db.favoriteList
            if (listCities.contains(Constants.CITY)) {
                db.deleteLocalization(Constants.CITY)
                btnFav.setBackgroundResource(R.drawable.ic_favorite_shadow_24)
                btnFav.foreground = ContextCompat.getDrawable(this,
                    R.drawable.ic_favorite_shadow_24
                )

            } else {
                db.addLocalization(Constants.CITY)
                btnFav.setBackgroundResource(R.drawable.ic_favorite_red_24)
                btnFav.foreground = ContextCompat.getDrawable(this, R.drawable.ic_favorite_red_24)
            }
        }

        viewModel.weatherResp.observe(this) { weather ->
            binding.apply {
                updateBtns()
                val firstManager = supportFragmentManager
                val firstTransaction = firstManager.beginTransaction()
                val secondManager = supportFragmentManager
                val secondTransaction = secondManager.beginTransaction()
                val firstFragment = FirstFragment()
                val secondFragment = SecondFragment()
                bundle.putSerializable("weather", weather)
                firstFragment.arguments = bundle
                secondFragment.arguments = bundle
                firstTransaction.replace(R.id.fragment1, firstFragment).commit()
                secondTransaction.replace(R.id.fragment2, secondFragment).commit()
            }
        }

        viewModel.forecastResp.observe(this) {forecast ->
           binding.apply {
               val thirdManager = supportFragmentManager
               val transaction = thirdManager.beginTransaction()
               val thirdFragment = ThirdFragment()
               bundle.putSerializable("forecast", forecast)
               thirdFragment.arguments = bundle
               Log.d("Bundle", "$bundle")
               transaction.replace(R.id.fragment3, thirdFragment).commit()
           }
        }

        viewModel.notExistingCity.observe(this) {error ->
            binding.apply {
                if (error) {
                   showToast("City doesn't exists")
                }
            }
        }

        viewModel.internetConnectionErr.observe(this) {error ->
            binding.apply {
                if (error) {
                    showToast("No internet connection. Data is outdated or absent if no saved data.")
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun updateBtns() {
        val listCities: List<String> = db.favoriteList
        Log.d("Cities", "$listCities")
        if (listCities.contains(Constants.CITY)) {
            btnFav.setBackgroundResource(R.drawable.ic_favorite_red_24)
            btnFav.foreground = ContextCompat.getDrawable(this, R.drawable.ic_favorite_red_24)
        } else {
            btnFav.setBackgroundResource(R.drawable.ic_favorite_shadow_24)
            btnFav.foreground = ContextCompat.getDrawable(this, R.drawable.ic_favorite_shadow_24)
        }

    }

    private fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.refresh) {
            viewModel.getWeather()
            return true;
        }else if (item.itemId == R.id.change) {
            val builder = AlertDialog.Builder(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.change_city_layout, null)
            val text = dialogLayout.findViewById<EditText>(R.id.etChangeCity)

            with(builder) {
                setTitle("Change city")

                setPositiveButton("OK") {dialog, which ->
                    val city = Constants.CITY
                    Constants.CITY = text.text.toString()
                    viewModel.getWeather()
                }
                setNegativeButton("Cancel"){dialog, which ->
                    Log.d("Main", "Cancel button clicked")
                }
                setView(dialogLayout)
                show()
            }
            return true;
        }else if(item.itemId == R.id.metric) {
            Constants.UNITS = "metric"
            viewModel.getWeather()
            return true
        } else if(item.itemId == R.id.imperial) {
            Constants.UNITS = "imperial"
            viewModel.getWeather()
            return true
        }else if (item.itemId == R.id.favorites) {
            val intent = Intent(this, FavoritesActivity::class.java)
            startActivity(intent)
            return true
        } else {
            return super.onOptionsItemSelected(item)
        }
    }
}
