package org.segura.triptip.model.form

class LiveDataValidatorResolver(private val validators: List<Validator>): Validator {
    override fun isValid(): Boolean {
        return validators.all { it.isValid() }
    }
}
