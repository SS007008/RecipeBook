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
        val dishTypes : List<String> =
            gson.fromJson(dishTypesJson, Array<String>::class.java).toList()

        return Recipe(
            id = id,
            name = name,
            image = image,
            ingredients = ingredientsList,
            instructions = instructionsList,
            mealType = dishTypes,
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