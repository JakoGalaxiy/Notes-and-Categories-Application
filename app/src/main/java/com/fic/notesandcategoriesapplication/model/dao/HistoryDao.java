package com.fic.notesandcategoriesapplication.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.fic.notesandcategoriesapplication.model.entity.History;

import java.util.List;

// 2. Definir un DAO
@Dao
public interface HistoryDao {

    // 2.a. Insertar un nuevo registro de historial
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertHistory(History history);

    // 2.b. Obtener todo el historial (ordenado por fecha descendente)
    @Query("SELECT * FROM history_table ORDER BY created_at DESC")
    LiveData<List<History>> getAllHistory();

    // 2.c. Filtrar historial por tipo de acción
    @Query("SELECT * FROM history_table WHERE action = :actionType ORDER BY created_at DESC")
    LiveData<List<History>> getHistoryByAction(String actionType);

    // Filtrar por fecha (implementación simple: obtener registros desde una fecha)
    // Nota: El filtrado por rango de fechas a menudo se maneja en el Repositorio/Controller
    @Query("SELECT * FROM history_table WHERE created_at >= :startDate ORDER BY created_at DESC")
    LiveData<List<History>> getHistoryFromDate(long startDate);
}