package StorageForRecipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.ss.collegeP.MainViewModel
import com.ss.collegeP. Recipe
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RecipeViewModel(
    private val repository : RecipeRepository,
    private val mainViewModel : MainViewModel
) : ViewModel(){

    private val _favouriteList = repository.favourites
    val favourites : StateFlow<List<Recipe>?> =
        _favouriteList.map { list ->
            list.map { it.toRecipe() } // created our own function as .to
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )

//    private val _categoriesState = mutableStateOf(MainViewModel.RecipeState())
    val gson = Gson()

    /**
    fun addToFavourite(recipe :  Recipe){
        viewModelScope.launch {

            val entity = RecipeEntity(
                id = recipe.id,
                name = recipe.name,
                image = recipe.image,
//                ingredients = recipe.ingredients,
//                instructions = recipe.instructions,
//                mealType = recipe.mealType,
                readyInMinutes = recipe.readyInMinutes,
                isVeg = recipe.isVeg,
                isFavorite = recipe.isFavorite,
                calories = recipe.calories,
                protein = recipe.protein,
                carbs = recipe.carbs,
                fat = recipe.fat,
                ingredientsJson = gson.toJson(recipe.ingredients),
                instructionsJson = gson.toJson(recipe.instructions),
                favouriteTimeStamp = System.currentTimeMillis()
            )

            repository.add(recipe = entity)
        }
    }



    fun removeFromFavourite(recipe : Recipe){
        viewModelScope.launch {

            val entity = RecipeEntity(
                id = recipe.id,
                name = recipe.name,
                image = recipe.image,
//                ingredients = recipe.ingredients,
//                instructions = recipe.instructions,
//                mealType = recipe.mealType,
                readyInMinutes = recipe.readyInMinutes,
                isVeg = recipe.isVeg,
                isFavorite = recipe.isFavorite,
                calories = recipe.calories,
                protein = recipe.protein,
                carbs = recipe.carbs,
                fat = recipe.fat,
                ingredientsJson = gson.toJson(recipe.ingredients),
                instructionsJson = gson.toJson(recipe.instructions),
                favouriteTimeStamp = System.currentTimeMillis()
            )

            repository.remove(recipe = entity)
        }
    }
    */

    fun toggleFavorite(recipe : Recipe){
        viewModelScope.launch {
            repository.updateFavourite(
                id = recipe.id,
                isFav = !recipe.isFavorite
            )
        }
    }

    fun RecipeEntity.toRecipe() : Recipe{

        val ingredientsList : List<String> =
            gson.fromJson(ingredientsJson, Array<String>::class.java).toList()
        val instructionsList : List<String> =
            gson.fromJson(instructionsJson, Array<String>::class.java).toList()

        return Recipe(
            id = id,
            name = name,
            image = image,
            ingredients = ingredientsList,
            instructions = instructionsList,
            mealType = if(isVeg) listOf("Vegetarian") else listOf("Non Veg"),
            readyInMinutes = readyInMinutes,
            isVeg = isVeg,
            isFavorite = isFavorite,
            calories = calories,
            protein = protein,
            carbs = carbs,
            fat = fat
        )
    }
}