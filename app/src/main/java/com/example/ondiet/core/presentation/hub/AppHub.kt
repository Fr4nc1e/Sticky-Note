package com.example.ondiet.core.presentation.hub

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.ondiet.core.presentation.component.bottombar.BottomBar
import com.example.ondiet.core.presentation.component.navigation.NavHub
import com.example.ondiet.core.presentation.component.navigation.bottomShowList
import com.example.ondiet.core.presentation.component.topbar.TopBar
import com.example.ondiet.core.presentation.hub.viewmodel.HubViewModel

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppHub(
    modifier: Modifier = Modifier,
    viewModel: HubViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val navController = rememberNavController()
    val snackBarHostState = remember {
        SnackbarHostState()
    }

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
                title = viewModel.title.value,
                onNavigateUp = navController::navigateUp
            )
        },
        bottomBar = {
            if (viewModel.title.value in bottomShowList) {
                BottomBar(
                    onNavigate = navController::navigate,
                    onPopBackStack = navController::popBackStack,
                    currentDestination = navController.currentDestination?.route
                )
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState) {
                Snackbar(
                    snackbarData = it,
                    shape = RoundedCornerShape(16.dp)
                )
            }
        }
    ) {
        NavHub(
            modifier = Modifier.padding(it),
            navController = navController,
            snackBarHostState = snackBarHostState
        )
    }
}
