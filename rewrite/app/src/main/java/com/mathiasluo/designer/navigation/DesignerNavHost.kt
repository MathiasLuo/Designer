package com.mathiasluo.designer.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.mathiasluo.designer.presentation.detail.ShotDetailScreen
import com.mathiasluo.designer.presentation.feed.FeedScreen

object Routes {
    const val Feed = "feed"
    const val Detail = "shot/{shotId}"
    fun detail(shotId: String) = "shot/$shotId"
}

@Composable
fun DesignerNavHost() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Routes.Feed) {
        composable(Routes.Feed) {
            FeedScreen(
                onShotClick = { id -> navController.navigate(Routes.detail(id)) },
            )
        }
        composable(
            route = Routes.Detail,
            arguments = listOf(navArgument("shotId") { type = NavType.StringType }),
        ) {
            ShotDetailScreen(onBack = { navController.popBackStack() })
        }
    }
}
