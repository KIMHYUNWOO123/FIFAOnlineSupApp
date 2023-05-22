package com.woo.myapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.woo.myapp.ui.main.Main
import com.woo.myapp.ui.match.Match
import com.woo.myapp.ui.ranker.Ranker
import com.woo.myapp.ui.splash.Splash
import com.woo.myapp.ui.transaction.Transaction
import com.woo.myapp.ui.user.User


object Screen {
    const val MAIN_ROUTE = "main_route"
    const val MAIN = "main"
    const val SPLASH = "splash"
    const val USER = "USER"
    const val MATCH = "match"
    const val TRANSACTION = "transaction"
    const val RANKER = "ranker"
}

@Composable
fun navGraph(navController: NavHostController) {
    val action = remember(navController) { MoveAction(navController = navController) }
    NavHost(navController = navController, startDestination = Screen.SPLASH, route = Screen.MAIN_ROUTE) {
        composable(route = Screen.SPLASH) {
            Splash(moveMain = action.moveMain)
        }
        composable(route = Screen.MAIN) {
            Main(moveUser = action.moveUser, moveRanker = action.moveRanker)
        }
        composable(route = Screen.USER) {
            User(onMatchView = action.moveMatch, onTransactionView = action.moveTransaction)
        }
        composable(route = "${Screen.MATCH}/{accessId}") { backStackEntry ->
            Match(accessId = backStackEntry.arguments?.getString("accessId") ?: "")
        }
        composable(route = "${Screen.TRANSACTION}/{accessId}") { backStackEntry ->
            Transaction(accessId = backStackEntry.arguments?.getString("accessId") ?: "")
        }
        composable(route = Screen.RANKER) {
            Ranker()
        }
    }
}

class MoveAction(navController: NavController) {

    val moveMain: () -> Unit = {
        navController.navigate(route = Screen.MAIN, navOptions = navOptions { popUpTo(Screen.MAIN_ROUTE) })
    }

    val moveUser: () -> Unit = {
        navController.navigate(route = Screen.USER, navOptions = navOptions { popUpTo(Screen.MAIN) })
    }
    val moveMatch: (String) -> Unit = {
        navController.navigate(route = "${Screen.MATCH}/$it", navOptions = navOptions { popUpTo(Screen.USER) })
    }
    val moveTransaction: (String) -> Unit = {
        navController.navigate(route = "${Screen.TRANSACTION}/$it", navOptions = navOptions { popUpTo(Screen.USER) })
    }
    val moveRanker: () -> Unit = {
        navController.navigate(route = Screen.RANKER, navOptions = navOptions { popUpTo(Screen.MAIN) })
    }

}