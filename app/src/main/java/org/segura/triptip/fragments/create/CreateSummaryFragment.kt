package org.segura.triptip.fragments.create

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.segura.triptip.MainActivity
import org.segura.triptip.R
import org.segura.triptip.databinding.TravelFormSummaryBindingImpl
import org.segura.triptip.model.BaseTravel
import org.segura.triptip.model.Route
import org.segura.triptip.model.TravelDatabase
import org.segura.triptip.model.form.TravelMainViewModel
import org.segura.triptip.model.form.TravelParamsViewModel
import org.segura.triptip.model.form.TravelRouteViewModel
import org.segura.triptip.model.params.Params
import org.segura.triptip.model.route.RouteWithWaypoint
import org.segura.triptip.model.travel.Travel

class CreateSummaryFragment : Fragment() {

    private val mainModel: TravelMainViewModel by activityViewModels()
    private val paramsModel: TravelParamsViewModel by activityViewModels()
    private val routeModel: TravelRouteViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val binding = DataBindingUtil.inflate<TravelFormSummaryBindingImpl>(layoutInflater, R.layout.travel_form_summary, container, false)
        val root = binding.root

        val newTravel = Travel(
            BaseTravel(
                mainModel.title.value!!,
                mainModel.start.value!!,
                mainModel.end.value!!,
                Params(
                    paramsModel.people.value!!,
                    paramsModel.days.value!!,
                    paramsModel.waterPerPeoplePerDay.value!!,
                    paramsModel.comment.value!!
                )
            ),
            RouteWithWaypoint(
                Route(),
                routeModel.waypoints.value!!
            )
        )
        binding.travel = newTravel
        binding.lifecycleOwner = viewLifecycleOwner

        root.findViewById<Button>(R.id.create_button).setOnClickListener {
            it.isEnabled = false
            lifecycleScope.launch {
                TravelDatabase.getInstance(this@CreateSummaryFragment.requireContext()).travelDao().insert(newTravel)
                // TODO: clear history
                startActivity(Intent(this@CreateSummaryFragment.requireContext(), MainActivity::class.java))
            }
        }

        return root
    }
}
