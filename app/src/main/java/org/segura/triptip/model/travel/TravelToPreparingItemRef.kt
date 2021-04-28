package org.segura.triptip.model.travel

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "travel-to-preparing-item", primaryKeys = ["travel_id", "preparing_item_id"])
data class TravelToPreparingItemRef(
    @ColumnInfo(name = "travel_id", index = true) val travelId: Int,
    @ColumnInfo(name = "preparing_item_id", index = true) val preparingItemId: Int
)
