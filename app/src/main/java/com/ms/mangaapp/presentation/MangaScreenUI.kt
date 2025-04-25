package com.ms.mangaapp.presentation

import android.annotation.SuppressLint
import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.material3.rememberBottomAppBarState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.ms.mangaapp.presentation.sign_in.AuthRepository
import kotlinx.coroutines.launch

import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ms.mangaapp.presentation.common.ObserveAsEvents
import com.ms.mangaapp.presentation.common.SnackBarController
import com.ms.mangaapp.presentation.main_screen.MangaNavHost

@SuppressLint("RestrictedApi")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MangaScreenUI(
    authRepository: AuthRepository
) {

    val isUserSignedIn by remember {
        mutableStateOf(authRepository.getSignInUser() != null)
    }

    val startDestination = if (isUserSignedIn) MainScreenGraph else AuthScreenGraph

    val navController = rememberNavController()

    val scope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val context = LocalContext.current

    val topScrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val bottomScrollBehaviour = BottomAppBarDefaults.exitAlwaysScrollBehavior(rememberBottomAppBarState())

    val showTopBar = currentDestination?.route in appBarScreens
    val showBottomBar = currentDestination?.route in bottomBarScreens

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    ObserveAsEvents(flow = SnackBarController.events, snackbarHostState) { event ->
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()

            val result = snackbarHostState.showSnackbar(
                message = event.message,
                actionLabel = event.action?.name,
                duration = SnackbarDuration.Short,
                withDismissAction = true
            )

            if (result == SnackbarResult.ActionPerformed) {
                event.action?.action?.invoke()
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        },
        modifier = Modifier.fillMaxSize(),
        topBar = {

            if (showTopBar) {
                TopAppBar(
                    colors = topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    title = {
                        Text(getScreenName(currentDestination?.route))
                    },
                    actions = {
                        IconButton(onClick = {
                            scope.launch {
                                authRepository.signOutUser()
                                navController.navigate(AuthScreenGraph) {
                                    popUpTo(0) { inclusive = true }
                                    launchSingleTop = true
                                }
                                authRepository.signOutUser()
                                //navController.navigateToAuthScreen()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Logout,
                                contentDescription = "Localized description"
                            )
                        }
                    },
                    scrollBehavior = topScrollBehavior
                )
            }

        },
        bottomBar = {
            if (showBottomBar) {
                val entry by navController.currentBackStackEntryAsState()
                val currentDestinations = entry?.destination

                /*
                BackHandler {
                    when (currentDestinations?.route) {
                        CameraScreen::class.qualifiedName -> {
                            navController.navigate(HomeScreen) {
                                popUpTo(navController.graph.findStartDestination().id)
                                launchSingleTop = true
                                restoreState = true
                            }
                        }

                        else -> {
                            (context as? Activity)?.finish()
                        }
                    }
                }*/

                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.primary,
                    scrollBehavior = bottomScrollBehaviour
                ) {

                    bottomNavItems.forEach { destinations ->

                        val isSelected =
                            currentDestinations?.hierarchy?.any { it.hasRoute(destinations.screen::class) }
                                ?: false

                        NavigationBarItem(
                            selected = isSelected,
                            onClick = {

                                if (currentDestinations?.route == CameraScreen::class.qualifiedName && destinations.screen == HomeScreen) {
                                    navController.navigateUp()
                                } else {
                                    navController.navigate(destinations.screen) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = destinations.screen == HomeScreen // Only save state for HomeScreen
                                        }
                                        launchSingleTop = true
                                        restoreState = destinations.screen == HomeScreen // Only restore state for HomeScreen
                                    }
                                }
                            },
                            label = { Text(destinations.label) },
                            icon = {
                                Icon(
                                    destinations.icon,
                                    contentDescription = destinations.label
                                )
                            }
                        )

                    }

                }

            }
        },
    ) { innerPadding ->

        MangaNavHost(
            navController = navController,
            startDestination = startDestination,
            scrollBehavior = topScrollBehavior,
            modifier = Modifier.padding(innerPadding)
        )
    }
}