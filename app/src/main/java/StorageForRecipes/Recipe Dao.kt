package StorageForRecipes

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavourite(recipe : RecipeEntity)

    @Delete
    suspend fun deleteFavourite(recipe : RecipeEntity)

    @Query("SELECT*FROM favourites ORDER BY favouriteTimeStamp DESC")
    fun getAllRecipes() : Flow<List<RecipeEntity>>

    @Query("SELECT*FROM favourites WHERE isFavorite = 1 ORDER BY favouriteTimeStamp DESC")
    fun getFavourites() : Flow<List<RecipeEntity>>

    @Query("SELECT*FROM favourites WHERE isVeg = 1 ORDER By favouriteTimeStamp DESC")
    fun getVegFavourites() : Flow<List<RecipeEntity>>

    @Query("SELECT*FROM favourites WHERE name LIKE '%' || :query || '%' ORDER BY favouriteTimeStamp DESC")
    fun searchFavourites(query : String) : Flow<List<RecipeEntity>>

    @Query("UPDATE favourites SET isFavorite = :isFav WHERE id = :id")
    suspend fun updateFavouriteStatus(id : Int, isFav : Boolean)

    @Query("DELETE FROM favourites WHERE id = :id")
    suspend fun deleteById(id : Int)

}