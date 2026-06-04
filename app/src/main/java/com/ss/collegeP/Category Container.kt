package com.ss.collegeP

import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateInt
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

enum class CategoryUIState{
    LIST,
    RECIPES
}

@Composable
fun CategoryContainer(
    mainViewModel : MainViewModel,
    isDark : Boolean,
    navController : NavHostController,
    currentRoute : String?,
    visibleCategory : String?,
    onCategoryChange : (String?) -> Unit,
    onRecipeScreenVisible : (Boolean) -> Unit,
    onAnimationChanged : (Boolean) -> Unit
){
    // rememberSaveabale keeps the state after process death

    var targetCategory by rememberSaveable { mutableStateOf(visibleCategory) }

    val uiState = if(targetCategory == null) CategoryUIState.LIST else CategoryUIState.RECIPES

    val transition = updateTransition(
        targetState = uiState,
        label = "category_transition"
    )

    val screenWidth = with(LocalDensity.current){
        LocalConfiguration.current.screenWidthDp.dp.roundToPx()
    }

    val listOffset by transition.animateInt(
        transitionSpec = {
            tween(
                durationMillis = 320,
                easing = FastOutSlowInEasing
            )
        },
        label = "list_offset"
    ){ state ->
        when(state){
            CategoryUIState.LIST -> 0
            CategoryUIState.RECIPES -> -screenWidth
        }
    }

    val recipeOffset by transition.animateInt(
        transitionSpec = {
            tween(
                durationMillis = 320,
                easing = FastOutSlowInEasing
            )
        },
        label = "recipe_offset"
    ){ state ->
        when(state){
            CategoryUIState.LIST -> screenWidth
            CategoryUIState.RECIPES -> 0
        }
    }

    val isAnimating = transition.currentState != transition.targetState

//    Avoid clicks on Bottom bar
    LaunchedEffect(isAnimating){
        onAnimationChanged(isAnimating)

        if(!isAnimating && targetCategory == null){
            onCategoryChange(null)
        }
    }

    LaunchedEffect(visibleCategory){
        onRecipeScreenVisible(visibleCategory != null)
    }

//    Category List
    Box(
        modifier = Modifier
            .fillMaxSize()
            .offset { IntOffset(listOffset, 0) }
    ){
        CategoryScreen(
            viewModel = mainViewModel,
            navigateToCategoryRecipes = { category ->
                if(!isAnimating){
//                    Locks bottom bar instantly
                    onAnimationChanged(true)

                    targetCategory = category
                    onCategoryChange(category)
                }
            }
        )
    }

//    Category Recipes
    visibleCategory?.let { category ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset { IntOffset(recipeOffset,0) }
        ){
            CategoryRecipeScreen(
                category = category,
                isDark = isDark ,
                viewModel = mainViewModel,
                navigateToDetail = {
                    if(!isAnimating){
                        mainViewModel.isFromCategory.value = true
                        mainViewModel.selectRecipe(it)
                        navController.navigate("detailscreen")
                    }
                },
                navigateBack = {
                    if(!isAnimating){
//                        lock bottom bar instantly
                        onAnimationChanged(true)

                        targetCategory = null
                    }
                }
            )
        }
    }

//    Block system back button while animating
    BackHandler(enabled = isAnimating){ }

    BackHandler(
        enabled =
            visibleCategory != null
                    && !isAnimating
                    && currentRoute == BottomNavItem.Categories.route
    ){
        onAnimationChanged(true)
        targetCategory = null
    }
}