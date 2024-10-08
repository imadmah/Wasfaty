package com.example.wasfaty.models.datasource.local

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.wasfaty.models.entity.MealPlan
import com.example.wasfaty.models.entity.Recipe

@Database(entities = [Recipe::class, MealPlan::class], version = 1, exportSchema = false)
abstract class RecipeDatabase : RoomDatabase() {

    abstract fun recipeDao(): RecipeDao
    abstract fun mealPlanDao() : MealPlanDao

    companion object {
        @Volatile
        private var INSTANCE: RecipeDatabase? = null

        fun getDatabase(context: Context): RecipeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecipeDatabase::class.java,
                    "recipe_database"
                    ).allowMainThreadQueries().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
