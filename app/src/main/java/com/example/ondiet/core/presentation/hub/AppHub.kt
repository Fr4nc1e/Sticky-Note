package com.example.ondiet.core.presentation.hub

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.ondiet.core.presentation.component.bottombar.BottomBar
import com.example.ondiet.core.presentation.component.navigation.NavHub
import com.example.ondiet.core.presentation.component.topbar.TopBar
import com.example.ondiet.core.presentation.hub.viewmodel.HubViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHub(
    modifier: Modifier = Modifier,
    viewModel: HubViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val navController = rememberNavController()

    LaunchedEffect(navController) {
        navController.currentBackStackEntryFlow.collect { backStackEntry ->
            backStackEntry.destination.route?.let { route ->
                viewModel.getTitleByRoute(context, route)
            }
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopBar(
                title = viewModel.title.value
            )
        },
        bottomBar = {
            BottomBar(
                onNavigate = navController::navigate,
                onPopBackStack = navController::popBackStack,
                currentDestination = navController.currentDestination?.route
            )
        }
    ) {
        NavHub(
            modifier = Modifier.padding(it),
            navController = navController
        )
    }
}
