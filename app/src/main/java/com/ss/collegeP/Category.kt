package com.ss.collegeP

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recipe(
    val id: Int,
    val name: String, // title from spoonacular api food site
    val image: String,
    val ingredients: List<String>,
    val instructions: List<String>,
    val mealType: List<String>,
    val readyInMinutes: Int,
    val isVeg: Boolean,
    var isFavorite: Boolean = false,

    val calories : Int = 0,
    val protein : Int = 0,
    val carbs : Int = 0,
    val fat : Int = 0,
) : Parcelable


// For Category screen dummy data
data class Category(
    val name: String,
    val image: Int
)