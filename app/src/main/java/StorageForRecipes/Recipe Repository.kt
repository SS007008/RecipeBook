package StorageForRecipes

import kotlinx.coroutines.flow.Flow

class RecipeRepository(private val dao : RecipeDao){

    val allRecipes : Flow<List<RecipeEntity>> = dao.getAllRecipes()

//  Favourite screen use this
    val favourites : Flow<List<RecipeEntity>> = dao.getFavourites()

    suspend fun add(recipe : RecipeEntity){
        dao.insertFavourite(recipe = recipe)
    }

    suspend fun updateFavourite(id : Int, isFav : Boolean){
        dao.updateFavouriteStatus(id = id, isFav = isFav)
    }

    suspend fun deleteById(id : Int){
        dao.deleteById(id = id)
    }

}