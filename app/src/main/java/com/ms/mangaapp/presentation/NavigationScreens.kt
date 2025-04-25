package com.ms.mangaapp.presentation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import kotlinx.serialization.Serializable
import kotlin.reflect.KClass

@Serializable
object AuthScreenGraph

@Serializable
object SignInScreen

@Serializable
object ForgetPasswordScreen

@Serializable
object MainScreenGraph

@Serializable
object HomeScreen

@Serializable
data class DetailedScreen(val id: String)

@Serializable
object FaceDetectScreenGraph

@Serializable
object CameraScreen


data class BottomNavItem(
    val screen: Any, // e.g HomeScreen or CameraScreen
    val label: String,
    val icon: ImageVector
)
val bottomNavItems = listOf(
    BottomNavItem(
        MainScreenGraph,
        "Home",
        Icons.Filled.Home
    ),
    BottomNavItem(
        FaceDetectScreenGraph,
        "Camera",
        Icons.Filled.Camera
    )
)

val appBarScreens = listOf(
    HomeScreen::class.qualifiedName,
    DetailedScreen::class.qualifiedName,
)

val bottomBarScreens = listOf(
    HomeScreen::class.qualifiedName,
    CameraScreen::class.qualifiedName
)

inline fun getScreenName(value: String?): String {
    return when (value) {
        HomeScreen::class.qualifiedName -> "Manga"
        DetailedScreen::class.qualifiedName -> "Details"
        CameraScreen::class.qualifiedName -> "Camera"
        else -> "Manga 2"
    }
}

inline fun getScreens(value: String): Any {
    return when (value) {
        HomeScreen::class.qualifiedName -> HomeScreen
        CameraScreen::class.qualifiedName -> CameraScreen
        else -> HomeScreen
    }
}

inline fun <reified T> getRouteName(): String {
    return T::class.qualifiedName!!
}

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.sharedViewModel(navController: NavController): T {
    val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
    val parentEntry = remember(this) {
        navController.getBackStackEntry(navGraphRoute)
    }
    return hiltViewModel(parentEntry)
}

fun NavController.navigateToMainScreen() {
    this.navigate(MainScreenGraph) {
        popUpTo<AuthScreenGraph> {
            inclusive = true
        }
    }
}


fun NavController.navigateToAuthScreen() {
    this.navigate(AuthScreenGraph) {
        popUpTo<MainScreenGraph> {
            inclusive = true
        }
        launchSingleTop = true
    }
}

fun NavHostController.navigateWithoutPopWithStateSingleTopTo(route: Any) {
    this.navigate(route) {
        popUpTo(this@navigateWithoutPopWithStateSingleTopTo.graph.startDestinationId) {
            inclusive = false
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}
