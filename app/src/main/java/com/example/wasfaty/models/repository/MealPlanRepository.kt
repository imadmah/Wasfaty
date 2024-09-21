package com.example.wasfaty.models.repository

import com.example.wasfaty.models.datasource.local.MealPlanDao
import com.example.wasfaty.models.entity.MealPlan

class MealPlanRepository(private val mealPlanDao: MealPlanDao) {
    suspend fun insertMealPlan(mealPlan: MealPlan) = mealPlanDao.insertMealPlan(mealPlan)
    suspend fun getMealPlansByDate(date: String) = mealPlanDao.getMealPlansByDate(date)
    suspend fun deleteMealPlan(mealPlan: MealPlan) = mealPlanDao.deleteMealPlan(mealPlan)
}
