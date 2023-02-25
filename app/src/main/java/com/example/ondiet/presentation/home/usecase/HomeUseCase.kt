package com.example.ondiet.presentation.home.usecase

import com.example.ondiet.presentation.home.usecase.component.DeleteNotesUseCase
import com.example.ondiet.presentation.home.usecase.component.GetAllNotesUseCase

data class HomeUseCase(
    val getAllNotes: GetAllNotesUseCase,
    val deleteNotes: DeleteNotesUseCase
)
