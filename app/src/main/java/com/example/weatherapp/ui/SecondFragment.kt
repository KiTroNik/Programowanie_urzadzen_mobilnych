package com.example.weatherapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.weatherapp.R
import com.example.weatherapp.model.BaseWeather
import com.example.weatherapp.utils.Constants


class SecondFragment : Fragment() {
    private lateinit var wind: TextView
    private lateinit var humidity: TextView
    private lateinit var visibility: TextView
    private lateinit var windSign: String

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_second, container, false)
        wind = view.findViewById<View>(R.id.wind) as TextView
        humidity = view.findViewById<View>(R.id.humidity) as TextView
        visibility = view.findViewById<View>(R.id.visibility) as TextView

        val bundle = arguments
        if (arguments != null) {
            windSign = if (Constants.UNITS == "metric") {
                "m/s"
            } else {
                "miles/hours"
            }

            val weather = bundle!!.getSerializable("weather") as BaseWeather
            wind.text = "Wind: ${weather.wind.speed} $windSign ${weather.wind.deg} deg"
            humidity.text = "Humidity: ${weather.main.humidity} %"
            visibility.text = "Visibility: ${weather.visibility} m"
        }
        return view
    }
}
