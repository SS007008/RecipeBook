package com.ss.collegeP


import StorageForRecipes.RecipeViewModel
import StorageForRecipes.RecipeViewModelFactory
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
@Composable
fun RecipeApp(isDark : Boolean, navController: NavHostController){

    val context = LocalContext.current
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    var rootRoute by rememberSaveable {
        mutableStateOf(BottomNavItem.Home.route)
    }

    var visibleCategory by rememberSaveable { mutableStateOf<String?>(null) }

    val mainViewModel : MainViewModel = viewModel(
        factory = MainViewModelFactory(context = context)
    )
    val viewState by mainViewModel.categoriesState

    val recipeViewModel : RecipeViewModel = viewModel(
        factory = RecipeViewModelFactory(
            context = context,
            mainViewModel = mainViewModel
        )
    )

    var hideBottomBar by remember { mutableStateOf(false) }

    var isCategoryAnimating by remember { mutableStateOf(false) }

    val stateHolder = rememberSaveableStateHolder()

    LaunchedEffect(currentRoute){
        if(currentRoute in items.map { it.route }){
            rootRoute = currentRoute!!
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ){

        stateHolder.SaveableStateProvider(rootRoute){
            when(rootRoute){

                BottomNavItem.Home.route ->{
                    HomeScreen(
//               navController = navController,
                        isDark = isDark,
                        navigateToDetailScreen = {
                            mainViewModel.isFromCategory.value = false
                            mainViewModel.selectRecipe(it)
                            navController.navigate("detailscreen")
                        },
                        viewState = viewState,
                        mainViewModel = mainViewModel,
                        recipeViewModel = recipeViewModel
                    )
                }

                BottomNavItem.Categories.route -> {
                    CategoryContainer(
                        mainViewModel = mainViewModel,
                        isDark = isDark,
                        navController = navController,
                        currentRoute = currentRoute,
                        visibleCategory = visibleCategory,
                        onCategoryChange = { visibleCategory = it },
                        onRecipeScreenVisible = { visible ->
//                            Keeps  bottom bar until animation finishes
                            if(!isCategoryAnimating){
                                hideBottomBar = visible
                            }
                        },
                        onAnimationChanged = { animating ->
                            isCategoryAnimating = animating }
                    )
                }

                BottomNavItem.Favourites.route -> {
                    FavouritesScreen(
                        navigateToDetailScreen = {
                            mainViewModel.isFromCategory.value = false
                            mainViewModel.selectRecipe(it)
                            navController.navigate("detailscreen")
                        },
                        recipeViewModel = recipeViewModel
                    )
                }
            }
        }

        if(currentRoute == "detailscreen"){
            Box(
             modifier = Modifier
                 .fillMaxSize()
                 .pointerInput(Unit){
                     awaitPointerEventScope{
                         while (true){
                             awaitPointerEvent()
                         }
                     }
                 }
            )
        }
    }
        NavHost(navController = navController,
            startDestination = BottomNavItem.Home.route ){

            composable(BottomNavItem.Home.route){}

            composable(BottomNavItem.Categories.route){}

            composable(BottomNavItem.Favourites.route){}

            composable(
                route = "detailscreen",
                enterTransition = {
                    fadeIn(
                        animationSpec = tween(300)
                    ) + scaleIn(
                        initialScale = 0.92f,
                        animationSpec = tween(300)
                    )
                },
                exitTransition = {
                    fadeOut(
                        animationSpec = tween(200)
                    ) + scaleOut(
                        targetScale = 0.92f,
                        animationSpec = tween(200)
                    )
                }
                ){
                val recipe = mainViewModel.selectedRecipe.value
                if(recipe != null){
                    DetailScreen(
                        recipe = recipe,
                        isFromCategory = mainViewModel.isFromCategory.value
                    )
                }
            }
        }

    if(currentRoute in items.map { it.route } && visibleCategory == null && !hideBottomBar){
        FloatingBottomBar(
            isDark = isDark,
            navController = navController,
            currentRoute = currentRoute,
            enabled = !isCategoryAnimating
        )
    }
}