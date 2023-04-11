package com.example.myapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.example.myapp.ui.main.Main
import com.example.myapp.ui.splash.Splash
import com.example.myapp.ui.user.User


object Screen {
    const val MAIN_ROUTE = "main_route"
    const val MAIN = "main"
    const val SPLASH = "splash"
    const val USER = "USER"
}

@Composable
fun navGraph(navController: NavHostController) {
    val action = remember(navController) { MoveAction(navController = navController) }
    NavHost(navController = navController, startDestination = Screen.SPLASH, route = Screen.MAIN_ROUTE) {
        composable(route = Screen.SPLASH) {
            Splash(moveMain = action.moveMain)
        }
        composable(route = Screen.MAIN) {
            Main(moveUser = action.moveUser)
        }
        composable(route = Screen.USER) {
            User()
        }
    }
}

class MoveAction(navController: NavController) {

    val moveMain: () -> Unit = {
        navController.navigate(route = Screen.MAIN, navOptions = navOptions { popUpTo(Screen.MAIN) })
    }

    val moveUser: () -> Unit = {
        navController.navigate(route = Screen.USER)
    }

}