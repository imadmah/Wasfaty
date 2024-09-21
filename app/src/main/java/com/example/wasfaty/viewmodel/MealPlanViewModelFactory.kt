package com.example.wasfaty.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wasfaty.models.repository.MealPlanRepository
import com.example.wasfaty.models.repository.RecipeRepository

class MealPlanViewModelFactory(private val repository: MealPlanRepository,private val repository1: RecipeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MealPlanViewModel::class.java))
            return MealPlanViewModel(repository,repository1) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
