package com.example.weatherapp.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.weatherapp.R
import com.example.weatherapp.model.BaseWeather
import com.example.weatherapp.utils.Constants
import com.squareup.picasso.Picasso
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class FirstFragment : Fragment() {

    private lateinit var name: TextView
    private lateinit var latlon: TextView
    private lateinit var time: TextView
    private lateinit var pressure: TextView
    private lateinit var tempereture: TextView
    private lateinit var image: ImageView
    private lateinit var description: TextView
    private lateinit var tempSign: String

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_first, container, false)
        name = view.findViewById<View>(R.id.name) as TextView
        latlon = view.findViewById<View>(R.id.latlon) as TextView
        time = view.findViewById<View>(R.id.time) as TextView
        pressure = view.findViewById<View>(R.id.pressure) as TextView
        tempereture = view.findViewById<View>(R.id.temperature) as TextView
        description = view.findViewById<View>(R.id.description) as TextView
        image = view.findViewById<View>(R.id.imageview) as ImageView

        val bundle = arguments
        if (arguments != null) {
            tempSign = if (Constants.UNITS == "metric") {
                "°C"
            } else {
                "°F"
            }

            val weather = bundle!!.getSerializable("weather") as BaseWeather
            name.text = weather.name
            latlon.text = "lat: ${weather.coord.lat} lon: ${weather.coord.lon}"
            time.text = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
            pressure.text = "${weather.main.pressure} hPa"
            tempereture.text = "${weather.main.temp} $tempSign"
            description.text = weather.weather[0].description
            Picasso.get().load("https://openweathermap.org/img/wn/${weather.weather[0].icon}@2x.png").into(image)
        }
        return view
    }
}
