package com.example.ondiet.core.presentation.component.bottombar

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ondiet.R
import com.example.ondiet.core.presentation.component.navigation.Screen

@Composable
fun BottomBar(
    currentDestination: String?,
    onNavigate: (String) -> Unit = {},
    onPopBackStack: () -> Unit = {}
) {
    BottomAppBar(
        modifier = Modifier.fillMaxWidth()
            .clip(
                RoundedCornerShape(
                    topStart = 50.dp,
                    topEnd = 50.dp
                )
            ),
        actions = {
            BottomBarItems.values().forEach { bottomBarItems ->
                IconButton(
                    onClick = {
                        if (currentDestination != bottomBarItems.route) {
                            onPopBackStack()
                            onNavigate(bottomBarItems.route)
                        }
                    }
                ) {
                    Icon(
                        imageVector = bottomBarItems.icon,
                        contentDescription = stringResource(id = bottomBarItems.contentDescription)
                    )
                }
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onNavigate(Screen.CreateNoteScreen.route)
                }
            ) {
                Icon(
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = stringResource(R.string.add_note)
                )
            }
        }
    )
}
