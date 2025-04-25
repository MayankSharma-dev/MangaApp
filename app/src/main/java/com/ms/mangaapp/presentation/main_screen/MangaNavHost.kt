package com.ms.mangaapp.presentation.main_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.BottomAppBarScrollBehavior
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.ms.mangaapp.presentation.AuthScreenGraph
import com.ms.mangaapp.presentation.CameraScreen
import com.ms.mangaapp.presentation.DetailedScreen
import com.ms.mangaapp.presentation.FaceDetectScreenGraph
import com.ms.mangaapp.presentation.ForgetPasswordScreen
import com.ms.mangaapp.presentation.HomeScreen
import com.ms.mangaapp.presentation.MainScreenGraph
import com.ms.mangaapp.presentation.SignInScreen
import com.ms.mangaapp.presentation.main_screen.detail.DetailViewModel
import com.ms.mangaapp.presentation.main_screen.detail.MangaDetailScreenUI
import com.ms.mangaapp.presentation.main_screen.face_detection.FaceDetectionUI
import com.ms.mangaapp.presentation.main_screen.home.HomeScreen
import com.ms.mangaapp.presentation.main_screen.home.HomeViewModel
import com.ms.mangaapp.presentation.navigateToAuthScreen
import com.ms.mangaapp.presentation.navigateToMainScreen
import com.ms.mangaapp.presentation.sharedViewModel
import com.ms.mangaapp.presentation.sign_in.AuthRepository
import com.ms.mangaapp.presentation.sign_in.SignInViewModel
import com.ms.mangaapp.presentation.sign_in.SignScreenUI
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MangaNavHost(
    navController: NavHostController,
    startDestination: Any,
    scrollBehavior: TopAppBarScrollBehavior,
    modifier: Modifier = Modifier
) {

    val lazyGridState = rememberLazyGridState()
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {

        navigation<AuthScreenGraph>(
            startDestination = SignInScreen
        ) {

            composable<SignInScreen> {
                val viewModel = it.sharedViewModel<SignInViewModel>(navController)
                SignScreenUI(
                    viewModel = viewModel,
                    onNavigate = {
                        navController.navigateToMainScreen()
                    }
                )
            }

            composable<ForgetPasswordScreen> {
                val viewModel = it.sharedViewModel<SignInViewModel>(navController)
            }
        }

        navigation<MainScreenGraph>(
            startDestination = HomeScreen
        ) {
            composable<HomeScreen> {

                val viewModel = hiltViewModel<HomeViewModel>()

                HomeScreen(
                    lazyGridState,
                    scrollBehavior,
                    viewModel,
                    onNavigateToDetail = {
                        navController.navigate(DetailedScreen(it))
                    }
                )
            }

            composable<DetailedScreen> {
                val arguments = it.toRoute<DetailedScreen>()
                val viewModel = hiltViewModel<DetailViewModel>()
                val data by viewModel.getMangaById(arguments.id).collectAsStateWithLifecycle(null)

                if (data != null) {
                    MangaDetailScreenUI(data!!)
                } else {
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
        navigation<FaceDetectScreenGraph>(
            startDestination = CameraScreen
        ) {
            composable<CameraScreen> {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    FaceDetectionUI()
                }
            }
        }
    }
}