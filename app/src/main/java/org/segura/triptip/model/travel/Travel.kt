package org.segura.triptip.model.travel

import androidx.room.Embedded
import androidx.room.Relation
import org.segura.triptip.model.BaseTravel
import org.segura.triptip.model.Route
import org.segura.triptip.model.route.RouteWithWaypoint

data class Travel(
    @Embedded val travel: BaseTravel,
    @Relation(parentColumn = "travel_id", entityColumn = "route_id", entity = Route::class)
    val route: RouteWithWaypoint,
)
