package org.segura.triptip

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


class CreateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.travel_form)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}
