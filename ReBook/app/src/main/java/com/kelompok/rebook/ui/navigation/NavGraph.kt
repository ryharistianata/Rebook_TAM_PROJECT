package com.kelompok.rebook.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kelompok.rebook.ui.screen.*
import com.kelompok.rebook.ui.viewmodel.BookViewModel

@Composable
fun ReBookNavGraph(
    navController: NavHostController = rememberNavController(),
    isDarkTheme: Boolean,
    onThemeToggle: () -> Unit
) {
    val bookViewModel: BookViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                isDarkTheme = isDarkTheme,
                onThemeToggle = onThemeToggle,
                onLoginSuccess = {
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate("register")
                }
            )
        }
        composable("register") {
            RegisterScreen(
                isDarkTheme = isDarkTheme,
                onThemeToggle = onThemeToggle,
                onRegisterSuccess = {
                    navController.navigate("login")
                },
                onLoginClick = {
                    navController.popBackStack()
                }
            )
        }
        composable("dashboard") {
            DashboardScreen(
                isDarkTheme = isDarkTheme,
                onThemeToggle = onThemeToggle,
                viewModel = bookViewModel,
                onNavigateToExplore = { navController.navigate("explore") },
                onNavigateToWishlist = { navController.navigate("wishlist") },
                onNavigateToProfile = { navController.navigate("profile") },
                onNavigateToUpload = { navController.navigate("upload") }
            )
        }
        composable("explore") {
            ExploreScreen(
                viewModel = bookViewModel,
                isDarkTheme = isDarkTheme,
                onThemeToggle = onThemeToggle,
                onBackClick = { navController.popBackStack() },
                onBookClick = { book ->
                    bookViewModel.selectBook(book)
                    navController.navigate("detail")
                }
            )
        }
        composable("detail") {
            val selectedBook by bookViewModel.selectedBook.collectAsState()

            selectedBook?.let { book ->
                BookDetailScreen(
                    book = book,
                    viewModel = bookViewModel,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
        composable("upload") {
            UploadScreen(
                isDarkTheme = isDarkTheme,
                onThemeToggle = onThemeToggle,
                viewModel = bookViewModel,
                onBackClick = { navController.popBackStack() },
                onUploadSuccess = { navController.popBackStack() }
            )
        }
        composable("profile") {
            ProfileScreen(
                isDarkTheme = isDarkTheme,
                onThemeToggle = onThemeToggle,
                onBackClick = { navController.popBackStack() },
                onLogoutClick = {
                    navController.navigate("login") {
                        popUpTo("dashboard") { inclusive = true }
                    }
                }
            )
        }
        composable("wishlist") {
            WishlistScreen(
                isDarkTheme = isDarkTheme,
                onThemeToggle = onThemeToggle,
                viewModel = bookViewModel,
                onBackClick = { navController.popBackStack() },
                onBookClick = { book ->
                    bookViewModel.selectBook(book)
                    navController.navigate("detail")
                }
            )
        }
    }
}
