package com.example.wasfaty.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wasfaty.models.entity.MealPlan
import com.example.wasfaty.models.entity.Recipe
import com.example.wasfaty.models.repository.MealPlanRepository
import com.example.wasfaty.models.repository.RecipeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MealPlanViewModel(private val repository: MealPlanRepository,private val repository1: RecipeRepository) : ViewModel() {

    // LiveData for MealPlans by selected day
    private val _mealPlansByDate = MutableLiveData<List<MealPlan>>(emptyList())
    val mealPlansByDate: LiveData<List<MealPlan>> = _mealPlansByDate

    // LiveData for fetched recipes
    private val _recipesForMealPlans = MutableLiveData<List<Recipe>>(emptyList())
    val recipesForMealPlans: LiveData<List<Recipe>> = _recipesForMealPlans

    // LiveData for selected day
    private val _selectedDay = MutableLiveData(1)
    val selectedDay: LiveData<Int> = _selectedDay

    // Insert a meal plan into the database
    fun insertMealPlan(mealPlan: MealPlan) = viewModelScope.launch {
        repository.insertMealPlan(mealPlan)
    }

    // Fetch meal plans for a given day
    private fun getMealPlansByDate(day: String) = viewModelScope.launch {
        val mealPlans = repository.getMealPlansByDate(day)
        _mealPlansByDate.value = mealPlans

        // Fetch corresponding recipes for each meal plan
        val recipeIds = mealPlans.map { it.recipeId }
        val recipes = recipeIds.map { recipeId ->
            async { repository1.getRecipeById(recipeId) }
        }.mapNotNull { it.await() } // Await all async calls and filter out null values

        _recipesForMealPlans.value = recipes
    }

    // Handle day selection for meal planning
    fun onDaySelected(day: Int) {
        _selectedDay.value = day
        getMealPlansByDate(day.toString())
    }
}
