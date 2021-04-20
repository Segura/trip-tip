package org.segura.triptip.model.route

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "route-to-waypoint", primaryKeys = ["route_id", "waypoint_id"])
data class RouteWithWaypointRef(
    @ColumnInfo(name = "route_id", index = true) val routeId: Int,
    @ColumnInfo(name = "waypoint_id", index = true) val waypointId: Int
)