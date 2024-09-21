package com.example.wasfaty.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wasfaty.viewmodel.HomeScreenViewModel


@Composable
fun BookmarkScreen(viewModel: HomeScreenViewModel,onRecipeClick: (Int) -> Unit){
    val bookmarkedRecipes by viewModel.bookmarkedRecipes.observeAsState(emptyList())


    Column(
       modifier= Modifier
           .padding(horizontal = 12.dp, vertical = 16.dp)
           .verticalScroll(rememberScrollState())
           .fillMaxWidth(),
    horizontalAlignment= Alignment.CenterHorizontally,
   ) {
       Text(text="BookMarked Recipes",fontSize = 16.sp, color = Color.White, fontWeight = FontWeight.Bold)
       Spacer(modifier = Modifier.height(16.dp))
       bookmarkedRecipes.forEach { recipe ->
           RecipeCard(
               recipe = recipe,
               onClick = {  onRecipeClick(recipe.id)}, // Pass recipe ,
               viewModel=viewModel,
               )
       }
   }
}
