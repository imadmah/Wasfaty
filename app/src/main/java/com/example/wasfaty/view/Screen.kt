package com.example.wasfaty.view

sealed class Screen(val route: String) {
    data object Home : Screen("Home")
    data object BookmarkScreen : Screen("BookmarkScreen")
    data object PlanRecipeScreen : Screen("PlanRecipeScreen")
    data object Settings : Screen("Settings")
    data object RecipeDetails : Screen("RecipeDetails/{recipeId}") {
        fun createRoute(recipeId: Int) = "RecipeDetails/$recipeId"
    }
}
