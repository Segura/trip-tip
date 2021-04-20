package org.segura.triptip.fragments

import android.view.View
import android.widget.TextView
import org.segura.triptip.R
import org.segura.triptip.model.travel.Travel

class InfoFragment : TravelFragment() {
    override val layout: Int
        get() = R.layout.fragment_info

    override fun onTravelUpdate(root: View, item: Travel) {
        val textView: TextView = root.findViewById(R.id.title)
        textView.text = travel.selectedItem.value?.travel?.title ?: ""
    }
}