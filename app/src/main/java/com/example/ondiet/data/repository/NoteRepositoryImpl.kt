package com.example.ondiet.data.repository

import android.util.Log
import com.example.ondiet.domain.model.Note
import com.example.ondiet.domain.repository.NoteRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.mongodb.kbson.ObjectId

class NoteRepositoryImpl(
    private val realm: Realm
) : NoteRepository {
    override suspend fun getNote(noteId: ObjectId): Note? {
        return realm
            .query<Note>(query = "_id == $0", noteId)
            .first()
            .find()
    }

    override fun getAllNotes(): Flow<List<Note>> {
        return realm
            .query<Note>()
            .asFlow()
            .map { it.list }
    }

    override fun filterNotes(title: String): Flow<List<Note>> {
        return realm
            .query<Note>(query = "title CONTAINS[c] $0", title)
            .asFlow()
            .map { it.list }
    }

    override suspend fun insertNote(note: Note) {
        realm.write {
            copyToRealm(note)
        }
    }

    override suspend fun updateNote(note: Note) {
        realm.write {
            val queriedNote = query<Note>(query = "_id == $0", note._id)
                .first()
                .find()
            queriedNote?.apply {
                title = note.title
                description = note.description
            }
        }
    }

    override suspend fun deleteNote(noteId: ObjectId) {
        realm.write {
            val queriedNote = query<Note>(query = "_id == $0", noteId)
                .first()
                .find()
            try {
                queriedNote?.let {
                    delete(it)
                }
            } catch (e: Exception) {
                Log.d("NoteDelete", "${e.message}")
            }
        }
    }
}
