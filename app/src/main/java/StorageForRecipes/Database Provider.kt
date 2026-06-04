package StorageForRecipes

import android.content.Context
import androidx.room.Room

object DatabaseProvider{

    @Volatile// Database must be only one in whole app
    private var INSTANCE  : RecipeDatabase? = null

    fun getDatabase(context: Context):RecipeDatabase{
        return INSTANCE ?: synchronized(this){
            val instance = Room.databaseBuilder(
                context.applicationContext,
                RecipeDatabase::class.java,
                "recipe_db"
            )
                .fallbackToDestructiveMigration(false)
                .build()

            INSTANCE = instance
            instance
        }
    }
}