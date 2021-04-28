package org.segura.triptip.model.travel

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "preparing_items")
data class PreparingItem(
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "count") val count: Int,
    @ColumnInfo(name = "units") val units: String
) {
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "preparing_item_id") var preparingItemId: Int = 0
    @ColumnInfo(name = "done") var done: Boolean = false
}
