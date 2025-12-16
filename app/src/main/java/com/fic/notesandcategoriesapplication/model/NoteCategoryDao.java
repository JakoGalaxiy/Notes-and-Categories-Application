package com.fic.notesandcategoriesapplication.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete; // <-- Importación necesaria
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update; // <-- Importación necesaria

import java.util.List;

@Dao
public interface NoteCategoryDao {

    // --- Inserciones (Ya existentes) ---

    @Insert
    long insertCategory(Category category);

    @Insert
    void insertNote(Note note);

    // --- MÉTODOS DE ACTUALIZACIÓN (NUEVOS) ---

    // 1. Actualizar una Nota
    @Update
    void updateNote(Note note);

    // 2. Actualizar una Categoría
    @Update
    void updateCategory(Category category);

    // --- MÉTODOS DE ELIMINACIÓN (NUEVOS) ---

    // 3. Eliminar una Nota
    // Room utiliza la clave primaria del objeto Note para saber qué fila eliminar.
    @Delete
    void deleteNote(Note note);

    // 4. Eliminar una Categoría
    @Delete
    void deleteCategory(Category category);

    // --- Consultas Avanzadas (Ya existentes) ---

    @Transaction
    @Query("SELECT * FROM categories ORDER BY category_name ASC")
    LiveData<List<CategoryWithNotes>> getCategoriesWithNotes();

    @Query("SELECT * FROM notes WHERE note_title LIKE :searchQuery OR note_content LIKE :searchQuery ORDER BY created_at DESC")
    LiveData<List<Note>> searchNotesByText(String searchQuery);

    @Query("SELECT * FROM notes WHERE category_id = :categoryId ORDER BY created_at DESC")
    LiveData<List<Note>> getNotesByCategoryId(int categoryId);

    // --- Obtener Categorías (Ya existentes) ---

    @Query("SELECT * FROM categories ORDER BY category_name ASC")
    LiveData<List<Category>> getAllCategories();
}