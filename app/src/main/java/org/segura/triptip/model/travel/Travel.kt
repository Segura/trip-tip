package org.segura.triptip.model.travel

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import org.segura.triptip.model.BaseTravel
import org.segura.triptip.model.Route
import org.segura.triptip.model.route.RouteWithWaypoint

data class Travel(
    @Embedded val baseTravel: BaseTravel,
    @Relation(parentColumn = "travel_id", entityColumn = "route_id", entity = Route::class)
    val route: RouteWithWaypoint,
    @Relation(
        parentColumn = "travel_id",
        entityColumn = "preparing_item_id",
        associateBy = Junction(TravelToPreparingItemRef::class)
    )
    val preparingList: List<PreparingItem>
)
