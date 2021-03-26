package org.segura.triptip.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class Travel : ViewModel() {
    val title: LiveData<String> = MutableLiveData<String>().apply {
        value = "someTravel"
    }
}
