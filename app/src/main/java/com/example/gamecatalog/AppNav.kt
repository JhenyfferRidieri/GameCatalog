package com.example.gamecatalog.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.gamecatalog.ui.screen.AddGameScreen
import com.example.gamecatalog.ui.screen.DetailScreen
import com.example.gamecatalog.ui.screen.HomeScreen

object Routes {
    const val HOME = "home"
    const val ADD = "add"
    const val DETAILS = "details/{id}"
}

@Composable
fun AppNav(vm: GameViewModel = viewModel()) {
    val nav = rememberNavController()

    NavHost(navController = nav, startDestination = Routes.HOME) {
        composable(Routes.HOME) {
            HomeScreen(
                vm = vm,
                onAdd = { nav.navigate(Routes.ADD) },
                onOpen = { id -> nav.navigate("details/$id") }
            )
        }
        composable(Routes.ADD) {
            AddGameScreen(onDone = { nav.popBackStack() }, vm = vm)
        }
        composable(
            route = Routes.DETAILS,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            DetailScreen(onBack = { nav.popBackStack() }, vm = vm, id = id)
        }
    }
}
