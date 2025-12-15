package com.fic.notesandcategoriesapplication.model;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class CategoryWithNotes {
    @Embedded
    public Category category;

    @Relation(
            parentColumn = "category_id", // Columna de la entidad Category
            entityColumn = "category_id"  // Columna de la entidad Note que hace referencia a Category
    )
    public List<Note> notes;

    // Getters
    public Category getCategory() {
        return category;
    }

    public List<Note> getNotes() {
        return notes;
    }
}