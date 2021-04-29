package org.segura.triptip.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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


const val IN_PROGRESS: Byte = 0
const val ERROR: Byte = 1
const val SUCCESS: Byte = 2

class WeatherFragment : TravelFragment(), RequestQueue.RequestEventListener {

    private val API_QUERY = "https://api.openweathermap.org/data/2.5/onecall?lat=%s&lon=%s&exclude=minutely,hourly,daily&lang=%s&units=metric&appid=%s"

    override val layout: Int
        get() = R.layout.fragment_weather

    private lateinit var activity: MainActivity
    private lateinit var container: GridLayout
    private lateinit var queue: RequestQueue
    private lateinit var loadingState: ByteArray

    private val allFinished: Boolean
        get() {
            return loadingState.all { it == SUCCESS }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        queue = Volley.newRequestQueue(context)
        queue.addRequestEventListener(this)
        activity = super.getActivity() as MainActivity
        val savedLoadingState = savedInstanceState?.getByteArray("loadingState")
        savedLoadingState?.let {
            loadingState = it
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val root = super.onCreateView(inflater, container, savedInstanceState)
        this.container = root.findViewById(R.id.container)
        return root
    }

    override fun onPause() {
        super.onPause()
        queue.cancelAll { true }
        activity.stopProgress()
    }

    override fun onResume() {
        super.onResume()
        if (allFinished) {
            container.visibility = View.VISIBLE
        } else {
            startLoading()
        }
    }

    override fun onTravelUpdate(root: View, item: Travel) {
        queue.cancelAll { true }
        loadingState = ByteArray(item.route.waypoints.size) { IN_PROGRESS }
        container.removeAllViews()
        for (i in loadingState) {
            container.addView(WaypointWeather(requireContext()))
        }
        startLoading()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putByteArray("loadingState", loadingState)
    }

    override fun onRequestEvent(request: Request<*>, event: Int) {
        if (request.isCanceled) {
            loadingState[request.tag as Int] = IN_PROGRESS
        }
    }

    private fun startLoading() {
        container.visibility = View.INVISIBLE
        activity.startProgress()
        val travel = travel.selectedItem.value!!
        val waypoints = travel.route.waypoints
        loadingState.forEachIndexed { index, state ->
            if (state != SUCCESS) {
                queue.add(this.createRequest(index, waypoints[index], travel.baseTravel.endAt.time))
            }
        }
    }

    private fun createRequest(index: Int, waypoint: Waypoint, expiredFrom: Long): JsonObjectRequest {
        val langCode = Locale.getDefault().language
        val url = API_QUERY.format(waypoint.latitude, waypoint.longitude, langCode, BuildConfig.OPEN_WEATHER_MAP_API_KEY)
        return CachedJSONObjectResponse(
            url,
            {
                loadingState[index] = SUCCESS
                fillView(container.getChildAt(index) as WaypointWeather, index, it)
                if (allFinished) {
                    activity.stopProgress()
                    container.visibility = View.VISIBLE
                }
            },
            {
                loadingState[index] = ERROR
            }
        ).apply {
            this.cacheExpiredFrom = expiredFrom
            this.tag = index
        }
    }

    private fun fillView(view: WaypointWeather, index: Int, response: JSONObject) {
        val weather = WeatherResponse(response)
        val current = weather.current

        view.apply {
            this.index = index + 1
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
    }
}
