package org.segura.triptip.views

import android.content.Context
import android.text.format.DateFormat
import android.widget.LinearLayout
import android.widget.TextView
import org.segura.triptip.R
import org.segura.triptip.model.travel.Travel

class TravelMenuItem(context: Context) : LinearLayout(context) {

    constructor(context: Context, travel: Travel) : this(context) {
        inflate(context, R.layout.travel_menu_item, this)
        val formatted = DateFormat.getDateFormat(context)
        val fromView: TextView = findViewById(R.id.from)
        val toView: TextView = findViewById(R.id.to)
        fromView.text = formatted.format(travel.baseTravel.startAt)
        toView.text = formatted.format(travel.baseTravel.endAt)
    }
}
