package dev.lordyorden.tradely

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.firebase.ui.auth.AuthUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import dev.lordyorden.tradely.databinding.ActionBarBinding
import dev.lordyorden.tradely.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        val actionbar = supportActionBar
        actionbar?.setDisplayShowHomeEnabled(true)
        actionbar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        actionBar?.setDisplayShowCustomEnabled(true)
        actionbar?.customView = ActionBarBinding.inflate(layoutInflater, binding.root, false).root


        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            supportActionBar?.hide()
            navView.visibility = View.GONE
        }

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.top_menu, menu)
        menu.getItem(0).setOnMenuItemClickListener {
            signOut()
            true
        }
        return true
    }

    private fun signOut() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                transactToLoginActivity()
            }
    }

    private fun transactToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}