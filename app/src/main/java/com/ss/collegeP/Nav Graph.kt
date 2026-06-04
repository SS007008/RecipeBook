package com.ss.collegeP

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource

sealed class BottomNavItem(
    val title : String,
    val icon : Int,
    val route : String
){
    data object Home : BottomNavItem(
        title = "Home",
        icon = R.drawable.home,
        route = "home"
    )

    data object Categories : BottomNavItem(
        title = "Categories",
        icon = R.drawable.category,
        route = "categories"
    )

    data object Favourites : BottomNavItem(
        title = "Favourites",
        icon = R.drawable.favourite,
        route = "favourites"
    )
}
val items = listOf(
    BottomNavItem.Home,
    BottomNavItem.Categories,
    BottomNavItem.Favourites
)