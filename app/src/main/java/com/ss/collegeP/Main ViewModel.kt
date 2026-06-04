package com.ss.collegeP


import StorageForRecipes.RecipeEntity
import StorageForRecipes.RecipeRepository
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository : RecipeRepository
) : ViewModel() {

   private val _categoriesState = mutableStateOf(RecipeState())
    val categoriesState : State<RecipeState> = _categoriesState
    var searchQuery = mutableStateOf("")

//    Temporary
    private val _favourites = mutableStateOf<List<Recipe>>(emptyList())
    val favourites : State<List<Recipe>> = _favourites

    private val _selectedRecipe = mutableStateOf<Recipe?>(null)
    val selectedRecipe : State<Recipe?> = _selectedRecipe

    var isFromCategory = mutableStateOf(false)

    init {
        fetchCategories()
    }

    fun cleanHtml(html: String?): List<String> {
        if (html == null) return listOf("No instructions")

        return html
            .replace("<ol>", "")
            .replace("</ol>", "")
            .replace("<li>", "")
            .replace("</li>",". ")
            .replace(Regex("<.*?>"),"")// removes any leftover html tags
            .split(". ")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
    }

    private fun fetchCategories(){
        viewModelScope.launch {

            try {

                val response = spoonApi.getRandomRecipes(
                    apiKey = BuildConfig.SPOONACULAR_API_KEY,
                    number = 100,
                    includeNutrition = true
                )

                val gson = Gson()

                val entities = response.recipes.mapNotNull { spoon ->

                    val imageUrl = spoon.image

                    if(imageUrl.isBlank() || !imageUrl.startsWith("http")){
                        return@mapNotNull null
                    }

//                  Fix spoonacular gives fake 45 sometimes
                    val time = if(spoon.readyInMinutes == 0 || spoon.readyInMinutes == 45){
                        (10..60).random() // generate realistic cooking time
                    }else{
                        spoon.readyInMinutes
                    }


//                    Ingredients
                    val ingredientsList = spoon.extendedIngredients
                        ?.map { it.original }
                        ?: emptyList()

//                    Instructions
                    val instructionsList = cleanHtml(spoon.instructions)

//                    Ready time
                    spoon.readyInMinutes = time

//                    Nutrition extract

                    val calories = spoon.nutrition?.nutrients
                        ?.find{ it.name == "Calories"}?.amount?.toInt() ?: 0

                    val protein = spoon.nutrition?.nutrients
                        ?.find{ it.name == "Protein"}?.amount?.toInt() ?: 0

                    val carbs = spoon.nutrition?.nutrients
                        ?.find{ it.name == "Carbohydrates"}?.amount?.toInt() ?: 0

                    val fat = spoon.nutrition?.nutrients
                        ?.find{ it.name == "Fat"}?.amount?.toInt() ?: 0


                    RecipeEntity(
                        id = spoon.id,
                        name = spoon.title,
                        image = spoon.image,
                        ingredientsJson = gson.toJson(ingredientsList),
                        instructionsJson = gson.toJson(instructionsList),
                        readyInMinutes = spoon.readyInMinutes,
                        isVeg = spoon.vegetarian,
                        isFavorite = false,
                        calories = calories,
                        protein = protein,
                        carbs = carbs,
                        fat = fat,
                        favouriteTimeStamp = System.currentTimeMillis(),
                        dishTypesJson = gson.toJson(spoon.dishTypes ?: emptyList<String>())
                    )
                }

                entities.forEach {
                    repository.add(it)
                }

                observeRecipesFromDb()

            }catch(e:Exception){
                observeRecipesFromDb()
            }
        }
    }

    fun searchRecipes(query : String){
        searchQuery.value = query

        val cleanQuery = query.trim()

        val filtered = if(cleanQuery.isEmpty()){
            if(query.isEmpty()){
                _categoriesState.value.list
            }else{
                emptyList()
            }
        }else{
            _categoriesState.value.list.filter {
                it.name.contains(query, ignoreCase = true)
            }
        }

        _categoriesState.value = _categoriesState.value.copy(
            filteredList = filtered
        )
    }

    private fun observeRecipesFromDb(){
        viewModelScope.launch {

            repository.allRecipes.collect { entityList ->

                val gson = Gson()

                val recipes = entityList.map { entity ->

                    val ing : List<String> = gson.fromJson(
                        entity.ingredientsJson, Array<String>::class.java
                    ).toList()

                    val ins : List<String> = gson.fromJson(
                        entity.instructionsJson, Array<String>::class.java
                    ).toList()

                    val dishTypes : List<String> = gson.fromJson(
                        entity.dishTypesJson, Array<String>::class.java
                    ).toList()

                    Recipe(
                        id = entity.id,
                        name = entity.name,
                        image = entity.image,
                        ingredients = ing,
                        instructions = ins,
                        mealType = dishTypes,
                        readyInMinutes = entity.readyInMinutes,
                        isVeg = entity.isVeg,
                        isFavorite = entity.isFavorite,
                        calories = entity.calories,
                        protein = entity.protein,
                        carbs = entity.carbs,
                        fat = entity.fat
                    )
                }

                // For search update not to affect default list on favourites
                val query = searchQuery.value.trim()

                val filtered = if(query.isEmpty()){
                    recipes
                }else{
                    recipes.filter {
                        it.name.contains(query, ignoreCase = true)
                    }
                }

                _categoriesState.value = _categoriesState.value.copy(
                    loading = false,
                    list = recipes,
                    filteredList = filtered
                )
            }
        }
    }

    fun removeBrokenRecipe(id : Int){
        viewModelScope.launch {
            repository.deleteById(id = id)
        }
    }

    fun getAllCategories() : List<Category>{
        val recipes = _categoriesState.value.list

        val categoryNames = recipes
            .flatMap { it.mealType } // extracts list from 'it' and merges it into one
            .map { it.lowercase().trim() }
            .distinct()// removes duplicates

        return categoryNames.map { name ->
            Category(
                name = name.replaceFirstChar { it.uppercase() },
                image = getCategoryImage(name)
            )
        }.sortedBy { it.name } // sorts by alphabets
    }

    fun getCategoryImage(name: String): Int {
        return when (name.lowercase()) {

            "breakfast" -> R.drawable.cat_breakfast
            "lunch" -> R.drawable.cat_lunch
            "dinner" -> R.drawable.cat_dinner
            "dessert" -> R.drawable.cat_dessert
            "snack" -> R.drawable.cat_snack

            "salad" -> R.drawable.cat_salad
            "soup" -> R.drawable.cat_soup
            "bread" -> R.drawable.cat_bread

            "beverage" -> R.drawable.cat_beverage
            "drink" -> R.drawable.cat_drink

            "sauce" -> R.drawable.cat_sauce
            "dip" -> R.drawable.cat_dip
            "spread" -> R.drawable.cat_spread
            "condiment" -> R.drawable.cat_condiment

            "main course" -> R.drawable.cat_main_course
            "main dish" -> R.drawable.cat_main_dish
            "side dish" -> R.drawable.cat_side_dish

            "appetizer" -> R.drawable.cat_appetizer
            "fingerfood" -> R.drawable.cat_fingerfood
            "starter" -> R.drawable.cat_starter
            "antipasti" -> R.drawable.cat_antipasti
            "antipasto" -> R.drawable.cat_antipasto
            "hor d'oeuvre" -> R.drawable.cat_hor_doeuvre

            "brunch" -> R.drawable.cat_brunch
            "morning meal" -> R.drawable.cat_morning_meal

            else -> R.drawable.cat_main_course
        }
    }

    fun selectRecipe(recipe : Recipe){
        _selectedRecipe.value = recipe
    }

    data class RecipeState(
        val loading : Boolean = true,
        val error : String? = null,
        val list : List<Recipe> = emptyList(),
        val filteredList : List<Recipe> = emptyList()
    )
}