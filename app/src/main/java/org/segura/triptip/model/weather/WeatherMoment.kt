package org.segura.triptip.model.weather

import org.json.JSONArray
import org.json.JSONObject

class WeatherMoment(data: JSONObject) {

    val temperature: Int
    val windDeg: Int
    val windSpeed: Float
    val windGust: Float?
    val conditions: List<WeatherCondition>

    val mainCondition
        get() = conditions.first()

    init {
        temperature = data.getInt("temp")
        windDeg = data.getInt("wind_deg")
        windSpeed = data.getDouble("wind_speed").toFloat()
        windGust = if (data.has("wind_gust")) data.getDouble("wind_gust").toFloat() else null

        val rawWeatherConditions = data.get("weather") as JSONArray
        conditions = List(rawWeatherConditions.length()) {
            WeatherCondition(rawWeatherConditions.getJSONObject(it))
        }
    }
}
