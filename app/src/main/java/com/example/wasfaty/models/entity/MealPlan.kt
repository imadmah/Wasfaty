package com.example.wasfaty.models.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "meal_plan")
data class MealPlan(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "recipe_id") val recipeId: Int,
    @ColumnInfo(name = "date") val date: String,
    @ColumnInfo(name = "meal_type") val mealType: String? = null
)


