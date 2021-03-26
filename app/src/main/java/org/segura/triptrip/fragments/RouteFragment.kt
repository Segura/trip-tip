package org.segura.triptip.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import org.segura.triptip.R

class RouteFragment : TravelFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val root = inflater.inflate(R.layout.fragment_route, container, false)
        val textView: TextView = root.findViewById(R.id.title)
        travel.title.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}
