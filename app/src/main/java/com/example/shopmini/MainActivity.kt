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
import com.example.shopmini.ui.screens.checkout.CheckoutScreen
import com.example.shopmini.ui.screens.detail.ProductDetailScreen
import com.example.shopmini.ui.screens.favorites.FavoriteScreen
import com.example.shopmini.ui.screens.home.HomeScreen
import com.example.shopmini.ui.screens.login.LoginScreen
import com.example.shopmini.ui.screens.payment.OrderSuccessScreen
import com.example.shopmini.ui.screens.payment.PaymentScreen
import com.example.shopmini.ui.screens.profile.ProfileScreen
import com.example.shopmini.ui.screens.profile.address.AddEditAddressScreen
import com.example.shopmini.ui.screens.profile.address.AddressesScreen
import com.example.shopmini.ui.screens.profile.orders.OrdersScreen
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
                        currentDestination?.hasRoute<Screen.SignUp>() == true ||
                        currentDestination?.hasRoute<Screen.Addresses>() == true ||
                        currentDestination?.hasRoute<Screen.AddEditAddress>() == true ||
                        currentDestination?.hasRoute<Screen.Checkout>() == true ||
                        currentDestination?.hasRoute<Screen.Payment>() == true ||
                        currentDestination?.hasRoute<Screen.OrderSuccess>() == true
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
                                },
                                onCheckoutClick = {
                                    navController.navigate(Screen.Checkout)
                                },
                                onNavigateToLogin = {
                                    navController.navigate(Screen.Login)
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
                                ProfileScreen(
                                    onNavigateToLogin = { navController.navigate(Screen.Login) },
                                    onNavigateToAddresses = { navController.navigate(Screen.Addresses) },
                                    onNavigateToOrdersPage = {navController.navigate(Screen.Orders)})



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
                        //Giriş Yapma Ekranı
                        composable<Screen.SignUp> {
                            SignUpScreen(
                                onNavigateToLogin = { navController.popBackStack() },
                                onSignUpSuccess = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        // Adreslerim Ekranı
                        composable<Screen.Addresses> {
                            AddressesScreen(
                                onNavigateToAddAddress = { navController.navigate(Screen.AddEditAddress()) },
                                onNavigateToEditAddress = { addressId ->
                                    navController.navigate(Screen.AddEditAddress(addressId = addressId))
                                },
                                onNavigateBack = { navController.popBackStack() }
                            )

                        }
                        // Yeni Adres Ekranı
                        composable<Screen.AddEditAddress> {
                            AddEditAddressScreen(
                                onNavigateBack = { navController.popBackStack() }
                            )

                        }
                        // Checkout: Teslimat Adresi Seçim Ekranı
                        composable<Screen.Checkout> {
                            CheckoutScreen(
                                onNavigateBack = { navController.popBackStack() },
                                onNavigateToAddresses = { navController.navigate(Screen.Addresses) },
                                onNavigateToPayment = { navController.navigate(Screen.Payment) }
                            )
                        }
                        //Ödeme Ekranı
                        composable<Screen.Payment> {
                            PaymentScreen(
                                onNavigateBack = { navController.popBackStack() },
                                onNavigateToSuccess = {
                                    navController.navigate(Screen.OrderSuccess) {
                                        popUpTo(Screen.Cart) { inclusive = true }
                                    }

                                })
                        }
                        composable<Screen.OrderSuccess> {
                            OrderSuccessScreen(
                                onNavigateToHome = {
                                    navController.navigate(Screen.Home) {
                                        popUpTo(Screen.Home) {
                                            inclusive =
                                                false  // Home'u silme, sadece üstündekileri temizle

                                        }
                                    }


                                })
                        }

                        composable<Screen.Orders> {
                            OrdersScreen(onNavigateBack = { navController.popBackStack() })

                        }
                    }
                }
            }
        }
    }
}

