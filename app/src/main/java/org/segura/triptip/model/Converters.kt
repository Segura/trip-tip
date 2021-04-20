package org.segura.triptip.model

import androidx.room.TypeConverter
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.segura.triptip.model.params.Params
import java.util.*

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun fromJsonToParams(value: String): Params {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromParamsToJson(value: Params): String {
        return Json.encodeToString(value)
    }
}
