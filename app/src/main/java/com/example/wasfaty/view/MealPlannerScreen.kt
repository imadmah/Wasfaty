package com.example.wasfaty.view

import android.net.Uri
import android.widget.Space
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.wasfaty.R
import com.example.wasfaty.models.entity.MealPlan
import com.example.wasfaty.models.entity.Recipe
import com.example.wasfaty.ui.theme.GreenMain
import com.example.wasfaty.viewmodel.HomeScreenViewModel
import com.example.wasfaty.viewmodel.MealPlanViewModel
import kotlinx.coroutines.CoroutineScope


import java.util.Calendar
import java.util.Locale

@Composable
fun PlanYourMealScreen(viewModel: MealPlanViewModel) {
    val calendar = Calendar.getInstance()
    val today = calendar.get(Calendar.DAY_OF_MONTH)

    // Get the number of days in the current month
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

    // Prepare list of days for the month with names and avoid past days
    val daysInMonthWithNames = remember {
        List(daysInMonth) { day ->
            calendar.set(Calendar.DAY_OF_MONTH, day + 1)
            val dayName = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault())
            DayInfo(day + 1, dayName ?: "")
        }.filter { it.day >= today }
    }

    val selectedDay by viewModel.selectedDay.observeAsState(today)
    val mealPlansForSelectedDay by viewModel.mealPlansByDate.observeAsState()
    val recipesForMealPlans by viewModel.recipesForMealPlans.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Day selector to choose a day
        DaySelector(
            daysWithNames = daysInMonthWithNames,
            selectedDay = selectedDay,
            onDaySelected = { day ->
                viewModel.onDaySelected(day)
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Display meal plans for the selected day
        if (mealPlansForSelectedDay?.isNotEmpty() == true) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(
                    items = mealPlansForSelectedDay!!,
                ) { mealPlan ->
                    val recipe = recipesForMealPlans.firstOrNull { it.id == mealPlan.recipeId }
                    if (recipe != null) {
                        MealPlanItem(mealPlan, recipe)
                    }
                }
            }
        } else {
            // If no meal plans are found for the day, show a message
            Text(
                text = "No meal plans available for this day.",
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }

    }
}

@Composable
fun DaySelector(
    daysWithNames: List<DayInfo>,
    selectedDay: Int,
    onDaySelected: (Int) -> Unit
) {
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        items(daysWithNames) { dayInfo ->
            val isSelected = selectedDay == dayInfo.day
            DayItem(
                dayInfo = dayInfo,
                isSelected = isSelected,
                onDaySelected = onDaySelected
            )
        }
    }
}

@Composable
fun DayItem(
    dayInfo: DayInfo,
    isSelected: Boolean,
    onDaySelected: (Int) -> Unit
) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .clip(CircleShape)
            .size(height = 64.dp, width = 48.dp)
            .border(
                width = 2.dp, // Border width
                color = (if (isSelected) Color.Transparent else Color.Gray), // Border color
                shape = CircleShape // Optional: You can define the shape of the border
            )
            .background(if (isSelected) GreenMain else Color.Transparent)
            .clickable { onDaySelected(dayInfo.day) },
        contentAlignment = Alignment.Center



    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = dayInfo.day.toString(),
                color = (if (isSelected) Color.White else Color.Gray),
                fontSize = 14.sp,


            )
            Text(
                text = dayInfo.dayName,
                color = (if (isSelected) Color.White else Color.Gray),
                fontSize = 14.sp,
            )
        }
    }
}

data class DayInfo(val day: Int, val dayName: String)

@Composable
fun MealPlanItem(mealPlan: MealPlan, recipe: Recipe) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .size(120.dp)
            .padding(vertical = 8.dp),
    ) {
        Row {
            Column(
                modifier = Modifier
                    .weight(0.5f)
                    .padding(8.dp)
            ) {
                Text(text = "Meal Type: ${mealPlan.mealType ?: "Unknown"}")
                Text(text = "Date: ${mealPlan.date}")
            }

            Image(
                painter = when {
                    // Check if imagePath is a valid resource ID (integer)
                    recipe.imagePath?.toIntOrNull() != null -> {
                        painterResource(id = recipe.imagePath.toInt())
                    }
                    // Check if imagePath is a valid URI
                    recipe.imagePath != null -> {
                        rememberAsyncImagePainter(
                            model = Uri.parse(recipe.imagePath),
                            error = painterResource(id = R.drawable.chocolate_chip_cookies) // Set error image if loading fails
                        )
                    }
                    // Fallback to a default image if imagePath is null
                    else -> {
                        painterResource(id = R.drawable.chocolate_chip_cookies)
                    }
                },
                contentDescription = "Recipe Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.5f)
            )
        }
    }
}

