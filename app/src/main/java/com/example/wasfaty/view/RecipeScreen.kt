package com.example.wasfaty.view

import android.net.Uri
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
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wasfaty.models.entity.Categorie
import com.example.wasfaty.ui.theme.SearchBar
import com.example.wasfaty.ui.theme.TextColor
import com.example.wasfaty.viewmodel.HomeScreenViewModel
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberAsyncImagePainter
import com.example.wasfaty.ui.theme.GreenMain


@Preview
@Composable
fun preview(){
    val recipe = Recipe(
        title = "Salmon Schnitzel",
        time = "45",
        difficulty = "Easy",
        ingredients = "2 eggs\n2 tbs mild mustard\n2 tbs each tarragon and dill, finely chopped\n1/4 cup (75g) plain flour\n2 cups (100g) panko breadcrumbs\n4 x 200g skinless salmon fillets\nSunflower oil, to shallow-fry\n1 cup Greek yoghurt\n1 garlic clove, crushed\nZest and juice of ½ lemon, plus extra wedges to serve\nHerb salad or green salad leaves, to serve",
        imagePath = "res://salmon_schnitzel", // Example image resource,
        cookingSteps = "dsfsd",
        category = "Fish",
    )

    RecipeScreen(recipe  )
}

@Composable
fun RecipeScreen(
    recipe: Recipe,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.White)
    ) {
        // Recipe Image Section

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
            modifier = Modifier.fillMaxSize().height(250.dp)
        )



        // Recipe Title Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFEFEFEF))
                .padding(16.dp)
        ) {
            Text(
                text = recipe.title,
                color = GreenMain
            )

            Text(
                text = recipe.cookingSteps ?: "No description provided.",
             
                color = Color.Gray,
                modifier = Modifier.padding(top = 4.dp)
            )

            // Recipe Info (Time and Difficulty)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AddCircle,
                    contentDescription = "Time",
                    tint = Color.Gray
                )
                Text(
                    text = "${recipe.time} min",
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 4.dp)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Difficulty",
                    tint = Color.Gray
                )
                Text(
                    text = recipe.difficulty,
                    color = Color.Gray,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }

        // Ingredients Section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Ingredients",
                color = Color.Black
            )

            recipe.ingredients.split("\n").forEach { ingredient ->
                Text(
                    text = "• $ingredient",
                    color = Color.Black,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
    }
}
