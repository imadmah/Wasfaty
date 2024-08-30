package com.example.wasfaty.models.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.wasfaty.models.entity.Recipe

@Dao
interface RecipeDao {

    @Query("SELECT * FROM recipes")
     fun getAllRecipes(): List<Recipe>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertRecipe(recipe: Recipe) : Long

    @Query("SELECT * FROM recipes WHERE id = :id")
     fun getRecipeById(id: Int): Recipe?

    @Query("DELETE FROM recipes WHERE id = :id")
     fun deleteRecipeById(id: Int)

    @Query("SELECT COUNT(*) FROM recipes")
     fun getRecipeCount(): Int
}
