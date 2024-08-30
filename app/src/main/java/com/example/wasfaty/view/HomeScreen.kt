package com.example.wasfaty.view

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wasfaty.R
import com.example.wasfaty.models.entity.Categorie
import com.example.wasfaty.models.entity.Recipe
import com.example.wasfaty.ui.theme.SearchBar
import com.example.wasfaty.ui.theme.TextColor
import com.example.wasfaty.viewmodel.HomeScreenViewModel
import androidx.compose.runtime.livedata.observeAsState
import coil.compose.rememberAsyncImagePainter
import com.example.wasfaty.ui.theme.GreenMain


@Composable
fun HomeScreen(viewModel: HomeScreenViewModel?, onRecipeClick: (Int) -> Unit) {
    val newestRecipes by viewModel?.newest5Recipes!!.observeAsState(emptyList())
    val allRecipes by viewModel!!.allRecipes.observeAsState(emptyList())


    Column(modifier = Modifier
        .padding(horizontal = 12.dp, vertical = 8.dp)
        .fillMaxWidth()
    ) {
        Row{
            Icon(Icons.Default.Menu, contentDescription ="Menu" )
            Spacer(modifier = Modifier.weight(1f))
            Text(text ="Home",fontWeight = Bold, fontSize = 18.sp,color= TextColor)
            Spacer(modifier = Modifier.weight(1f))
            Icon(painter = painterResource(R.drawable.bell), contentDescription ="Notifications",tint= GreenMain )
        }
        Spacer(modifier = Modifier.height(16.dp))


        SearchScreen()
        Text(text = "Categories", fontWeight = Bold, fontSize = 18.sp, color = TextColor)
        Categories()

        Column(Modifier.verticalScroll(rememberScrollState())) {
            NewRecipes(recipes = newestRecipes, onRecipeClick = onRecipeClick)
            RecommendedRecipes(recipes = allRecipes, onRecipeClick = onRecipeClick)
        }
    }
}


@Composable
fun Categories() {
    val categories = listOf(
        Categorie(1, "Desserts"),
        Categorie(2, "Breakfast"),
        Categorie(3, "Main Course"),
        Categorie(4, "Salade"),
        Categorie(5, "Pizza")
    )

    LazyRow(Modifier.padding(vertical = 8.dp)) {
        items(categories) { category ->
            CategoryItem(category = category)
        }
    }
}

@Composable
fun CategoryItem(category: Categorie) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        OutlinedButton(
            onClick = { /* Handle button click */ },
            colors = ButtonDefaults.outlinedButtonColors(SearchBar),
            border = BorderStroke(0.dp, Color.Transparent),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 2.dp)
        ) {
            Row {
                Text(text = category.name, color = Color.White, fontSize = 12.sp)
                if (category.iconResId!=null) Icon(painter = painterResource(id = category.iconResId), contentDescription ="CategoryIcon" )
            }
        }
        // Optionally show an icon or other UI components here
    }
}

@Composable
fun SearchView(
    query: String,
    onQueryChanged: (String) -> Unit,
    onSearch: (String) -> Unit,
    backgroundColor: Color = SearchBar,
    contentColor: Color = SearchBar,
    placeholderColor: Color = Color.Gray,
    iconColor: Color = Color.Black
) {
    var text by remember { mutableStateOf(query) }

    TextField(
        value = text,
        onValueChange = {
            text = it
            onQueryChanged(it)
        },
        placeholder = {
            Text(
                "Search...",
                color = placeholderColor
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(2.dp),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = iconColor
            )
        },
        trailingIcon = {
            if (text.isNotEmpty()) {
                IconButton(onClick = {
                    text = ""
                    onQueryChanged("")
                }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear Icon",
                        tint = iconColor
                    )
                }
            }
        },

        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch(text)
            }
        ),
        singleLine = true,
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.colors(
            //setting the text field background when it is focused
            focusedContainerColor = backgroundColor,
            //setting the text field background when it is unfocused or initial state
            unfocusedContainerColor = backgroundColor,
            //setting the text field background when it is disabled
            disabledContainerColor =backgroundColor,

            unfocusedIndicatorColor= Color.Transparent,

            cursorColor = contentColor



        )
    )
}

