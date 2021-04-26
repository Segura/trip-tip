package org.segura.triptip.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import org.segura.triptip.model.params.Params
import java.util.*

@Entity(tableName = "travels")
data class BaseTravel(
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "start_at") val startAt: Date,
    @ColumnInfo(name = "end_at") val endAt: Date,
    @ColumnInfo(name = "params") val params: Params,
) {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "travel_id") var travelId: Int = 0
    @ColumnInfo(name = "archived") var archived: Boolean = false

    @Ignore
    private val now = Date()

    val isBefore: Boolean
        get() = endAt.before(now)
    val isAfter: Boolean
        get() = startAt.after(now)
}
