package com.example.ondiet.core.presentation.viewmodel

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.ondiet.R
import com.example.ondiet.core.presentation.component.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HubViewModel @Inject constructor() : ViewModel() {
    private val _title = mutableStateOf("")
    val title: State<String> = _title

    fun getTitleByRoute(
        context: Context,
        route: String
    ) {
        _title.value = when (route) {
            Screen.HomeScreen.route -> context.getString(R.string.home)
            Screen.NoteDetailScreen.route -> context.getString(R.string.note_detail)
            Screen.CreateNoteScreen.route -> context.getString(R.string.create_note)
            Screen.SearchScreen.route -> context.getString(R.string.search)
            else -> ""
        }
    }
}
