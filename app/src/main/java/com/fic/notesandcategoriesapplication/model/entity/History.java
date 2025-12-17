package com.fic.notesandcategoriesapplication.model.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

// 1. Crear la entidad History con tabla history
@Entity(tableName = "history_table")
public class History {

    // 1.a. history_id (Clave Primaria)
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "history_id")
    private int historyId;

    // 1.b. action (Tipo de acción)
    // 6. Validar que action no esté vacío
    @ColumnInfo(name = "action")
    @NonNull
    private String action;

    // 1.c. created_at (Registro de tiempo)
    // 6. Validar que created_at no esté vacío
    @ColumnInfo(name = "created_at")
    @NonNull
    private Date createdAt;

    // 1.d. details (Detalles de la acción)
    @ColumnInfo(name = "details")
    private String details;

    // Constructor para Room
    public History(@NonNull String action, @NonNull Date createdAt, String details) {
        this.action = action;
        this.createdAt = createdAt;
        this.details = details;
    }

    // --- Getters y Setters ---

    public int getHistoryId() {
        return historyId;
    }

    public void setHistoryId(int historyId) {
        this.historyId = historyId;
    }

    @NonNull
    public String getAction() {
        return action;
    }

    public void setAction(@NonNull String action) {
        this.action = action;
    }

    @NonNull
    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(@NonNull Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    // Método de utilidad para formatear la fecha
    public String getFormattedDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        return sdf.format(createdAt);
    }
}