package com.example.ondiet.core.presentation.component.bottombar

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.ondiet.R
import com.example.ondiet.core.presentation.component.navigation.Screen

enum class BottomBarItems(
    val route: String,
    val icon: ImageVector,
    @StringRes val contentDescription: Int
) {
    Home(
        route = Screen.HomeScreen.route,
        icon = Icons.Filled.Home,
        contentDescription = R.string.home
    ),
    Search(
        route = Screen.SearchScreen.route,
        icon = Icons.Filled.Search,
        contentDescription = R.string.search
    )
}
