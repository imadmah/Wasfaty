package com.example.wasfaty.view

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.wasfaty.R
import com.example.wasfaty.models.entity.Recipe
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomAppBarDefaults.containerColor
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import com.example.wasfaty.models.entity.MealPlan
import com.example.wasfaty.ui.theme.GreenMain
import com.example.wasfaty.ui.theme.SearchBar
import com.example.wasfaty.viewmodel.MealPlanViewModel
import com.example.wasfaty.utils.CalendarPicker
import com.example.wasfaty.viewmodel.HomeScreenViewModel


@Composable
fun RecipeScreen(
    recipe: Recipe,
    homeScreenViewModel : HomeScreenViewModel,
    mealplanviewModel: MealPlanViewModel,
    modifier: Modifier = Modifier
) {
    var showCalendar by remember { mutableStateOf(false) } // Control showing calendar

    val allRecipes by homeScreenViewModel.allRecipes.observeAsState(initial = emptyList())
    // Find the current recipe in the allRecipes list
    val currentRecipe = allRecipes.find { it.id == recipe.id } ?: recipe


    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        // Recipe image section
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = if (recipe.imagePath != null) {
                    // Check if imagePath is an integer resource ID
                    val resId = recipe.imagePath.toIntOrNull()
                    if (resId != null) {
                        painterResource(id = resId)
                    } else {
                        rememberAsyncImagePainter(
                            model = Uri.parse(recipe.imagePath), // Load from phone storage
                            error = painterResource(id = R.drawable.recipetem)
                        )
                    }
                } else {
                    painterResource(id = R.drawable.recipetem)
                },
                contentDescription = "Recipe Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .height(250.dp)
            )

            // Row for bookmark and meal plan icons
            Row(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                // Bookmark Icon

                IconButton(
                    onClick = {
                        homeScreenViewModel.updateBookmarkStatus(
                            recipe.id,
                            !currentRecipe.isBookmarked
                        )
                    }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.bookmark),
                        contentDescription = "Bookmark",
                        // Conditionally set tint based on bookmark status
                        tint = if(currentRecipe.isBookmarked) GreenMain else Color.White,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(36.dp)
                    )
                }
            }
        }

        // Recipe title and info
        // Recipe Title Section
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 4.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically,

            ){
                Text(
                    text = "Plan This Meal",
                    color = GreenMain
                )
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = {
                    showCalendar = true // Show calendar when clicked
                },
                    colors = ButtonDefaults.buttonColors( containerColor = Color.White )
                )
                {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add to Meal Plan",
                        tint = Color.Black
                    )
                }

            }


            Text(
                text = recipe.title,
                color = GreenMain
            )

            Text(
                text = recipe.cookingSteps,

                color = Color.White,
                modifier = Modifier.padding(top = 4.dp)
            )

        // Ingredients section
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.clock_hour_4),
                    contentDescription = "Time",
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
                Text(
                    text = "${recipe.time}",
                    color = Color.White,
                    modifier = Modifier.padding(start = 4.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                Icon(
                    painter = painterResource(id = R.drawable.skill_level),
                    contentDescription = "Difficulty",
                    modifier = Modifier.size(24.dp),
                    tint = Color.White
                )
                Text(
                    text = recipe.difficulty,
                    color = Color.White,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }

        // Display CalendarPicker when 'Add MealPlan' is clicked
        if (showCalendar) {
            CalendarPicker(
                onDaySelected = { day ->
                    val mealPlan = MealPlan(
                        recipeId = recipe.id, // Pass only the required fields
                        date = day.toString(),
                        mealType = recipe.category
                    )
                    mealplanviewModel.insertMealPlan(mealPlan) // Insert the MealPlan without specifying an ID
                    showCalendar = false // Hide the calendar after selection
                }

            )
        }
    }
}


