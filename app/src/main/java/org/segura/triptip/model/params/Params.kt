package org.segura.triptip.model.params

import kotlinx.serialization.Serializable

@Serializable
data class Params(
    val people: Int,
    val days: Int,
    val waterPerPeoplePerDay: Int,
    val comment: String
) {
    val water: Int
        get() = people * days * waterPerPeoplePerDay
}
