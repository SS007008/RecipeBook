package com.ss.collegeP

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage


@Composable
fun DetailScreen(
    recipe : Recipe,
    isFromCategory : Boolean
){

    var showIngredients by remember { mutableStateOf(true) }

    val calories = recipe.calories
    val protein = recipe.protein
    val carbs = recipe.carbs
    val fat = recipe.fat

    Box(
        modifier = Modifier.fillMaxSize()
    ){

//        Top Image
        AsyncImage(
            model = recipe.image,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp),
            contentScale = ContentScale.Crop
        )

//        Bottom Card

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 280.dp)
                .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(20.dp)
        ){
            Text(
                text = recipe.name,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

//            MealTpe - Single Text with two styles
            val mealTypeValue = if(isFromCategory){
                if(recipe.isVeg) "Veg" else "Non-Veg"
            }else{
                recipe.mealType.joinToString(", ")
            }

            Text(
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFF4CAF50),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    ){
                        append("MealType: ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    ){
                        append(mealTypeValue)
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(2.dp))

//            Ready in - same approach as meal type
            Text(
                text = buildAnnotatedString{
                    withStyle(
                        style = SpanStyle(
                            color = Color(0xFF4CAF50),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    ){
                        append("Ready in: ")
                    }
                    withStyle(
                        style = SpanStyle(
                            color = Color.Gray,
                            fontSize = 14.sp
                        )
                    ){
                        append("⏱ ${recipe.readyInMinutes} min")
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

//            Toggle buttons
            Row(
               modifier = Modifier
                   .fillMaxWidth()
                   .clip(RoundedCornerShape(50))
                   .background(Color.Gray.copy(alpha = 0.2f))
                   .padding(4.dp)
            ){
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(50))
                        .background(
                            if(showIngredients) Color(0xFF4CAF50)
                            else Color.Transparent
                        )
                        .clickable { showIngredients = true }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "Ingredients",
                        color = if(showIngredients) Color.White
                        else Color.Gray
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(50))
                        .background(
                            if(!showIngredients) Color(0xFF4CAF50)
                            else Color.Transparent
                        )
                        .clickable{ showIngredients = false }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "Instructions",
                        color = if(!showIngredients) Color.White
                        else Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

//            Content
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ){
                if(showIngredients){
                    recipe.ingredients.forEach {
                        Text(
                            text = "• $it",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }else{
                    recipe.instructions.forEach {
                        Text(
                            text = "• $it",
                            fontSize = 16.sp,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

//            Nutrition Card
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    NutritionItem("$calories","Calories")
                    NutritionItem("${protein}g", "Protein")
                    NutritionItem("${carbs}g", "Carbs")
                    NutritionItem("${fat}g", "Fat")
                }
            }
        }
    }
}

@Composable
fun NutritionItem(value : String, label : String){

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Text(
            text = label,
            fontSize = 18.sp,
            color = Color.Gray
        )
    }
}