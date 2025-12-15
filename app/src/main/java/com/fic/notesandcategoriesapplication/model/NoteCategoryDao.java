package com.fic.notesandcategoriesapplication.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface NoteCategoryDao {

    // --- Inserciones ---

    @Insert
    long insertCategory(Category category); // Retorna el categoryId

    @Insert
    void insertNote(Note note);

    // --- Consultas Avanzadas (Relación 1:N y Agrupación) ---

    // Este método usa la anotación @Transaction para asegurar que las operaciones de consulta
    // sean atómicas. Retorna la relación 1:N modelada por CategoryWithNotes.
    @Transaction
    @Query("SELECT * FROM categories ORDER BY category_name ASC")
    LiveData<List<CategoryWithNotes>> getCategoriesWithNotes();

    // --- Consultas Avanzadas (Búsqueda con LIKE) ---

    // Búsqueda dinámica en note_title y note_content por texto
    @Query("SELECT * FROM notes WHERE note_title LIKE :searchQuery OR note_content LIKE :searchQuery ORDER BY created_at DESC")
    LiveData<List<Note>> searchNotesByText(String searchQuery);

    // Búsqueda de todas las notas de una categoría específica (ejemplo de filtrado simple)
    @Query("SELECT * FROM notes WHERE category_id = :categoryId ORDER BY created_at DESC")
    LiveData<List<Note>> getNotesByCategoryId(int categoryId);

    // --- Obtener Categorías (Para spinners/vistas de agregar) ---

    @Query("SELECT * FROM categories ORDER BY category_name ASC")
    LiveData<List<Category>> getAllCategories();
}