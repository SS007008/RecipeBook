data class SpoonacularResponse(
    val recipes: List<SpoonRecipe>
)

data class SpoonRecipe(
    val id: Int,
    val title: String,
    val image: String,
    var readyInMinutes: Int,
    val vegetarian: Boolean,
    val instructions: String?,
    val extendedIngredients: List<Ingredient>?,
    val nutrition: Nutrition?,
    val dishTypes : List<String>?
)

data class Ingredient(
    val original: String
)

data class Nutrition(
    val nutrients: List<Nutrient>
)

data class Nutrient(
    val name: String,
    val amount: Double,
    val unit: String
)