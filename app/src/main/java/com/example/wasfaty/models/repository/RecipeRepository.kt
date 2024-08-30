package com.example.wasfaty.models.repository

import com.example.wasfaty.models.datasource.local.RecipeDataSource
import com.example.wasfaty.models.entity.Recipe


class RecipeRepository(private val localDataSource: RecipeDataSource) {

     fun getAllRecipes(): List<Recipe> = localDataSource.getAllRecipes()

     fun addRecipe(recipe: Recipe) : Long {
       return localDataSource.insertRecipe(recipe)
    }

     fun getRecipeById(id: Int): Recipe? = localDataSource.getRecipeById(id)

     fun deleteRecipeById(id: Int) {
        localDataSource.deleteRecipeById(id)
    }
      fun getRecipeCount(): Int {
        return localDataSource.getRecipeCount()
    }
}
