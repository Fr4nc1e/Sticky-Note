package com.example.ondiet.core.presentation.event

import com.example.ondiet.core.presentation.util.UiText

sealed class UiEvent {
    data class Navigate(val route: String) : UiEvent()
    data class SnackBarEvent(val uiText: UiText) : UiEvent()
    object NavigateUp : UiEvent()
}
