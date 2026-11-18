package com.mathiasluo.designer.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mathiasluo.designer.presentation.auth.LoginScreen
import com.mathiasluo.designer.presentation.detail.ShotDetailScreen
import com.mathiasluo.designer.presentation.shell.MainScreen
import com.mathiasluo.designer.presentation.shell.SessionViewModel

private object Routes {
    const val Main = "main"
    const val Login = "login"
    const val Detail = "shot/{shotId}"
    fun detail(shotId: String) = "shot/$shotId"
}

@Composable
fun DesignerNavHost() {
    val navController = rememberNavController()
    val sessionViewModel: SessionViewModel = hiltViewModel()
    val liked by sessionViewModel.session.liked.collectAsStateWithLifecycle(initialValue = emptySet())
    var openDrawer by remember { mutableStateOf(false) }

    NavHost(navController = navController, startDestination = Routes.Main) {
        composable(Routes.Main) {
            MainScreen(
                openDrawerOnStart = openDrawer,
                onDrawerOpened = { openDrawer = false },
                onOpenShot = { id -> navController.navigate(Routes.detail(id)) },
                onLogin = { navController.navigate(Routes.Login) },
                sessionViewModel = sessionViewModel,
            )
        }
        composable(Routes.Login) {
            LoginScreen(
                onBack = { navController.popBackStack() },
                onLoggedIn = {
                    openDrawer = true
                    navController.popBackStack()
                },
            )
        }
        composable(
            route = Routes.Detail,
            arguments = listOf(navArgument("shotId") { type = NavType.StringType }),
        ) {
            ShotDetailScreen(
                onBack = { navController.popBackStack() },
                liked = liked,
                onToggleLike = sessionViewModel.session::toggleLike,
            )
        }
    }
}
