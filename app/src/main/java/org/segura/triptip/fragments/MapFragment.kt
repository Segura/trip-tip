package org.segura.triptip.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.PointF
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mapzen.tangram.*
import com.mapzen.tangram.geometry.Geometry
import com.mapzen.tangram.geometry.Polyline
import org.segura.triptip.BuildConfig
import org.segura.triptip.R

data class Waypoint(
    val coordinates: LngLat,
    val marker: Marker
)

fun <T> MutableLiveData<T>.notifyObserver() {
    this.value = this.value
}

class MapFragment : Fragment(), MapView.MapReadyCallback {

    var waypointStyle = "{ style: 'ux-icons-overlay', interactive: true, sprite: ux-transit-stop, size: 24px, order: 999 }"
    private val PERMISSION_REQUEST = 1

    lateinit var map: MapController
    lateinit var view: MapView
    lateinit var routeLayer: MapData

    val waypoints = MutableLiveData<MutableList<Waypoint>>().apply {
        value = mutableListOf()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val root = inflater.inflate(R.layout.map_fragment, container, false)
        view = root.findViewById(R.id.map)
        view.onCreate(savedInstanceState)
        view.getMapAsync(this, CachingHttpHandler(context?.externalCacheDir))
        root.findViewById<FloatingActionButton>(R.id.show_my_location_button).setOnClickListener { showMyLocation() }
        root.findViewById<FloatingActionButton>(R.id.remove_markers_button).setOnClickListener { removeMarkers() }
        return root
    }

    override fun onMapReady(mapController: MapController?) {
        map = mapController!!

        map.loadSceneFileAsync(
            "cinnabar/cinnabar-style.yaml", listOf(
                SceneUpdate("global.sdk_api_key", BuildConfig.NEXTZEN_API_KEY),
            )
        )

        map.touchInput.setGestureDisabled(TouchInput.Gestures.LONG_PRESS)
        map.touchInput.setGestureDisabled(TouchInput.Gestures.ROTATE)
        map.touchInput.setGestureDisabled(TouchInput.Gestures.SHOVE)

        routeLayer = map.addDataLayer("mz_route_line_transit")

        map.setMarkerPickListener { result ->
            result?.let {
                waypoints.value!!.removeIf { it.marker == result.marker }
                waypoints.notifyObserver()
                map.removeMarker(result.marker)
                updateRoute()
            }
        }

        map.touchInput.setTapResponder(object : TouchInput.TapResponder {
            override fun onSingleTapUp(x: Float, y: Float): Boolean {
                return false
            }

            override fun onSingleTapConfirmed(x: Float, y: Float): Boolean {
                val markerPosition: LngLat? = map.screenPositionToLngLat(PointF(x, y))
                markerPosition?.let {
                    val newMarker = map.addMarker()
                    newMarker.setPoint(markerPosition)
                    newMarker.setStylingFromString(waypointStyle)
                    waypoints.value!!.add(Waypoint(markerPosition, newMarker))
                    waypoints.notifyObserver()
                    updateRoute()
                }
                return false
            }
        })

        map.touchInput.setDoubleTapResponder { x, y ->
            map.pickMarker(x, y)
            false
        }

        showMyLocation()
    }

    private fun showMyLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val locationManager = getSystemService(requireContext(), LocationManager::class.java)
            val location = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            location?.let {
                val cameraPosition = CameraPosition().apply {
                    longitude = location.longitude
                    latitude = location.latitude
                    zoom = 12f
                }
                map.flyToCameraPosition(cameraPosition, 1000, null)
            }
        } else {
            requestPermissions(Array(1) { Manifest.permission.ACCESS_FINE_LOCATION }, PERMISSION_REQUEST)
        }
    }

    private fun removeMarkers() {
        waypoints.value!!.clear()
        waypoints.notifyObserver()
        map.removeAllMarkers()
        updateRoute()
    }

    private fun updateRoute() {
        routeLayer.setFeatures(listOf(Polyline(waypoints.value!!.map { it.coordinates }, null) as Geometry))
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST && Manifest.permission.ACCESS_FINE_LOCATION in permissions && PackageManager.PERMISSION_GRANTED in grantResults) {
            showMyLocation()
        }
    }

    override fun onResume() {
        super.onResume()
        view.onResume()
    }

    override fun onPause() {
        super.onPause()
        view.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        view.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        view.onLowMemory()
    }
}
