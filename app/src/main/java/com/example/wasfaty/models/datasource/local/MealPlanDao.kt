package com.example.wasfaty.models.datasource.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.wasfaty.models.entity.MealPlan

@Dao
interface MealPlanDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertMealPlan(mealPlan: MealPlan)

    @Query("SELECT * FROM meal_plan WHERE date = :date")
     fun getMealPlansByDate(date: String): List<MealPlan>

    @Delete
     fun deleteMealPlan(mealPlan: MealPlan)
}
