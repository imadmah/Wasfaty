package com.example.wasfaty.models.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    val title: String,
    val time: String,
    val difficulty: String,
    val ingredients : String,
    val cookingSteps : String,
    val imagePath: String? = null,
    val isBookmarked: Boolean = false,
    val category: String,

    )