package org.segura.triptip.fragments.create

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import org.segura.triptip.R
import org.segura.triptip.databinding.TravelFormMainBinding
import org.segura.triptip.model.form.TravelMainViewModel
import java.util.*

class CreateMainFragment : Fragment() {

    private val model: TravelMainViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        super.onCreateView(inflater, container, savedInstanceState)
        val binding = DataBindingUtil.inflate<TravelFormMainBinding>(layoutInflater, R.layout.travel_form_main, container, false)
        val root = binding.root

        binding.travelMain = model
        binding.lifecycleOwner = viewLifecycleOwner

        val now = Calendar.getInstance()
        val onStartClick = View.OnClickListener {
            val datePickerDialog = DatePickerDialog(
                this@CreateMainFragment.requireContext(),
                { _, year, monthOfYear, dayOfMonth ->
                    model.start.value = this@CreateMainFragment.assemblyDate(year, monthOfYear, dayOfMonth)
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
            )
            model.end.value?.let { datePickerDialog.datePicker.maxDate = it.time }
            datePickerDialog.show()
        }
        val onEndClick = View.OnClickListener {
            val datePickerDialog = DatePickerDialog(
                this@CreateMainFragment.requireContext(),
                { _, year, monthOfYear, dayOfMonth ->
                    model.end.value = this@CreateMainFragment.assemblyDate(year, monthOfYear, dayOfMonth)
                },
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
            )
            model.start.value?.let { datePickerDialog.datePicker.minDate = it.time }
            datePickerDialog.show()
        }

        root.findViewById<EditText>(R.id.title_field).doOnTextChanged { text, _, _, _ -> model.title.value = text.toString() }
        root.findViewById<EditText>(R.id.start_date_field).setOnClickListener(onStartClick)
        root.findViewById<ImageButton>(R.id.start_date_button).setOnClickListener(onStartClick)
        root.findViewById<EditText>(R.id.end_date_field).setOnClickListener(onEndClick)
        root.findViewById<ImageButton>(R.id.end_date_button).setOnClickListener(onEndClick)
        root.findViewById<Button>(R.id.next_button).setOnClickListener { findNavController().navigate(R.id.action_next) }

        return root
    }

    fun assemblyDate(year: Int, month: Int, day: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        return calendar.time
    }
}
