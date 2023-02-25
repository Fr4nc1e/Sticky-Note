package com.example.ondiet.core.presentation.event

import com.example.ondiet.core.presentation.util.UiText

sealed class CoreUiEvent {
    data class Navigate(val route: String) : CoreUiEvent()
    data class SnackBarEvent(val uiText: UiText) : CoreUiEvent()
    object NavigateUp : CoreUiEvent()
}
