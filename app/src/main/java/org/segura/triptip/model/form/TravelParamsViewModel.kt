package org.segura.triptip.model.form

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class TravelParamsViewModel : ViewModel() {

    val validMediator = MediatorLiveData<Boolean>()

    val people = MutableLiveData<Int>()
    val days = MutableLiveData<Int>()
    val waterPerPeoplePerDay = MutableLiveData<Int>()
    val comment = MutableLiveData<String>("")

    private val peopleValidator = FieldValidator(people, this).apply {
        addRule { v, _ -> v != null }
        addRule { v, _ -> v!! >= 0 }
        addRule { v, _ -> v!! <= 100 }
    }
    private val daysValidator = FieldValidator(days, this).apply {
        addRule { v, _ -> v != null }
        addRule { v, _ -> v!! >= 0 }
        addRule { v, _ -> v!! <= 100 }
    }
    private val waterPerPeoplePerDayValidator = FieldValidator(waterPerPeoplePerDay, this).apply {
        addRule { v, _ -> v != null }
        addRule { v, _ -> v!! >= 0 }
        addRule { v, _ -> v!! <= 100 }
    }
    private val commentValidator = FieldValidator(comment, this).apply {
        addRule { v, _ -> v != null}
    }

    private val validators = listOf(peopleValidator, daysValidator, waterPerPeoplePerDayValidator, commentValidator)

    init {
        validMediator.value = false
        validMediator.addSource(people) { validateForm() }
        validMediator.addSource(days) { validateForm() }
        validMediator.addSource(waterPerPeoplePerDay) { validateForm() }
        validMediator.addSource(comment) { validateForm() }
    }

    private fun validateForm() {
        val validatorResolver = LiveDataValidatorResolver(validators)
        validMediator.value = validatorResolver.isValid()
    }
}
