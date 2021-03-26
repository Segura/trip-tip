package org.segura.triptip.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.segura.triptip.MainActivity
import org.segura.triptip.model.Travel

open class TravelFragment : Fragment() {
    lateinit var travel : Travel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        travel = (activity as MainActivity).travel
        return super.onCreateView(inflater, container, savedInstanceState)
    }
}
