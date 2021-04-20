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
import org.segura.triptip.databinding.TravelFormParamsBinding
import org.segura.triptip.model.form.TravelParamsViewModel

class CreateParamsFragment : Fragment() {

    private val model: TravelParamsViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val binding = DataBindingUtil.inflate<TravelFormParamsBinding>(layoutInflater, R.layout.travel_form_params, container, false)
        val root = binding.root

        binding.travelParams = model
        binding.lifecycleOwner = viewLifecycleOwner

        root.findViewById<Button>(R.id.next_button).setOnClickListener {
            val findNavController = findNavController()
            findNavController.navigate(R.id.action_next)
        }
        return root
    }
}
