package com.example.wasfaty.view.activity

import com.example.wasfaty.view.AddRecipeScreen
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.wasfaty.ui.theme.WasfatyTheme
import com.example.wasfaty.models.datasource.local.RecipeDataSource
import com.example.wasfaty.models.datasource.local.RecipeDatabase
import com.example.wasfaty.models.entity.Recipe
import com.example.wasfaty.models.repository.RecipeRepository
import com.example.wasfaty.view.HomeScreen
import com.example.wasfaty.view.RecipeScreen
import com.example.wasfaty.view.Screen
import com.example.wasfaty.view.navBar.ColorButtonNavBar
import com.example.wasfaty.viewmodel.HomeScreenViewModel
import com.example.wasfaty.viewmodel.HomeScreenViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
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
    val RecipebyId by homeScreenViewModel.RecipeById.observeAsState()

    Scaffold(
        bottomBar = {
            ColorButtonNavBar(
                onHomeClick = { navController.navigate(Screen.Home.route) },
                onBookmarkClick = { navController.navigate(Screen.BookmarkScreen.route) },
                onCalendarClick = { navController.navigate(Screen.PlanRecipeScreen.route) },
                onSettingsClick = { navController.navigate(Screen.Settings.route) }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(homeScreenViewModel) { recipeId ->
                    navController.navigate(Screen.RecipeDetails.createRoute(recipeId))
                }
            }
            composable(Screen.RecipeDetails.route) { backStackEntry ->
                val recipeId = backStackEntry.arguments?.getString("recipeId")?.toIntOrNull()
                recipeId?.let {homeScreenViewModel.getRecipeById(it) }
                val recipe = RecipebyId
                if (recipe != null) { //ToDo know the smart Cast diffrenece and shit here aka if i use val recipe etc
                    RecipeScreen(recipe)
                }
            }
            composable(Screen.Settings.route) {
                AddRecipeScreen { recipe ->
                    homeScreenViewModel.addRecipe(recipe)

                }
            }
            composable(Screen.PlanRecipeScreen.route) {
                // PlanRecipeScreen logic
            }
            composable(Screen.BookmarkScreen.route) {
                HomeScreen(homeScreenViewModel) { recipeId ->
                    navController.navigate(Screen.RecipeDetails.createRoute(recipeId))
                }
            }
        }
    }
}

