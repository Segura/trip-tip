package org.segura.triptip.model.weather

import org.json.JSONObject

class WeatherResponse(response: JSONObject) {

    val current: WeatherMoment

    init {
        current = WeatherMoment(response.get("current") as JSONObject)
    }
}
