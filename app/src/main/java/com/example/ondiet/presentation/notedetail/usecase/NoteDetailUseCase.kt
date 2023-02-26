package com.example.ondiet.presentation.notedetail.usecase

import com.example.ondiet.presentation.notedetail.usecase.component.GetNoteUseCase
import com.example.ondiet.presentation.notedetail.usecase.component.ModifyNoteUseCase

data class NoteDetailUseCase(
    val getNote: GetNoteUseCase,
    val modifyNote: ModifyNoteUseCase
)
