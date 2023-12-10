package com.example.weatherapp.ui

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.weatherapp.R
import com.example.weatherapp.model.Forecast
import com.example.weatherapp.utils.Constants
import java.time.LocalDate


class ThirdFragment : Fragment() {
    private lateinit var firstDay: TextView
    private lateinit var secondDay: TextView
    private lateinit var thirdDay: TextView
    private lateinit var firstTemp: TextView
    private lateinit var secondTemp: TextView
    private lateinit var thirdTemp: TextView
    private lateinit var tempSign: String

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_third, container, false)
        firstDay = view.findViewById<View>(R.id.firstDay) as TextView
        secondDay = view.findViewById<View>(R.id.secondDay) as TextView
        thirdDay = view.findViewById<View>(R.id.thirdDay) as TextView
        firstTemp = view.findViewById<View>(R.id.firstTemp) as TextView
        secondTemp = view.findViewById<View>(R.id.secondTemp) as TextView
        thirdTemp = view.findViewById<View>(R.id.thirdTemp) as TextView

        val bundle = arguments
        Log.d("elo", "$arguments")
        if (arguments != null) {
            tempSign = if (Constants.UNITS == "metric") {
                "°C"
            } else {
                "°F"
            }

            val weather = bundle!!.getSerializable("forecast") as Forecast
            val test = bundle!!.getString("Siema")
            firstTemp.text = test
            firstTemp.text = "${weather.daily[0].temp.day} $tempSign"
            secondTemp.text = "${weather.daily[1].temp.day} $tempSign"
            thirdTemp.text = "${weather.daily[2].temp.day} $tempSign"
            firstDay.text = LocalDate.now().plusDays(1).dayOfWeek.name
            secondDay.text = LocalDate.now().plusDays(2).dayOfWeek.name
            thirdDay.text = LocalDate.now().plusDays(3).dayOfWeek.name
        }

        return view
    }
}