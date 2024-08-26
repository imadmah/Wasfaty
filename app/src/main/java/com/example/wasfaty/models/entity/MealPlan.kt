package com.example.wasfaty.models.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_plans")
data class MealPlan(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String, // Format as needed, e.g., YYYY-MM-DD
    val recipeIds: List<Long> // Store IDs of recipes in the meal plan
)