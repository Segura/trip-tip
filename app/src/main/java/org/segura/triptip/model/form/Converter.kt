package org.segura.triptip.model.form

import android.text.format.DateFormat
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseMethod
import androidx.preference.PreferenceManager
import java.util.*

object Converter {
    @JvmStatic
    fun dateToString(date: Date?): String {
        return date.toString()
    }

    @JvmStatic
    fun intToString(num: Int?): String {
        return num?.toString() ?: ""
    }

    @JvmStatic
    @InverseMethod("intToString")
    fun stringToInt(str: String?): Int {
        return Integer.parseInt(str ?: "0")
    }

    @BindingAdapter("date")
    @JvmStatic
    fun setDateText(view: EditText, date: Date?) {
        val formatter = DateFormat.getDateFormat(view.context)
        view.setText(if (date != null) formatter.format(date) else "")
    }

    @BindingAdapter("distance")
    @JvmStatic
    fun setDistanceText(view: TextView, distance: Float) {
        val preferences = PreferenceManager.getDefaultSharedPreferences(view.context)
//  preferences.
//  val formatter = DateFormat.getDateFormat(view.context)
//  view.setText(if (date != null) formatter.format(date) else "")
        val text = distance.toString() + preferences.getString("distance_units", "none")
        view.text = text
    }
}
