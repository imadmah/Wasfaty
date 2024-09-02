package com.example.wasfaty.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.wasfaty.R


@Preview
@Composable
fun WeeklyMealPlannerScreen() {
    val daysOfWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Meal Planner",
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Calendar Layout
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(daysOfWeek) { day ->
                DayMealPlan(day)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun DayMealPlan(day: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, MaterialTheme.colorScheme.primary)
            .padding(8.dp)
    ) {
        Text(
            text = day,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Lunch
        MealSlot("Lunch", "Recipe Title for Lunch", R.drawable.recipetem)

        Spacer(modifier = Modifier.height(8.dp))

        // Dinner
        MealSlot("Dinner", "Recipe Title for Dinner", R.drawable.classic_lasagna)
    }
}

@Composable
fun MealSlot(mealType: String, recipeTitle: String, imageRes: Int) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = null,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .border(1.dp, MaterialTheme.colorScheme.onSurface, CircleShape)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Text(
                text = mealType,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = recipeTitle,
            )
        }
    }
}
