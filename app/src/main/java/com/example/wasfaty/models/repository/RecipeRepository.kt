package com.example.wasfaty.models.repository

import com.example.wasfaty.models.datasource.local.RecipeDataSource
import com.example.wasfaty.models.entity.Recipe


class RecipeRepository(private val localDataSource: RecipeDataSource) {

    suspend fun getAllRecipes(): List<Recipe> = localDataSource.getAllRecipes()

    suspend fun addRecipe(recipe: Recipe) : Long {
       return localDataSource.insertRecipe(recipe)
    }

    suspend fun getRecipeById(id: Int): Recipe? = localDataSource.getRecipeById(id)

    suspend fun deleteRecipeById(id: Int) {
        localDataSource.deleteRecipeById(id)
    }

    suspend fun getRecipeCount(): Int {
        return localDataSource.getRecipeCount()
    }

    suspend fun getBookmarkedRecipes() : List<Recipe> {
       return localDataSource.getBookmarkedRecipes()
    }
    suspend fun getRecipesByCategory(category: String):List<Recipe> {
        return localDataSource.getRecipesByCategory(category)
    }
    suspend fun updateBookmarkStatus(recipeId: Int, isBookmarked: Boolean){
        localDataSource.updateBookmarkStatus(recipeId,isBookmarked)
    }
}
