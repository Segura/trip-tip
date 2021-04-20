package org.segura.triptip.fragments

import android.view.View
import android.widget.GridLayout
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import org.segura.triptip.BuildConfig
import org.segura.triptip.MainActivity
import org.segura.triptip.R
import org.segura.triptip.model.route.Waypoint
import org.segura.triptip.model.travel.Travel
import org.segura.triptip.model.weather.WeatherResponse
import org.segura.triptip.views.WaypointWeather
import java.util.*


class WeatherFragment : TravelFragment() {

    private val API_QUERY = "https://api.openweathermap.org/data/2.5/onecall?lat=%s&lon=%s&exclude=minutely&lang=%s&units=metric&appid=%s"

    override val layout: Int
        get() = R.layout.fragment_weather

    private var requestsCount = 0
    private lateinit var responses: Array<JSONObject?>

    override fun onTravelUpdate(root: View, item: Travel) {
        val activity = activity as MainActivity
        responses = Array(item.route.waypoints.size) { null }

        val container = root.findViewById<GridLayout>(R.id.container)

        val queue = Volley.newRequestQueue(context)
        queue.addRequestEventListener { request, event ->
            if (event == RequestQueue.RequestEvent.REQUEST_FINISHED) {
                requestsCount -= 1
                if (requestsCount == 0) {
                    var i = 1
                    for (response in responses) {
                        val weather = WeatherResponse(response!!)
                        val current = weather.current

                        val newView = WaypointWeather(requireContext()).apply {
                            this.index = i++
                            this.temperature = current.temperature
                            this.windDegree = current.windDeg
                            this.windSpeed = current.windSpeed
                            this.windGust = current.windGust
                            this.description = current.mainCondition.description
                            this.iconId = current.mainCondition.icon.code
                            this.daytime = current.mainCondition.icon.weatherIconDaytime
                            this.layoutParams = GridLayout.LayoutParams(
                                GridLayout.spec(GridLayout.UNDEFINED, 1f),
                                GridLayout.spec(GridLayout.UNDEFINED, 1f)
                            ).apply {
                                this.height = 0
                                this.width = 0
                            }
                        }
                        container.addView(newView)
                    }
                    activity.stopProgress()
                }
            }
        }

        requestsCount = item.route.waypoints.size
        item.route.waypoints.forEachIndexed { index, waypoint ->
            queue.add(this.createRequest(index, waypoint))
        }
        activity.startProgress()
    }

    fun createRequest(index: Int, waypoint: Waypoint): JsonObjectRequest {
        val langCode = Locale.getDefault().language
        val url = API_QUERY.format(waypoint.latitude, waypoint.longitude, langCode, BuildConfig.OPEN_WEATHER_MAP_API_KEY)
        return JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                responses[index] = response
            },
            { error ->
                // TODO: Handle error
            }
        )
    }
}
