package org.segura.triptip.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.segura.triptip.DATABASE_NAME
import org.segura.triptip.model.route.RouteDao
import org.segura.triptip.model.route.RouteWithWaypointRef
import org.segura.triptip.model.route.Waypoint
import org.segura.triptip.model.travel.PreparingItem
import org.segura.triptip.model.travel.TravelDao
import org.segura.triptip.model.travel.TravelToPreparingItemRef

@Database(entities = [BaseTravel::class, Waypoint::class, Route::class, RouteWithWaypointRef::class, PreparingItem::class, TravelToPreparingItemRef::class], version = 11)
@TypeConverters(Converters::class)
abstract class TravelDatabase : RoomDatabase() {
    abstract fun travelDao(): TravelDao
    abstract fun routeDao(): RouteDao

    companion object {
        @Volatile private var instance: TravelDatabase? = null

        fun getInstance(context: Context): TravelDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): TravelDatabase {
            return Room.databaseBuilder(context, TravelDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
