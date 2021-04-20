package org.segura.triptip.model.weather

import org.json.JSONObject

class WeatherCondition(data: JSONObject) {

    val description: String
    val icon: WeatherIcon

    init {
        description = data.getString("description")
        icon = WeatherIcon(data.getString("icon"))
    }
}
