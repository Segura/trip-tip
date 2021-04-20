package org.segura.triptip.model.weather

import org.segura.triptip.R

class WeatherIcon(string: String) {

    val code: Int
    val weatherIconDaytime: WeatherIconDaytime

    init {
        code = when(string.substring(0, 2).toInt()) {
            1 -> R.drawable.ic_clear_sky
            2 -> R.drawable.ic_few_clouds
            3 -> R.drawable.ic_scattered_clouds
            4 -> R.drawable.ic_broken_clouds
            5 -> R.drawable.ic_shower_rain
            10 -> R.drawable.ic_rain
            11 -> R.drawable.ic_thunderstorm
            13 -> R.drawable.ic_snow
            50 -> R.drawable.ic_mist
            else -> 0
        }
        weatherIconDaytime = if (string[2] == 'd') WeatherIconDaytime.DAY else WeatherIconDaytime.NIGHT
    }
}
