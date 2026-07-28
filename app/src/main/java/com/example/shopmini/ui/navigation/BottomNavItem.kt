package com.example.shopmini.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.ImageVector


//Bottom navigation bar için navigation bileşenleri
sealed class BottomNavItem(
    val route: Any,
    val icon: ImageVector,
    val title: String,
){
    object Home: BottomNavItem(
        route = Screen.Home,
        icon = Icons.Default.Home,
        title = "Home"
    )

    object Cart: BottomNavItem(
        route = Screen.Cart,
        icon = Icons.Default.ShoppingCart,
        title = "Cart"
    )
    object Favorites: BottomNavItem(
        route = Screen.Favourites,
        icon = Icons.Default.Favorite,
        title = "Favorites"
    )
    object Profile: BottomNavItem(
        route = Screen.Profile,
        icon = Icons.Default.Person,
        title = "Profile"
    )



}

