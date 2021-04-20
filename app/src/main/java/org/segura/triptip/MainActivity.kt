package org.segura.triptip

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.*
import org.segura.triptip.model.*
import org.segura.triptip.model.travel.Travel
import org.segura.triptip.views.TravelMenuItem
import java.util.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val viewModel: TravelViewModel by viewModels()

    private lateinit var travels: List<Travel>
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var db: TravelDatabase
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar = findViewById(R.id.progress)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        setupDrawer(toolbar)
        setupBottomMenu()

        lifecycleScope.launch {
            db = TravelDatabase.getInstance(applicationContext)
            travels = db.travelDao().selectNonArchived()
            if (travels.isEmpty()) {
                startActivity(Intent(this@MainActivity, CreateActivity::class.java))
            }

            val navigationView: NavigationView = findViewById(R.id.drawer_menu)
            for (travel in travels) {
                navigationView.menu.add(
                    R.id.trips,
                    R.id.travel_id_salt xor travel.travel.travelId,
                    Menu.FIRST,
                    travel.travel.title + travel.travel.travelId
                ).apply {
                    setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
                    setIcon(R.drawable.ic_archive)
                    isCheckable = true
                    actionView = TravelMenuItem(this@MainActivity, travel)
                }
            }
        }
    }

    private fun setupBottomMenu() {
        val navView: BottomNavigationView = findViewById(R.id.bottom_nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_summary, R.id.navigation_route, R.id.navigation_weather, R.id.navigation_food, R.id.navigation_info
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun setupDrawer(toolbar: Toolbar) {
        drawerLayout = findViewById(R.id.drawer_layout)
        val drawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()

        val navigationView: NavigationView = findViewById(R.id.drawer_menu)
        navigationView.setNavigationItemSelectedListener(this)
        val createButton: FloatingActionButton = navigationView.getHeaderView(0).findViewById(R.id.action_create)
        createButton.setOnClickListener { startActivity(Intent(this, CreateActivity::class.java)) }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.top, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> startActivity(Intent(this, SettingsActivity::class.java))
        }
        val possibleTravelId = item.itemId xor R.id.travel_id_salt
        travels.find { it.travel.travelId == possibleTravelId }?.let {
            viewModel.selectItem(it)
        }
        drawerLayout.closeDrawers()
        return true
    }

    fun startProgress() {
        progressBar.visibility = View.VISIBLE
    }

    fun stopProgress() {
        progressBar.visibility = View.INVISIBLE
    }
}
