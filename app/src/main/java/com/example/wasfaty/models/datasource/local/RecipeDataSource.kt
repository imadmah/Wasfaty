package com.example.wasfaty.models.datasource.local

import com.example.wasfaty.models.entity.Recipe


class RecipeDataSource(private val recipeDao: RecipeDao) {

     suspend fun getAllRecipes(): List<Recipe> = recipeDao.getAllRecipes()

     suspend fun insertRecipe(recipe: Recipe): Long  {
       return recipeDao.insertRecipe(recipe)
    }

     suspend fun getRecipeById(id: Int): Recipe? = recipeDao.getRecipeById(id)

     suspend fun deleteRecipeById(id: Int) {
        recipeDao.deleteRecipeById(id)
    }

    suspend fun getRecipeCount(): Int {
        return recipeDao.getRecipeCount()
    }

    suspend fun getBookmarkedRecipes() : List<Recipe>{
       return recipeDao.getBookmarkedRecipes()
    }
    suspend fun getRecipesByCategory(category: String):List<Recipe> {
       return recipeDao.getRecipesByCategory(category)
    }

     suspend fun updateBookmarkStatus(recipeId: Int, isBookmarked: Boolean){
        recipeDao.updateBookmarkStatus(recipeId,isBookmarked)
    }
}
