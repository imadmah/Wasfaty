package com.example.wasfaty.view

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.wasfaty.R
import com.example.wasfaty.ui.theme.GreenMain
import com.example.wasfaty.ui.theme.SearchBar
import com.example.wasfaty.view.navBar.colorButtons


import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.TextFieldColors
import com.example.wasfaty.models.entity.Recipe

@Preview
@Composable fun ThisScreenPreview(){
    AddRecipeScreen { Recipe ->
    }
}


@Composable
fun AddRecipeScreen(onSaveClick: (Recipe) -> Unit) {
    val context = LocalContext.current

    // State variables for form fields
    var titleState by remember { mutableStateOf("") }
    var timeState by remember { mutableStateOf("") }
    var difficultyState by remember { mutableStateOf("Easy") }
    var ingredientsState by remember { mutableStateOf("") }
    var stepsState by remember { mutableStateOf("") }

    // State for selected image
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    // State for field error messages
    var titleError by remember { mutableStateOf(false) }
    var timeError by remember { mutableStateOf(false) }
    var ingredientsError by remember { mutableStateOf(false) }
    var stepsError by remember { mutableStateOf(false) }
    var imageError by remember { mutableStateOf(false) }

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            try {
                // Persist URI permission
                context.contentResolver.takePersistableUriPermission(
                    uri,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION
                )
            } catch (e: SecurityException) {
                // Handle exception if permission could not be persisted
                Toast.makeText(context, "Failed to persist URI permission", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        // Image Picker Section
        ImagePickerSection(
            selectedImageUri = selectedImageUri,
            onSelectImageClick = { imagePickerLauncher.launch("image/*") }
        )

        // Show error if image is not selected
        if (imageError) {
            Text(
                text = "Image is required",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Recipe Title
        OutlinedTextField(
            value = titleState,
            onValueChange = {
                titleState = it
                titleError = it.isEmpty()
            },
            label = { Text("Recipe Title") },
            placeholder = { Text("Cheese Cake") },
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors(),
            isError = titleError
        )

        // Show error if title is empty
        if (titleError) {
            Text(
                text = "Title is required",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Cooking Time
        OutlinedTextField(
            value = timeState,
            onValueChange = {
                timeState = it
                timeError = it.isEmpty()
            },
            label = { Text("Cooking Time") },
            placeholder = { Text("e.g., 45 min") },
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors(),
            isError = timeError
        )

        // Show error if cooking time is empty
        if (timeError) {
            Text(
                text = "Cooking time is required",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Difficulty Dropdown
        DifficultyDropdown(
            selectedDifficulty = difficultyState,
            onDifficultyChange = { difficultyState = it }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Ingredients
        OutlinedTextField(
            value = ingredientsState,
            onValueChange = {
                ingredientsState = it
                ingredientsError = it.isEmpty()
            },
            label = { Text("Ingredients") },
            placeholder = { Text("e.g., 2 cups flour, 1 tsp salt") },
            maxLines = 5,
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors(),
            isError = ingredientsError
        )

        // Show error if ingredients are empty
        if (ingredientsError) {
            Text(
                text = "Ingredients are required",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Cooking Steps
        OutlinedTextField(
            value = stepsState,
            onValueChange = {
                stepsState = it
                stepsError = it.isEmpty()
            },
            label = { Text("Cooking Steps") },
            placeholder = { Text("e.g., 1. Mix ingredients...") },
            maxLines = 8,
            modifier = Modifier.fillMaxWidth(),
            colors = textFieldColors(),
            isError = stepsError
        )

        // Show error if cooking steps are empty
        if (stepsError) {
            Text(
                text = "Cooking steps are required",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Save Button
        Button(
            onClick = {
                // Validate all fields
                val isTitleValid = titleState.isNotEmpty()
                val isTimeValid = timeState.isNotEmpty()
                val isIngredientsValid = ingredientsState.isNotEmpty()
                val isStepsValid = stepsState.isNotEmpty()
                val isImageValid = selectedImageUri != null

                // Update error states
                titleError = !isTitleValid
                timeError = !isTimeValid
                ingredientsError = !isIngredientsValid
                stepsError = !isStepsValid
                imageError = !isImageValid

                if (isTitleValid && isTimeValid && isIngredientsValid && isStepsValid && isImageValid) {
                    // Create the Recipe object and pass it to the onSaveClick lambda
                    val newRecipe = Recipe(
                        title = titleState,
                        time = timeState,
                        difficulty = difficultyState,
                        ingredients = ingredientsState,
                        cookingSteps = stepsState,
                        imagePath = selectedImageUri?.toString()
                    )
                    onSaveClick(newRecipe)

                    // Show confirmation Toast
                    Toast.makeText(context, "Recipe saved successfully", Toast.LENGTH_SHORT).show()

                    // Clear all fields and the selected image
                    titleState = ""
                    timeState = ""
                    difficultyState = "Easy"
                    ingredientsState = ""
                    stepsState = ""
                    selectedImageUri = null
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = GreenMain,
                contentColor = Color.White
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save Recipe")
        }
    }
}



@Composable
fun textFieldColors(): TextFieldColors {
    return OutlinedTextFieldDefaults.colors(
        focusedBorderColor = GreenMain, // Border color when focused
        unfocusedBorderColor = Color.White, // Border color when unfocused
        focusedLabelColor = GreenMain, // Label color when focused
        unfocusedLabelColor = Color.White // Label color when unfocused
    )
}

@Composable
fun ImagePickerSection(selectedImageUri: Uri?, onSelectImageClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        if (selectedImageUri != null) {
            // Display the selected image using Coil
            Image(
                painter = // Replace with your placeholder drawable
                rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current).data(data = selectedImageUri)
                        .apply(block = fun ImageRequest.Builder.() {
                            crossfade(true)
                            error(R.drawable.placeholder_image) // Replace with your placeholder drawable
                        }).build()
                ),
                contentDescription = "Selected Image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            // Placeholder text
            Text(
                text = "Tap to select an image",
                color = Color.White
            )
        }
        // Image selection overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
                .clickable { onSelectImageClick() }
        )
    }
}

// Difficulty Dropdown Composable remains the same as previously discussed.

@Composable
fun DifficultyDropdown(
    selectedDifficulty: String,
    onDifficultyChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val difficultyLevels = listOf("Easy", "Medium", "Hard")

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = GreenMain, // Border color
                shape = RoundedCornerShape(4.dp)
            )
            .clickable { expanded = true }
            .padding(16.dp)
    ) {
        Text(
            text = selectedDifficulty,
            textAlign = TextAlign.Start,
            color = Color.White
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            difficultyLevels.forEach { difficulty ->
                DropdownMenuItem(
                    
                    text = { Text(text = difficulty) },
                    onClick = {
                        onDifficultyChange(difficulty)
                        expanded = false
                    }

                )
            }
        }
    }
}


