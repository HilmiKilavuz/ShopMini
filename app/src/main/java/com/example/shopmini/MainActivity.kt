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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shopmini.ui.components.BottomNavigationBar
import com.example.shopmini.ui.navigation.Screen
import com.example.shopmini.ui.screens.cart.CartScreen
import com.example.shopmini.ui.screens.detail.ProductDetailScreen
import com.example.shopmini.ui.screens.favorites.FavoriteScreen
import com.example.shopmini.ui.screens.home.HomeScreen
import com.example.shopmini.ui.theme.ShopMiniTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShopMiniTheme {
                // Uygulamanın navigasyon yöneticisi
                val navController = rememberNavController()
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { BottomNavigationBar(navController = navController) }
                ) { innerPadding ->




                    // Ekranlar arası geçiş haritası
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home, // Uygulama açıldığında ilk burası başlar
                        modifier = Modifier.padding(innerPadding)
                    ) {

                        // 1. Rota: Ana Ekran
                        composable<Screen.Home> {
                            HomeScreen { tiklananId ->
                                navController.navigate(Screen.ProductDetailScreen(productId = tiklananId))
                            }
                        }

                        // 2. Rota: Detay Ekranı
                        composable<Screen.ProductDetailScreen> {
                            ProductDetailScreen(
                                onBackClick = {
                                    // Geri butonuna basıldığında bir önceki sayfaya döner
                                    navController.popBackStack()
                                }
                            )
                        }
                        // 3. Rota: Sepet Ekranı
                        composable<Screen.Cart> {
                            CartScreen(onBackClick = {
                                // Geri butonuna basıldığında bir önceki sayfaya döner
                                navController.popBackStack()
                            })
                        }
                        // 4. Rota: Favoriler Ekranı
                        composable<Screen.Favourites> {
                            FavoriteScreen(onNavigateToDetail = { productId ->
                                navController.navigate(Screen.ProductDetailScreen(productId))
                            })

                        }

                        // 5. Rota: Profil Ekranı
                        composable<Screen.Profile> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("Profil Ekranı Çok Yakında!")
                            }

                        }
                    }
                }
            }
        }
    }
}