@Composable
fun SearchScreen() {
    var query by remember { mutableStateOf("") }

    Column(Modifier.padding(8.dp)) {
        SearchView(
            query = query,
            onQueryChanged = { newQuery ->
                query = newQuery
            },
            onSearch = { searchQuery ->
                // Handle search logic here
                Log.d("Search", "Searching for: $searchQuery")
            },
            backgroundColor = SearchBar, // Customize the background color
            contentColor = Color.White, // Customize the text color
            placeholderColor = Color.Gray, // Customize the placeholder color
            iconColor = Color.Gray// Customize the icon color
        )
    }
}


@Composable
fun RecipeCard(
    recipe: Recipe,
    verticalStyle: Boolean = false,
    onClick: () -> Unit // Add a lambda for the click action
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 32.dp),
        modifier = Modifier
            .then(
                if (verticalStyle) Modifier
                    .width(160.dp)
                    .height(220.dp) else Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
            .padding(8.dp)
            .clickable(onClick = onClick), // Add clickable modifier
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Background Image

                    Image(
                        painter = when {
                            // Check if imagePath is a valid resource ID (integer)
                            recipe.imagePath?.toIntOrNull() != null -> {
                                Log.d("RecipeImage", "Loading image from resource ID: ${recipe.imagePath}")
                                painterResource(id = recipe.imagePath.toInt())
                            }
                            // Check if imagePath is a valid URI
                            recipe.imagePath != null -> {
                                Log.d("RecipeImage", "Loading image from URI: ${recipe.imagePath}")
                                rememberAsyncImagePainter(
                                    model = Uri.parse(recipe.imagePath),
                                    error = painterResource(id = R.drawable.chocolate_chip_cookies) // Set error image if loading fails
                                ).also {
                                    Log.d("RecipeImage", "Async image loading started")
                                }
                            }
                            // Fallback to a default image if imagePath is null
                            else -> {
                                Log.d("RecipeImage", "Image path is null, loading default image")
                                painterResource(id = R.drawable.chocolate_chip_cookies)
                            }
                        },
                        contentDescription = "Recipe Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )



            // Gradient Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x40000000))
            )

            // Overlay content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(text = recipe.title, color = Color.White, fontWeight = Bold)

                Spacer(modifier = Modifier.height(4.dp))

                // Time and difficulty
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.bell),
                            contentDescription = "Time",
                            tint = Color.Gray,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = recipe.time, color = Color.White, fontSize = 12.sp)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Face,
                            contentDescription = "Difficulty",
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = recipe.difficulty, color = Color.White, fontSize = 12.sp)
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            // Bookmark Icon at the top-right corner
            Icon(
                painter = painterResource(id = R.drawable.bookmark),
                contentDescription = "Bookmark",
                tint = Color.White,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(24.dp)
            )
        }
    }
}


@Composable
fun NewRecipes(recipes: List<Recipe>, onRecipeClick: (Int) -> Unit) {
    Text(text = "Newest", color = Color.White, fontSize = 18.sp, fontWeight = Bold)
    LazyRow {
        items(recipes) { recipe ->
            RecipeCard(
                recipe = recipe,
                onClick = { onRecipeClick(recipe.id) }, // Pass recipe ID
                verticalStyle = true
            )
        }
    }
}

@Composable
fun RecommendedRecipes(recipes: List<Recipe>, onRecipeClick: (Int) -> Unit) {
    Row(Modifier.padding(vertical = 8.dp)) {
        Text(text = "Recommended", fontSize = 16.sp, color = Color.White, fontWeight = Bold)
        Spacer(modifier = Modifier.weight(1f))
        Text(text = "See All", fontSize = 12.sp, color = Color.Gray)
    }
    Column {
        recipes.forEach { recipe ->
            RecipeCard(
                recipe = recipe,
                onClick = { onRecipeClick(recipe.id) } // Pass recipe ID
            )
        }
    }
}



