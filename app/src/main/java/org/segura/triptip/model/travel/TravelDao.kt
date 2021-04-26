package org.segura.triptip.model.travel

import androidx.room.*
import org.segura.triptip.model.BaseTravel
import org.segura.triptip.model.TravelDatabase
import org.segura.triptip.model.route.RouteWithWaypointRef

@Dao
abstract class TravelDao(val db: TravelDatabase) {

    @Transaction
    @Query("SELECT * FROM travels WHERE archived = 0 ORDER BY start_at")
    abstract suspend fun selectNonArchived(): List<Travel>

    @Transaction
    @Query("SELECT * FROM travels WHERE archived = 1 ORDER BY start_at")
    abstract suspend fun selectArchived(): List<Travel>

    @Query("SELECT * FROM travels WHERE end_at < :date")
    abstract fun selectBefore(date: Int): List<Travel>

    @Query("SELECT * FROM travels WHERE start_at > :date")
    abstract fun selectAfter(date: Int): List<Travel>

    @Query("SELECT * FROM travels WHERE start_at > :from AND end_at < :to")
    abstract fun selectBetween(from: Int, to: Int): List<Travel>

    @Transaction
    @Query("SELECT * FROM travels WHERE travel_id = :travelId")
    abstract suspend fun selectOne(travelId: Int): Travel?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insert(travel: BaseTravel): Long

    @Update
    abstract fun update(travel: BaseTravel)

    suspend fun insert(travel: Travel) {
        val travelId = insert(travel.travel)
        travel.route.route.travelId = travelId.toInt()
        val routeId = db.routeDao().insert(travel.route.route)
        for (waypoint in travel.route.waypoints) {
            val waypointId = db.routeDao().insert(waypoint)
            db.routeDao().insert(RouteWithWaypointRef(routeId.toInt(), waypointId.toInt()))
        }
    }

    @Delete
    abstract suspend fun delete(travel: BaseTravel)
}
