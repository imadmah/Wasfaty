package com.example.wasfaty.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Index
import com.example.wasfaty.models.datasource.local.InitialRecipes
import com.example.wasfaty.models.entity.Recipe
import com.example.wasfaty.models.repository.RecipeRepository
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val repository: RecipeRepository) : ViewModel() {

    private val _newest5Recipes = MutableLiveData<List<Recipe>>(emptyList())
    val newest5Recipes: LiveData<List<Recipe>> = _newest5Recipes

    private val _allRecipes = MutableLiveData<List<Recipe>>(emptyList())
    val allRecipes: LiveData<List<Recipe>> = _allRecipes

    private val _recipeById = MutableLiveData<Recipe>()
    val RecipeById: LiveData<Recipe> = _recipeById


    init {
        initializeRecipes()
        loadRecipes()
    }

    private fun initializeRecipes(){
        viewModelScope.launch {
            val isFirstRun = checkIfFirstRun()
            if (isFirstRun) {
                addInitialRecipes()
            }
        }
    }

    private suspend fun checkIfFirstRun(): Boolean {
        return repository.getRecipeCount() == 0
    }
    private fun addInitialRecipes() {
        val initialRecipes =InitialRecipes
        viewModelScope.launch {
            initialRecipes.forEach{
                repository.addRecipe(it)
            }
        }
    }
    private fun loadRecipes() {
        viewModelScope.launch {
            val recipes = repository.getAllRecipes()
            _allRecipes.value = recipes
            updateNewest5Recipes(recipes)
        }
    }

    fun addRecipe(recipe: Recipe) {
        viewModelScope.launch {
            val generatedId = repository.addRecipe(recipe).toInt()
            // Update the recipe object with the new ID (if needed)
            recipe.id = generatedId
            // Update LiveData after adding a new recipe
            val updatedRecipes = _allRecipes.value.orEmpty() + recipe
            _allRecipes.value = updatedRecipes
            updateNewest5Recipes(updatedRecipes)
        }
    }

    private fun updateNewest5Recipes(recipes: List<Recipe>) {
        _newest5Recipes.value = recipes.takeLast(5)
    }

    fun getRecipeById(id:Int){
        viewModelScope.launch {
            _recipeById.value = repository.getRecipeById(id)
        }
    }

}
