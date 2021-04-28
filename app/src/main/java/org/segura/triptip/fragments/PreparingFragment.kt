package org.segura.triptip.fragments

import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import org.segura.triptip.R
import org.segura.triptip.model.TravelDatabase
import org.segura.triptip.model.travel.Travel

class PreparingFragment : TravelFragment() {
    override val layout: Int
        get() = R.layout.fragment_preparing

    override fun onTravelUpdate(root: View, item: Travel) {
        val container = root.findViewById<LinearLayout>(R.id.preparing_list)
        container.removeAllViews()
        item.preparingList.forEachIndexed { index, preparingItem ->
            val view = layoutInflater.inflate(R.layout.preparing_item, container, false).apply {
                this.findViewById<TextView>(R.id.index).text = (index + 1).toString()
                this.findViewById<TextView>(R.id.name).text = preparingItem.name
                this.findViewById<TextView>(R.id.count).text = preparingItem.count.toString()
                this.findViewById<TextView>(R.id.units).text = preparingItem.units
                this.findViewById<CheckBox>(R.id.checked).apply {
                    this.isChecked = preparingItem.done
                    if (this.isEnabled) {
                        this.setOnCheckedChangeListener { _, checked ->
                            preparingItem.done = checked
                            lifecycleScope.launch {
                                TravelDatabase.getInstance(this@PreparingFragment.requireContext()).travelDao().insert(preparingItem)
                            }
                        }
                    }
                }
            }
            container.addView(view)
        }
    }
}
