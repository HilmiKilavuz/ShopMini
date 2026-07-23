/**
 * Uygulamanın tek Activity'sidir.
 * Jetpack Compose UI'ın başlatıldığı ana ekrandır.
 */
package com.example.shopmini

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.shopmini.ui.navigation.Screen
import com.example.shopmini.ui.screens.detail.ProductDetailScreen
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

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    // Uygulamanın navigasyon yöneticisini (şoförünü) oluşturuyoruz
                    val navController = rememberNavController()

                    // Ekranlar arası geçiş haritasını (NavHost) kuruyoruz
                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home, // Uygulama açıldığında ilk burası başlar
                        modifier = Modifier.padding(innerPadding)
                    ) {

                        // 1. Rota: Ana Ekran
                        composable<Screen.Home> {
                            HomeScreen(
                                onProductClick = { tiklananId ->
                                    navController.navigate(Screen.ProductDetailScreen(productId = tiklananId))
                                }
                            )
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
                    }

                }
            }
        }
    }
}



