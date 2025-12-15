package com.fic.notesandcategoriesapplication.model;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Category.class, Note.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract NoteCategoryDao noteCategoryDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "notes_db")
                            .addCallback(sRoomDatabaseCallback) // Opcional: para datos iniciales
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // Callback para insertar datos iniciales
    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                NoteCategoryDao dao = INSTANCE.noteCategoryDao();

                // Datos iniciales de categorías
                long workId = dao.insertCategory(new Category("Work"));
                long personalId = dao.insertCategory(new Category("Personal"));
                long ideasId = dao.insertCategory(new Category("Ideas"));

                // Datos iniciales de notas
                dao.insertNote(new Note("Project Alpha", "Finish the MVC implementation.", System.currentTimeMillis(), (int) workId));
                dao.insertNote(new Note("Shopping List", "Buy milk and eggs.", System.currentTimeMillis() - 86400000, (int) personalId));
                dao.insertNote(new Note("New App Concept", "A to-do list with a gamified interface.", System.currentTimeMillis() - 172800000, (int) ideasId));
            });
        }
    };
}