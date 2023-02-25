package com.example.ondiet.domain.repository

import com.example.ondiet.domain.model.Note
import kotlinx.coroutines.flow.Flow
import org.mongodb.kbson.ObjectId

interface NoteRepository {
    fun getAllNotes(): Flow<List<Note>>
    fun filterNotes(title: String): Flow<List<Note>>
    suspend fun insertNote(note: Note)
    suspend fun updateNote(note: Note)
    suspend fun deleteNote(id: ObjectId)
}
