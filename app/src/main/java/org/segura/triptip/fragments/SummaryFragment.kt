package org.segura.triptip.fragments

import android.view.View
import android.widget.TextView
import org.segura.triptip.R
import org.segura.triptip.model.travel.Travel

class SummaryFragment : TravelFragment() {
    override val layout: Int
        get() = R.layout.fragment_summary

    override fun onTravelUpdate(root: View, item: Travel) {
        val textView: TextView = root.findViewById(R.id.title)
        travel.selectedItem.value?.let {
            textView.text = it.baseTravel.title
        }
    }
}
