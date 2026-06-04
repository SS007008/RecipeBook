package com.ss.collegeP

import StorageForRecipes.DatabaseProvider
import StorageForRecipes.RecipeRepository
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MainViewModelFactory(
    private val context : Context
) : ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
         val dao = DatabaseProvider
             .getDatabase(context)
             .favouriteDao()

        val repository = RecipeRepository(dao)

        if(modelClass.isAssignableFrom(MainViewModel::class.java)){
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}