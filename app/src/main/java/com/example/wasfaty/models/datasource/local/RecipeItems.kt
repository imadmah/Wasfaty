package com.example.wasfaty.models.datasource.local

import com.example.wasfaty.R
import com.example.wasfaty.models.entity.Recipe
import com.example.wasfaty.view.navBar.Item

val InitialRecipes = listOf(

       Recipe(
            title = "Classic Lasagna",
    time = "90min",
    difficulty = "Medium",
    ingredients = "12 lasagna noodles, 4 cups ricotta cheese, 3 cups shredded mozzarella, 2 cups grated Parmesan, 2 eggs, 1 lb ground beef, 1 jar marinara sauce, 1 tsp salt, 1 tsp pepper",
    cookingSteps = """
        1. Preheat oven to 375°F (190°C).
        2. Cook the lasagna noodles according to package instructions.
        3. In a large skillet, brown the ground beef and season with salt and pepper. Add marinara sauce and let simmer.
        4. In a mixing bowl, combine ricotta cheese, eggs, and half of the Parmesan.
        5. Layer noodles, cheese mixture, meat sauce, and mozzarella in a baking dish. Repeat layers.
        6. Top with remaining mozzarella and Parmesan.
        7. Bake for 45 minutes, uncovered, until bubbly and golden.
        8. Let it cool for 10 minutes before serving.
    """.trimIndent(),
    imagePath = R.drawable.classic_lasagna.toString()
) ,


Recipe(
    title = "Chicken Stir-Fry",
    time = "25min",
    difficulty = "Easy",
    ingredients = "2 boneless chicken breasts, 1 red bell pepper, 1 green bell pepper, 1 onion, 2 cups broccoli florets, 1 tbsp soy sauce, 1 tbsp hoisin sauce, 2 cloves garlic, 1 tbsp sesame oil, 1 tsp cornstarch",
    cookingSteps = """
        1. Slice the chicken into thin strips and season with salt and pepper.
        2. Heat sesame oil in a large skillet over medium-high heat.
        3. Add chicken and cook until browned.
        4. Add minced garlic and stir-fry for 1 minute.
        5. Add sliced peppers, onion, and broccoli florets. Stir-fry until vegetables are tender-crisp.
        6. Mix soy sauce, hoisin sauce, and cornstarch with a bit of water. Pour over the stir-fry.
        7. Cook until sauce thickens, stirring constantly.
        8. Serve hot over rice or noodles.
    """.trimIndent(),
    imagePath = R.drawable.quick_chicken_stir_fry.toString()
),

 Recipe(
    title = "Chocolate Chip Cookies",
    time = "45min",
    difficulty = "Easy",
    ingredients = "2 1/4 cups all-purpose flour, 1 tsp baking soda, 1/2 tsp salt, 1 cup unsalted butter, 3/4 cup white sugar, 3/4 cup brown sugar, 1 tsp vanilla extract, 2 large eggs, 2 cups chocolate chips",
    cookingSteps = """
        1. Preheat oven to 350°F (175°C).
        2. In a bowl, whisk together flour, baking soda, and salt.
        3. In another bowl, cream together butter, white sugar, and brown sugar until fluffy.
        4. Beat in vanilla and eggs, one at a time.
        5. Gradually add the flour mixture to the butter mixture. Stir in the chocolate chips.
        6. Drop spoonfuls of dough onto a baking sheet.
        7. Bake for 10-12 minutes, or until golden brown.
        8. Let cookies cool on a wire rack.
    """.trimIndent(),
    imagePath = R.drawable.chocolate_chip_cookies.toString()
),

Recipe(
    title = "Beef Tacos",
    time = "30min",
    difficulty = "Easy",
    ingredients = "1 lb ground beef, 1 packet taco seasoning, 8 taco shells, 1 cup shredded lettuce, 1 cup diced tomatoes, 1 cup shredded cheddar cheese, 1/2 cup sour cream, 1/4 cup salsa",
    cookingSteps = """
        1. In a skillet, cook the ground beef over medium heat until fully browned. Drain excess fat.
        2. Add taco seasoning and 1/4 cup water. Simmer for 5 minutes.
        3. Warm the taco shells in the oven or microwave.
        4. Fill each shell with seasoned beef, lettuce, tomatoes, and cheddar cheese.
        5. Top with sour cream and salsa as desired.
        6. Serve immediately with lime wedges.
    """.trimIndent(),
    imagePath = R.drawable.ground_beef_tacos.toString()
),

Recipe(
    title = "Vegetable Curry",
    time = "40min",
    difficulty = "Medium",
    ingredients = "1 onion, 2 cloves garlic, 1 tbsp ginger, 2 tbsp curry powder, 1 can coconut milk, 2 cups vegetable broth, 1 carrot, 1 potato, 1 zucchini, 1 cup cauliflower, 1 cup chickpeas, salt and pepper to taste",
    cookingSteps = """
        1. Heat oil in a large pot over medium heat. Sauté chopped onion, minced garlic, and grated ginger.
        2. Stir in curry powder and cook until fragrant.
        3. Add coconut milk and vegetable broth. Bring to a simmer.
        4. Add diced carrots, potatoes, zucchini, and cauliflower. Simmer until vegetables are tender.
        5. Stir in chickpeas and cook for an additional 5 minutes.
        6. Season with salt and pepper.
        7. Serve hot with rice or naan bread.
    """.trimIndent(),
    imagePath = R.drawable.vegetable_curry.toString()
),

)