package org.segura.triptip.model.params

import androidx.room.Ignore
import kotlinx.serialization.Serializable

@Serializable
data class Params(
    val people: Int,
    val days: Int,
    val waterPerPeoplePerDay: Int,
    val comment: String
) {
    @Ignore
    fun getWater(): Int {
        return people * days * waterPerPeoplePerDay
    }
}
