package com.ss.collegeP

import StorageForRecipes.RecipeRepository
import StorageForRecipes.RecipeViewModel
import StorageForRecipes.RecipeViewModelFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage


@Composable
fun HomeScreen(
//    navController : NavHostController,
    isDark : Boolean,
    modifier : Modifier = Modifier,
    navigateToDetailScreen: (Recipe) -> Unit,
    mainViewModel : MainViewModel,
    viewState : MainViewModel.RecipeState,
    recipeViewModel : RecipeViewModel
){

    Column(modifier = Modifier.fillMaxSize()){

//        Top Bar
        Text(
            text = "Recipe Book",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(16.dp)
                .align(alignment = Alignment.CenterHorizontally)
        )

//        Search Bar
        SearchBar(
            query = mainViewModel.searchQuery.value,
            onQueryChange = { mainViewModel.searchRecipes(query = it) },
            onClear = { mainViewModel.searchRecipes("") },
            isDark = isDark,
            placeholder = "Search recipes...",
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))

        when{
            viewState.loading -> {
                Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    CircularProgressIndicator(
                        color = Color(0xFF4CAF50)
                    )
                }
            }
            viewState.error != null ->{
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    Text(text = "Error loading recipes")
                }
            }
            else ->{

                if(viewState.filteredList.isEmpty()){
                    // No result UI
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ){
                        Text(
                            text = "No recipes found",
                            fontSize = 18.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }else{
                    categoryScreen(
                        recipes = viewState.filteredList,
                        navigateToDetailScreen = navigateToDetailScreen,
                        mainViewModel = mainViewModel,
                        recipeViewModel = recipeViewModel
                    )
                }
            }
        }
    }

}

@Composable
fun categoryScreen(
    recipes : List<Recipe>,
    navigateToDetailScreen: (Recipe) -> Unit,
    mainViewModel : MainViewModel,
    recipeViewModel : RecipeViewModel
){
    LazyVerticalGrid(GridCells.Fixed(2), modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)){
        items(recipes){
                recipe->
            categoryItem(
                recipe = recipe,
                navigateToDetailScreen = navigateToDetailScreen,
                mainViewModel = mainViewModel,
                recipeViewModel = recipeViewModel
            )
        }
    }
}


@Composable
fun categoryItem(
    recipe : Recipe,
    navigateToDetailScreen : (Recipe) -> Unit,
    mainViewModel : MainViewModel,
    recipeViewModel : RecipeViewModel
){

    Box(
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable{ navigateToDetailScreen(recipe) }
    ) {

//        Image
        AsyncImage(
            model = recipe.image,
            contentDescription = recipe.name,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f),
            contentScale = ContentScale.Crop,
            onError = {
                mainViewModel.removeBrokenRecipe(id = recipe.id)
            }
        )

//        Gradient
        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.7f)
                        )
                    )
                )
        )

//        Text
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp)
        ){
            Text(
                text = recipe.name,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            /**
            Text(
                text = "⏱ ${recipe.readyInMinutes} min",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 12.sp
            )
             **/
        }

//        Favourite
        IconButton(
            onClick = {
                recipeViewModel.toggleFavorite(recipe = recipe)
            },
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = if(recipe.isFavorite)
                    Icons.Default.Favorite
                else
                    Icons.Default.FavoriteBorder,
                contentDescription = null,
                tint = if(recipe.isFavorite) Color(0xFF4CAF50) else Color.Black
            )
        }
    }
}
