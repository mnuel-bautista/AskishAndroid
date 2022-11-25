package dev.manuel.proyectomoviles

import android.app.Activity
import android.content.Context
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.content.edit
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.MenuProvider
import androidx.navigation.ui.setupWithNavController
import com.google.firebase.auth.FirebaseAuth
import dev.manuel.proyectomoviles.databinding.ActivityMainBinding
import dev.manuel.proyectomoviles.db.AppDatabase
import dev.manuel.proyectomoviles.ui.fragments.FragmentGruposDialog

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val auth = AppDatabase.getDatabase()?.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        Thread.sleep(1000)
        installSplashScreen()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menu.clear()
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return false
            }
        })

        setSupportActionBar(binding.toolbar)

        val topDestinations = setOf(
            R.id.fragmentCuestionarios,
            R.id.fragmentSalas,
            R.id.fragmentGrupos,
            R.id.FragmentLogin,
            R.id.FragmentRegistro,
            R.id.fragmentGruposDialog
        )

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(topLevelDestinationIds = topDestinations)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNavigation.setupWithNavController(navController)

        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAnchorView(R.id.fab)
                .setAction("Action", null).show()
        }

        findNavController(R.id.nav_host_fragment_content_main)
            .addOnDestinationChangedListener { _, destination, _ ->
                binding.fab.setOnClickListener { }
                when (destination.id) {
                    R.id.fragmentPreguntas, R.id.fragmentCuestionarioCompletado,
                    R.id.fragmentSalaEspera, R.id.FragmentLogin, R.id.FragmentRegistro, R.id.fragmentCuestionario -> {
                        binding.fab.visibility = View.INVISIBLE
                        binding.bottomNavigation.visibility = View.INVISIBLE
                    }
                    R.id.fragmentCuestionarios, R.id.fragmentSalas -> {
                        binding.fab.visibility = View.INVISIBLE
                        binding.bottomNavigation.visibility = View.VISIBLE
                    }
                    R.id.fragmentGrupos -> {
                        binding.fab.visibility = View.VISIBLE
                        binding.bottomNavigation.visibility = View.VISIBLE
                        binding.fab.setOnClickListener {
                            findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.fragmentGruposDialog)
                        }
                    }
                    else -> {
                        binding.fab.visibility = View.VISIBLE
                        binding.bottomNavigation.visibility = View.VISIBLE
                    }
                }
            }

        addOrRemoveMenu()
    }

    fun addOrRemoveMenu() {
        findNavController(R.id.nav_host_fragment_content_main).addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.fragmentGrupos, R.id.fragmentCuestionarios, R.id.fragmentSalas -> {
                    addMenu(auth)
                }
                else -> removeMenu()
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_logout -> {
                auth?.signOut()
                removeUserId()
                findNavController(R.id.nav_host_fragment_content_main)
                    .navigate(R.id.action_global_FragmentLogin)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }


}

const val UserCredentialsPreferences = "user-credentials"

fun Activity.removeUserId() {
    val preferences = getSharedPreferences(UserCredentialsPreferences, Context.MODE_PRIVATE)
    preferences.edit(commit = true) { remove("userId") }
}

fun Activity.getUserId(): String {
    val preferences = getSharedPreferences(UserCredentialsPreferences, Context.MODE_PRIVATE)
    return preferences.getString("userId", "") ?: ""
}

fun Activity.setUserId(userId: String) {
    val preferences = getSharedPreferences(UserCredentialsPreferences, Context.MODE_PRIVATE)
    preferences.edit(commit = true) { putString("userId", userId) }
}

fun MainActivity.removeMenu() {
    addMenuProvider(object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menu.clear()
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return false
        }
    })
}

fun MainActivity.addMenu(auth: FirebaseAuth?) {
    addMenuProvider(object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.menu_main, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return when (menuItem.itemId) {
                R.id.action_logout -> {
                    auth?.signOut()
                    findNavController(R.id.action_global_FragmentLogin)
                    true
                }
                else -> false
            }
        }
    })
}