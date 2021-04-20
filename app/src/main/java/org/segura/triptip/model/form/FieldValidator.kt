package org.segura.triptip.model.form

import androidx.lifecycle.LiveData

typealias Predicate<T, M> = (value: T?, form: M) -> Boolean

interface Validator {
    fun isValid(): Boolean
}

class FieldValidator<T, M>(private val liveData: LiveData<T>, private val form: M): Validator {
    private val validationRules = mutableListOf<Predicate<T, M>>()

    override fun isValid(): Boolean {
        return validationRules.all { it(liveData.value, form) }
    }

    fun addRule(predicate: Predicate<T, M>) {
        validationRules.add(predicate)
    }
}
