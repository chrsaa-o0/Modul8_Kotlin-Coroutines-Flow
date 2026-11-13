package com.example.modul5

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.modul5.data.repository.DefaultStudentRepository
import com.example.modul5.data.remote.RetrofitClient
import com.example.modul5.data.remote.StudentRemoteDataSource
import com.example.modul5.screen.AboutScreen
import com.example.modul5.screen.HomeScreen
import com.example.modul5.screen.DetailScreen
import com.example.modul5.screen.ProfileScreen
import com.example.modul5.ui.detail.DetailViewModel
import com.example.modul5.ui.home.HomeViewModel

@Composable
fun NavigationAppLab() {
    val navController = rememberNavController()
    val remoteDataSource = remember { StudentRemoteDataSource(RetrofitClient.apiService) }
    val repository = remember { DefaultStudentRepository(remoteDataSource) }

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.HOME) {
            val homeViewModel: HomeViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                factory = object : ViewModelProvider.Factory {
                    @Suppress("UNCHECKED_CAST")
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return HomeViewModel(repository) as T
                    }
                }
            )
            val homeUiState by homeViewModel.uiState.collectAsStateWithLifecycle()
            val searchQuery by homeViewModel.searchQuery.collectAsStateWithLifecycle()
            HomeScreen(
                uiState = homeUiState,
                searchQuery = searchQuery,
                onSearchQueryChange = { query ->
                    homeViewModel.onSearchQueryChange(query)
                },
                onStudentClick = { id ->
                    navController.navigate(Routes.detail(id))
                },
                onOpenProfile = {
                    navController.navigate(Routes.PROFILE)
                },
                onOpenAbout = {
                    navController.navigate(Routes.ABOUT)
                },
                onRetry = {
                    homeViewModel.retry()
                },
                onRefresh = {
                    homeViewModel.refresh()
                }
            )
        }
        composable(
            route = Routes.DETAIL,
            arguments = listOf(
                navArgument("studentId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->
            val studentId = backStackEntry.arguments?.getInt("studentId") ?: 0
            val viewModel: DetailViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
                factory = DetailViewModel.factory(repository)
            )
            LaunchedEffect(studentId) {
                viewModel.loadStudent(studentId)
            }

            val detailUiState by viewModel.uiState.collectAsStateWithLifecycle()

            DetailScreen(
                uiState = detailUiState,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.PROFILE) {
            ProfileScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.ABOUT) {
            AboutScreen(
                onBack = { navController.popBackStack() }
            )
        }
    }
}