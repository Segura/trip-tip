package org.segura.triptip.fragments

import android.view.View
import org.segura.triptip.R
import org.segura.triptip.model.travel.Travel

class RouteFragment : TravelFragment() {
    override val layout: Int
        get() = R.layout.fragment_route

    override fun onTravelUpdate(root: View, item: Travel) {
        // TODO
    }
}
