package com.example.wasfaty.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wasfaty.models.entity.Recipe
import com.example.wasfaty.models.repository.RecipeRepository
import kotlinx.coroutines.launch

class HomeScreenViewModel(private val repository: RecipeRepository) : ViewModel() {

    private val _newest5Recipes = MutableLiveData<List<Recipe>>(emptyList())
    val newest5Recipes: LiveData<List<Recipe>> = _newest5Recipes

    private val _allRecipes = MutableLiveData<List<Recipe>>(emptyList())
    val allRecipes: LiveData<List<Recipe>> = _allRecipes

    init {
        loadRecipes()
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
            repository.addRecipe(recipe)
            // Update LiveData after adding a new recipe
            val updatedRecipes = _allRecipes.value.orEmpty() + recipe
            _allRecipes.value = updatedRecipes
            updateNewest5Recipes(updatedRecipes)
        }
    }



    private fun updateNewest5Recipes(recipes: List<Recipe>) {
        _newest5Recipes.value = recipes.takeLast(5)
    }
}
