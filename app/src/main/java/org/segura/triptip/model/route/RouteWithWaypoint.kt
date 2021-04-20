package org.segura.triptip.model.route

import android.location.Location
import androidx.room.Embedded
import androidx.room.Ignore
import androidx.room.Junction
import androidx.room.Relation
import org.segura.triptip.model.Route

data class RouteWithWaypoint(
    @Embedded val route: Route,
    @Relation(
        parentColumn = "route_id",
        entityColumn = "waypoint_id",
        associateBy = Junction(RouteWithWaypointRef::class)
    )
    val waypoints: List<Waypoint>
) {
    @Ignore
    fun getTotalDistance(): Float {
        return waypoints.zipWithNext().fold(0.0f, { res, coordinates ->
            val distance = FloatArray(3)
            Location.distanceBetween(coordinates.first.latitude, coordinates.first.longitude, coordinates.second.latitude, coordinates.second.longitude, distance)
            res + distance.first()
        })
    }
}
