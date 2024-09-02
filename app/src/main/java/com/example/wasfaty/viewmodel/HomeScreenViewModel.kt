package com.example.wasfaty.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wasfaty.models.datasource.local.InitialRecipes
import com.example.wasfaty.models.entity.Recipe
import com.example.wasfaty.models.repository.RecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeScreenViewModel(private val repository: RecipeRepository) : ViewModel() {

    private val _newest5Recipes = MutableLiveData<List<Recipe>>(emptyList())
    val newest5Recipes: LiveData<List<Recipe>> = _newest5Recipes

    private val _allRecipes = MutableLiveData<List<Recipe>>(emptyList())
    val allRecipes: LiveData<List<Recipe>> = _allRecipes

    private val _recipeById = MutableLiveData<Recipe>()
    val recipeById: LiveData<Recipe> = _recipeById

    private val _bookmarkedRecipes = MutableLiveData<List<Recipe>>(emptyList())
    val bookmarkedRecipes: LiveData<List<Recipe>> = _bookmarkedRecipes

    private val _chosenCategoryRecipes = MutableLiveData<List<Recipe>>(emptyList())
    val chosenCategory: LiveData<List<Recipe>> = _chosenCategoryRecipes

    init {
        initializeRecipes()
        loadRecipes()

    }

    private fun initializeRecipes() {
        viewModelScope.launch {
            val isFirstRun = withContext(Dispatchers.IO) {
                checkIfFirstRun()
            }
            if (isFirstRun) {
                addInitialRecipes()
            }
        }
    }

    private suspend fun checkIfFirstRun(): Boolean {
        return withContext(Dispatchers.IO) {
            repository.getRecipeCount() == 0
        }
    }

    private fun addInitialRecipes() {
        val initialRecipes = InitialRecipes
        viewModelScope.launch(Dispatchers.IO) {
            initialRecipes.forEach {
                repository.addRecipe(it)
            }
        }
    }

    private fun loadRecipes() {
        viewModelScope.launch {
            val recipes = withContext(Dispatchers.IO) {
                repository.getAllRecipes()
            }
            withContext(Dispatchers.Main){
            _allRecipes.value = recipes
            updateNewest5Recipes(recipes)
            getBookmarkedRecipes()
            }
        }
    }

    fun addRecipe(recipe: Recipe) {
        viewModelScope.launch(Dispatchers.IO) {
            val generatedId = repository.addRecipe(recipe).toInt()
            // Update the recipe object with the new ID (if needed)
            recipe.id = generatedId
            withContext(Dispatchers.Main) {
                // Update LiveData after adding a new recipe
                val updatedRecipes = _allRecipes.value.orEmpty() + recipe
                _allRecipes.value = updatedRecipes
                updateNewest5Recipes(updatedRecipes)
            }
        }
    }

    private fun updateNewest5Recipes(recipes: List<Recipe>) {
        _newest5Recipes.value = recipes.takeLast(5)
    }

    fun getRecipeById(id: Int) {
        viewModelScope.launch {
            val recipe = withContext(Dispatchers.IO) {
                repository.getRecipeById(id)
            }
            _recipeById.value = recipe!!
        }
    }

    fun toggleBookmark(recipeId: Int, isBookmarked: Boolean) {
        viewModelScope.launch {

        }
    }

    private fun getBookmarkedRecipes() {
        viewModelScope.launch {
           val bookmarkedRecipes = withContext(Dispatchers.IO){
            repository.getBookmarkedRecipes()
           }
            _bookmarkedRecipes.value = bookmarkedRecipes
        }
    }

    fun getRecipesByCategory(category: String) {
        viewModelScope.launch {
            val chosenRecipes = withContext(Dispatchers.IO){
                repository.getRecipesByCategory(category)
            }
            _chosenCategoryRecipes.value = chosenRecipes
        }
    }

    fun updateBookmarkStatus(recipeId: Int, isBookmarked: Boolean) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                // Update the bookmark status in the repository (database)
                repository.updateBookmarkStatus(recipeId, isBookmarked)
            }

            withContext(Dispatchers.Main) {
                // Update the bookmark status in _allRecipes
                val updatedAllRecipes = _allRecipes.value?.map { recipe ->
                    if (recipe.id == recipeId) {
                        recipe.copy(isBookmarked = isBookmarked)
                    } else {
                        recipe
                    }
                }?.toMutableList()

                _allRecipes.value = updatedAllRecipes!!

                // Re-filter the bookmarked recipes
                _bookmarkedRecipes.value = updatedAllRecipes?.filter { it.isBookmarked }

                // Debug: Print updated bookmarked recipes
                Log.d("UpdateBookmarkStatus", "Updated Bookmarked Recipes: ${_bookmarkedRecipes.value}")
            }
        }
    }

}

