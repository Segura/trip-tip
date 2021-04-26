package org.segura.triptip.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import org.segura.triptip.model.TravelViewModel
import org.segura.triptip.model.travel.Travel

abstract class TravelFragment : Fragment() {

    abstract val layout: Int

    val travel: TravelViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val root = inflater.inflate(layout, container, false)
        travel.selectedItem.observe(viewLifecycleOwner, { item -> onTravelUpdate(root, item) })
        return root
    }

    abstract fun onTravelUpdate(root: View, item: Travel)
}
