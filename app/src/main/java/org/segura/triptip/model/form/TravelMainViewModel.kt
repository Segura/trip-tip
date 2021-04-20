package org.segura.triptip.model.form

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.*

class TravelMainViewModel : ViewModel() {

    val validMediator = MediatorLiveData<Boolean>()

    val title = MutableLiveData<String>()
    val start = MutableLiveData<Date>()
    val end = MutableLiveData<Date>()

    private val titleValidator = FieldValidator(title, this).apply {
        addRule { v, _ -> !v.isNullOrBlank() }
    }
    private val startValidator = FieldValidator(start, this).apply {
        addRule { v, _ -> v != null }
        addRule { v, m -> m.end.value != null && v!!.before(m.end.value) }
    }
    private val endValidator = FieldValidator(end, this).apply {
        addRule { v, _ -> v != null }
        addRule { v, m -> m.start.value != null && v!!.after(m.start.value) }
    }
    private val validators = listOf(titleValidator, startValidator, endValidator)

    init {
        validMediator.value = false
        validMediator.addSource(title) { validateForm() }
        validMediator.addSource(start) { validateForm() }
        validMediator.addSource(end) { validateForm() }
    }

    private fun validateForm() {
        val validatorResolver = LiveDataValidatorResolver(validators)
        validMediator.value = validatorResolver.isValid()
    }
}
