package org.segura.triptip.model.route

import android.location.Location
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "waypoints")
data class Waypoint(
    @ColumnInfo(name = "lat") val latitude: Double,
    @ColumnInfo(name = "long") val longitude: Double,
    @ColumnInfo(name = "visited") val visited: Boolean
) {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "waypoint_id") var waypointId: Int = 0

    @Ignore
    val location: Location = Location("").apply {
        latitude = this@Waypoint.latitude
        longitude = this@Waypoint.longitude
    }
}
