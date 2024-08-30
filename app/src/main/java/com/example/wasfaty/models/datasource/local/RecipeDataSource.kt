package com.example.wasfaty.models.datasource.local

import com.example.wasfaty.models.entity.Recipe


class RecipeDataSource(private val recipeDao: RecipeDao) {

     fun getAllRecipes(): List<Recipe> = recipeDao.getAllRecipes()

     fun insertRecipe(recipe: Recipe): Long  {
       return recipeDao.insertRecipe(recipe)
    }

     fun getRecipeById(id: Int): Recipe? = recipeDao.getRecipeById(id)

     fun deleteRecipeById(id: Int) {
        recipeDao.deleteRecipeById(id)
    }

      fun getRecipeCount(): Int {
        return recipeDao.getRecipeCount()
    }
}
