package StorageForRecipes

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ss.collegeP.MainViewModel
import java.lang.IllegalArgumentException

class RecipeViewModelFactory(
    private val context: Context,
    private val mainViewModel: MainViewModel
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        val dao = DatabaseProvider
            .getDatabase(context)
            .favouriteDao()

        val repository = RecipeRepository(dao = dao)

        if(modelClass.isAssignableFrom(RecipeViewModel::class.java)){
            return RecipeViewModel(
                mainViewModel = mainViewModel,
                repository = repository
            ) as T
        }

        throw IllegalArgumentException("Unknown ViewModel")
    }
}