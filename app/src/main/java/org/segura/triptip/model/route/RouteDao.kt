package org.segura.triptip.model.route

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import org.segura.triptip.model.Route

@Dao
interface RouteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(route: Route): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(waypoint: Waypoint): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(ref: RouteWithWaypointRef)

    @Delete
    suspend fun delete(route: Route)
}
