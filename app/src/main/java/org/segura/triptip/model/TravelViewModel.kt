package org.segura.triptip.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.segura.triptip.model.travel.Travel

class TravelViewModel : ViewModel() {

    private val mutableSelectedItem = MutableLiveData<Travel>()
    val selectedItem: LiveData<Travel> get() = mutableSelectedItem

    fun selectItem(travel: Travel) {
        mutableSelectedItem.value = travel
    }
}
