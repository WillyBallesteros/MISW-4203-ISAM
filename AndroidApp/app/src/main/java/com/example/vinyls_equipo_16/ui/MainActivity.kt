package com.example.vinyls_equipo_16.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.example.vinyls_equipo_16.R
import com.example.vinyls_equipo_16.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener el NavHostFragment y establecer el NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        val navController = navHostFragment.navController

        // Definir los ID de los destinos de nivel superior en el DrawerLayout
        appBarConfiguration = AppBarConfiguration(
            setOf(R.id.albumFragment, R.id.musicianFragment, R.id.collectorFragment), binding.drawerLayout
        )

        binding.navView.setupWithNavController(navController)

        // Configurar el ActionBar para que trabaje con el NavController
        setSupportActionBar(findViewById(R.id.my_toolbar))
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment_content_main).navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}