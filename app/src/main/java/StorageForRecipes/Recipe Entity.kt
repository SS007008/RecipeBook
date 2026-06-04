package StorageForRecipes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourites")
data class RecipeEntity(
    @PrimaryKey
    val id : Int,

    val name: String,
    val image: String,
    val readyInMinutes: Int,
    val isVeg: Boolean,
    var isFavorite: Boolean,

    val calories : Int,
    val protein : Int,
    val carbs : Int,
    val fat : Int,

    val ingredientsJson : String,
    val instructionsJson : String,
    val favouriteTimeStamp : Long, // Must Keep for room sorting

    val dishTypesJson : String
)