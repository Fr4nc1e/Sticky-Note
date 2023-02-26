package com.example.ondiet.core.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.ondiet.R
import com.example.ondiet.core.presentation.component.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class HubViewModel @Inject constructor() : ViewModel() {
    private val _titleFlow = MutableStateFlow("")
    val titleFlow = _titleFlow.asStateFlow()

    fun getTitleByRoute(
        context: Context,
        route: String
    ) {
        _titleFlow.value = when (route) {
            Screen.HomeScreen.route -> context.getString(R.string.home)
            Screen.CreateNoteScreen.route -> context.getString(R.string.create_note)
            Screen.SearchScreen.route -> context.getString(R.string.search)
            else -> ""
        }
    }
}
