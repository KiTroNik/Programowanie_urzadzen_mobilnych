package com.example.weatherapp.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import android.util.Log
import androidx.lifecycle.*
import com.example.weatherapp.MyApp
import com.example.weatherapp.model.BaseWeather
import com.example.weatherapp.model.Forecast
import com.example.weatherapp.repository.WeatherRepository
import com.example.weatherapp.utils.Constants.API_KEY
import com.example.weatherapp.utils.Constants.CITY
import com.example.weatherapp.utils.Constants.EXCLUDE
import com.example.weatherapp.utils.Constants.UNITS
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.*
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel
@Inject
constructor(app: Application, private val repository: WeatherRepository) : AndroidViewModel(app) {

    private val _resp = MutableLiveData<BaseWeather>()
    private val _forecast = MutableLiveData<Forecast>()
    private val _internetErr = MutableLiveData<Boolean>()
    private val _noCity = MutableLiveData<Boolean>()
    private lateinit var fileOutputStream: FileOutputStream

    val weatherResp: LiveData<BaseWeather>
        get() = _resp
    val forecastResp: LiveData<Forecast>
        get() = _forecast
    val internetConnectionErr: LiveData<Boolean>
        get() = _internetErr
    val notExistingCity: LiveData<Boolean>
        get() = _noCity

    init {
        getWeather()
    }

    fun getWeather() = viewModelScope.launch {
        if (hasInternetConnection()) {
            _internetErr.postValue(false)
            repository.getBaseWeather(API_KEY, CITY, UNITS).let { response ->
                if (response.isSuccessful) {
                    _resp.postValue(response.body())
                    writeWeatherToFile(response.body() as BaseWeather)
                    _noCity.postValue(false)
                    repository.getForecast(API_KEY, response.body()!!.coord.lat, response.body()!!.coord.lon, EXCLUDE, UNITS).let {fore ->
                        if (fore.isSuccessful) {
                            _forecast.postValue(fore.body())
                            writeForecastToFile(fore.body() as Forecast)
                        } else {
                            Log.d("Tag", "Forecast Error Response: ${fore.message()}")
                        }
                    }
                } else {
                    if (response.message() == "Not Found") {
                        _noCity.postValue(true)
                    }
                    Log.d("Tag", "getWeather Error Response: ${response.message()}")
                }
            }
        } else {
            _internetErr.postValue(true)
            try {
                _resp.postValue(readWeatherFromFile())
                _forecast.postValue(readForecastFromFile())
            } catch (e: Exception) {
                Log.d("ERROR", "${e.toString()}")
            }
        }
    }

    private fun readWeatherFromFile(): BaseWeather {
        var fileInputStream: FileInputStream? = null
        fileInputStream = getApplication<MyApp>().openFileInput("weather.txt")
        val inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
        val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)
        val stringBuilder: StringBuilder = StringBuilder()
        var text: String? = null
        while (run {
                text = bufferedReader.readLine()
                text
            } != null) {
            stringBuilder.append(text)
        }
        val gson = Gson()
        return gson.fromJson(stringBuilder.toString(), BaseWeather::class.java)
    }

    private fun readForecastFromFile(): Forecast {
        var fileInputStream: FileInputStream? = null
        fileInputStream = getApplication<MyApp>().openFileInput("forecast.txt")
        val inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
        val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)
        val stringBuilder: StringBuilder = StringBuilder()
        var text: String? = null
        while (run {
                text = bufferedReader.readLine()
                text
            } != null) {
            stringBuilder.append(text)
        }
        val gson = Gson()
        return gson.fromJson(stringBuilder.toString(), Forecast::class.java)
    }

    private fun writeWeatherToFile(weather: BaseWeather) {
        val gson = Gson()
        val jsonWeather = gson.toJson(weather)
        fileOutputStream = getApplication<MyApp>().openFileOutput("weather.txt", Context.MODE_PRIVATE)
        fileOutputStream.write(jsonWeather.toByteArray())
    }

    private fun writeForecastToFile(forecast: Forecast) {
        val gson = Gson()
        val jsonWeather = gson.toJson(forecast)
        fileOutputStream = getApplication<MyApp>().openFileOutput("forecast.txt", Context.MODE_PRIVATE)
        fileOutputStream.write(jsonWeather.toByteArray())
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<MyApp>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when(type) {
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}
