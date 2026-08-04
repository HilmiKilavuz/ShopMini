/**
 * Uygulamanın tek Activity'sidir.
 * Jetpack Compose UI'ın başlatıldığı ana ekrandır.
 */
package com.example.shopmini

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.shopmini.domain.usecase.auth.IsUserLoggedInUseCase
import com.example.shopmini.ui.components.BottomNavigationBar
import com.example.shopmini.ui.navigation.Screen
import com.example.shopmini.ui.screens.cart.CartScreen
import com.example.shopmini.ui.screens.detail.ProductDetailScreen
import com.example.shopmini.ui.screens.favorites.FavoriteScreen
import com.example.shopmini.ui.screens.home.HomeScreen
import com.example.shopmini.ui.screens.login.LoginScreen
import com.example.shopmini.ui.screens.signup.SignUpScreen
import com.example.shopmini.ui.theme.ShopMiniTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var isUserLoggedInUseCase: IsUserLoggedInUseCase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShopMiniTheme {
                val navController = rememberNavController()

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                val isAuthScreen = currentDestination?.hasRoute<Screen.Login>() == true ||
                        currentDestination?.hasRoute<Screen.SignUp>() == true
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (!isAuthScreen) {
                            BottomNavigationBar(navController = navController)
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home, // 1. HERKES ANA SAYFA İLE BAŞLAR
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // Ana Ekran (Herkes görebilir)
                        composable<Screen.Home> {
                            HomeScreen { tiklananId ->
                                navController.navigate(Screen.ProductDetailScreen(productId = tiklananId))
                            }
                        }
                        // Detay Ekranı (Herkes görebilir)
                        composable<Screen.ProductDetailScreen> {
                            ProductDetailScreen(
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                        // Sepet Ekranı
                        composable<Screen.Cart> {

                            CartScreen(
                                onBackClick = { navController.popBackStack() },
                                onItemClick = { tiklananId ->
                                    navController.navigate(Screen.ProductDetailScreen(productId = tiklananId))
                                }
                            )
                        }
                        // Favoriler Ekranı
                        composable<Screen.Favorites> {

                            FavoriteScreen(
                                onNavigateToDetail = { productId ->
                                    navController.navigate(Screen.ProductDetailScreen(productId))
                                }
                            )


                        }
                        // Profil Ekranı (Giriş zorunlu)
                        composable<Screen.Profile> {
                            if (isUserLoggedInUseCase()) {
                                // Profil ekranı içeriği buraya gelecek
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) { Text(text = "Profil Ekranı") }
                            } else {
                                LaunchedEffect(Unit) {
                                    navController.navigate(Screen.Login)
                                }
                            }
                        }
                        // Login & SignUp Rotaları
                        composable<Screen.Login> {
                            LoginScreen(
                                onNavigateToSignUp = { navController.navigate(Screen.SignUp) },
                                onLoginSuccess = {
                                    navController.popBackStack() // Geldiği ekrana geri döner
                                }
                            )
                        }
                        composable<Screen.SignUp> {
                            SignUpScreen(
                                onNavigateToLogin = { navController.popBackStack() },
                                onSignUpSuccess = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

