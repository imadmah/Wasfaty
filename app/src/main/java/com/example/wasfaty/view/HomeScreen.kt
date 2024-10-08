package com.example.wasfaty.view

import android.net.Uri
import android.widget.Toast
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
import androidx.compose.runtime.derivedStateOf
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
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberAsyncImagePainter
import com.example.wasfaty.models.datasource.local.categories
import com.example.wasfaty.ui.theme.GreenMain


@Composable
fun HomeScreen(viewModel: HomeScreenViewModel?, onRecipeClick: (Int) -> Unit) {
    val newestRecipes by viewModel?.newest5Recipes!!.observeAsState(emptyList())
    val allRecipes by viewModel!!.allRecipes.observeAsState(emptyList())
    var selectedCategory by remember { mutableStateOf<Categorie?>(null) }
    val categorizedRecipes by remember {
        derivedStateOf {
            selectedCategory?.let { category ->
                allRecipes.filter { it.category == category.name }
            } ?: allRecipes
        }
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .fillMaxWidth()
    ) {
        SearchScreen()
        Text(text = "Categories", fontWeight = Bold, fontSize = 18.sp, color = TextColor)
        Categories(selectedCategory) { category ->
            selectedCategory = if (selectedCategory == category) null else category
        }

        if (selectedCategory != null) {
            CategorizedRecipes(recipes = categorizedRecipes, onRecipeClick = onRecipeClick, viewModel)
        } else {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                NewRecipes(recipes = newestRecipes, onRecipeClick = onRecipeClick, viewModel)
                RecommendedRecipes(recipes = allRecipes, onRecipeClick = onRecipeClick, viewModel)
            }
        }
    }
}
@Composable
fun Categories(selectedCategory: Categorie?, onCategoryClick: (Categorie) -> Unit) {

    LazyRow(Modifier.padding(vertical = 8.dp)) {
        items(categories) { category ->
            CategoryItem(category = category, isSelected = selectedCategory == category) {
                onCategoryClick(category)
            }
        }
    }
}

@Composable
fun CategoryItem(category: Categorie, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal =  4.dp)
    ) {
        OutlinedButton(
            onClick = onClick,
            colors = ButtonDefaults.outlinedButtonColors(if (isSelected) Color.Gray else SearchBar),
            border = BorderStroke(0.dp, Color.Transparent),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 2.dp)
        ) {
            Row {
                Text(text = category.name, color = Color.White, fontSize = 12.sp)
                if (category.iconResId != null) Icon(painter = painterResource(id = category.iconResId), contentDescription = "CategoryIcon")
            }
        }
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
            onSearch = {
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
    onClick: () -> Unit ,
    viewModel: HomeScreenViewModel
) {
    val allRecipes by viewModel.allRecipes.observeAsState(initial = emptyList())
    // Find the current recipe in the allRecipes list
    val currentRecipe = allRecipes.find { it.id == recipe.id } ?: recipe
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
                        modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0x40000000))
            )
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
                            painter = painterResource(id = R.drawable.clock_hour_4),
                            contentDescription = "Time",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = recipe.time, color = Color.White, fontSize = 12.sp)
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(id = R.drawable.skill_level),
                            contentDescription = "Difficulty",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = recipe.difficulty, color = Color.White, fontSize = 12.sp)
                    }
                }
            }


            Icon(
                painter = painterResource(id = R.drawable.bookmark),
                contentDescription = "Bookmark",
                tint = if(currentRecipe.isBookmarked) GreenMain else Color.White,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .size(24.dp)
                    .clickable {
                        viewModel.updateBookmarkStatus(recipe.id,!currentRecipe.isBookmarked)

                    }
            )

        }
    }
}


@Composable
fun NewRecipes(recipes: List<Recipe>, onRecipeClick: (Int) -> Unit,viewModel: HomeScreenViewModel? ) {
    Text(text = "Newest", color = Color.White, fontSize = 18.sp, fontWeight = Bold)
    val recipesReversed = recipes.reversed()
    LazyRow {
        items(recipesReversed) { recipe ->
            if (viewModel != null) {
                RecipeCard(
                    recipe = recipe,
                    onClick = { onRecipeClick(recipe.id) }, // Pass recipe ID
                    verticalStyle = true,
                    viewModel= viewModel
                )
            }
        }
    }
}

@Composable
fun RecommendedRecipes(recipes: List<Recipe>, onRecipeClick: (Int) -> Unit,viewModel: HomeScreenViewModel?) {
    Row(Modifier.padding(vertical = 8.dp)) {
        Text(text = "All Recipes", fontSize = 16.sp, color = Color.White, fontWeight = Bold)
        Spacer(modifier = Modifier.weight(1f))
       // Text(text = "See All", fontSize = 12.sp, color = Color.Gray)
    }
    Column {
        recipes.forEach { recipe ->
            if (viewModel != null) {
                RecipeCard(
                    recipe = recipe,
                    onClick = { onRecipeClick(recipe.id) }, // Pass recipe ID
                    viewModel= viewModel
                )
            }
        }
    }
}

@Composable
fun CategorizedRecipes(recipes: List<Recipe>, onRecipeClick: (Int) -> Unit,viewModel: HomeScreenViewModel?){
    Column(
        modifier=Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text="Your  Recipes", fontSize = 16.sp, color = Color.White, fontWeight = Bold)
        Spacer(modifier = Modifier.height(16.dp))
        recipes.forEach { recipe ->
            RecipeCard(
                recipe = recipe,
                onClick = {  onRecipeClick(recipe.id)}, // Pass recipe ,
                viewModel= viewModel!!,
            )
        }
    }
}
