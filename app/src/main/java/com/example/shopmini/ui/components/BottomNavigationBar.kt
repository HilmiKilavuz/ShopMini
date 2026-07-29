package com.example.shopmini.ui.components

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.shopmini.ui.navigation.BottomNavItem

//Anasayfa altındaki butonlar
@Composable
fun BottomNavigationBar(
    navController: NavController
) {

    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Cart,
        BottomNavItem.Favorites,
        BottomNavItem.Profile
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination


    NavigationBar() {
        items.forEach {
            NavigationBarItem(
                icon = { Icon(imageVector = it.icon, contentDescription = it.title) },
                label = { Text(text = it.title) },
                selected = currentDestination?.route == it.route,
                onClick = {
                    navController.navigate(it.route)
                }

            )

        }
    }


}