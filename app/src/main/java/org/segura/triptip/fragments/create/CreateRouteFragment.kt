package org.segura.triptip.fragments.create

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import org.segura.triptip.R
import org.segura.triptip.databinding.TravelFormRouteBinding
import org.segura.triptip.fragments.MapFragment
import org.segura.triptip.model.form.TravelRouteViewModel
import org.segura.triptip.model.route.Waypoint

class CreateRouteFragment : Fragment() {

    private val routeModel: TravelRouteViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)

        val binding = DataBindingUtil.inflate<TravelFormRouteBinding>(layoutInflater, R.layout.travel_form_route, container, false)
        val root = binding.root

        binding.travelRoute = routeModel
        binding.lifecycleOwner = viewLifecycleOwner

        root.findViewById<Button>(R.id.next_button).setOnClickListener {
            val findNavController = findNavController()
            findNavController.navigate(R.id.action_next)
        }
        val fragment = childFragmentManager.findFragmentById(R.id.map_fragment) as MapFragment
        fragment.waypoints.observe(viewLifecycleOwner, {
            routeModel.waypoints.value = it.map { Waypoint(it.coordinates.latitude, it.coordinates.longitude) }
        })
        return root
    }
}
