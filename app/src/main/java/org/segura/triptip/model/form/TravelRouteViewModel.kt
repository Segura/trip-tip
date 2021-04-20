package org.segura.triptip.model.form

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.segura.triptip.model.route.Waypoint

class TravelRouteViewModel : ViewModel() {

    val validMediator = MediatorLiveData<Boolean>()
    val waypoints = MutableLiveData(emptyList<Waypoint>())

    private val waypointsValidator = FieldValidator(waypoints, this).apply {
        addRule { v, _ -> v!!.size > 1 }
    }
    private val validators = listOf(waypointsValidator)

    init {
        validMediator.value = false
        validMediator.addSource(waypoints) { validateForm() }
    }

    private fun validateForm() {
        val validatorResolver = LiveDataValidatorResolver(validators)
        validMediator.value = validatorResolver.isValid()
    }
}
