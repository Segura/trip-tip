package org.segura.triptip.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "route",
    foreignKeys = [
        ForeignKey(entity = BaseTravel::class, parentColumns = ["travel_id"], childColumns = ["route_id"])
    ],
)
data class Route(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "route_id") var routeId: Int = 0,
    @ColumnInfo(name = "travel_id", index = true) var travelId: Int = 0
)
