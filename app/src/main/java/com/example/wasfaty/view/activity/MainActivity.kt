package com.example.wasfaty.view.activity

import com.example.wasfaty.view.AddRecipeScreen
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.wasfaty.models.datasource.local.RecipeDataSource
import com.example.wasfaty.models.datasource.local.RecipeDatabase
import com.example.wasfaty.models.repository.RecipeRepository
import com.example.wasfaty.ui.theme.WasfatyTheme
import com.example.wasfaty.view.HomeScreen
import com.example.wasfaty.view.navBar.ColorButtonNavBar
import com.example.wasfaty.viewmodel.HomeScreenViewModel
import com.example.wasfaty.viewmodel.HomeScreenViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val PREFS_NAME = "wasfaty_prefs"
    private val KEY_ONBOARDING_COMPLETED = "onboarding_completed"

    private lateinit var homeScreenViewModel: HomeScreenViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // Check if onboarding is completed
        if (!isOnboardingCompleted(sharedPreferences)) {
            startActivity(Intent(this, OnBoarding::class.java))
            markOnboardingCompleted(sharedPreferences)
            finish()
        } else {
            // Initialize the database and ViewModel on a background thread
            lifecycleScope.launch(Dispatchers.IO) {
                val database = RecipeDatabase.getDatabase(applicationContext)
                val localDataSource = RecipeDataSource(database.recipeDao())
                val repository = RecipeRepository(localDataSource)

                // Initialize ViewModel using ViewModelProvider
                homeScreenViewModel = ViewModelProvider(
                    this@MainActivity,
                    HomeScreenViewModelFactory(repository)
                )[HomeScreenViewModel::class.java]

                // Once the ViewModel is ready, set the content
                runOnUiThread {
                    setContent {
                        WasfatyTheme {
                            MyApp(homeScreenViewModel)
                        }
                    }
                }
            }
        }
    }

    private fun isOnboardingCompleted(sharedPreferences: SharedPreferences): Boolean {
        return sharedPreferences.getBoolean(KEY_ONBOARDING_COMPLETED, false)
    }

    private fun markOnboardingCompleted(sharedPreferences: SharedPreferences) {
        sharedPreferences.edit().putBoolean(KEY_ONBOARDING_COMPLETED, true).apply()
    }
}

// ViewModelFactory to provide necessary dependencies to the ViewModel


@Preview(showBackground = true)
@Composable
fun Preview() {
     lateinit var homeScreenViewModel: HomeScreenViewModel
    WasfatyTheme {
        MyApp(homeScreenViewModel = homeScreenViewModel)
    }
}

@Composable
fun MyApp(homeScreenViewModel: HomeScreenViewModel) {
    val navController = rememberNavController()
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    
    Scaffold(
        bottomBar = {
            ColorButtonNavBar(
                onHomeClick = { navController.navigate("Home") },
                onBookmarkClick = { navController.navigate("BookmarkScreen") },
                onCalendarClick = { navController.navigate("PlanRecipeScreen") },
                onSettingsClick = { navController.navigate("Settings") }
            )
        }
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = "Home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("Home") {
                HomeScreen(homeScreenViewModel)
            }
            composable("Settings") {
                AddRecipeScreen{recipe ->
                    homeScreenViewModel.addRecipe(recipe)
                }
            }
            composable("PlanRecipeScreen") {
                HomeScreen(homeScreenViewModel)
            }
            composable("BookmarkScreen") {
                HomeScreen(homeScreenViewModel)
            }
        }
    }
}
