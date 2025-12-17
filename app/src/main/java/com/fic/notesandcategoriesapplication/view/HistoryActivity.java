package com.fic.notesandcategoriesapplication.view;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fic.notesandcategoriesapplication.R;
import com.fic.notesandcategoriesapplication.controller.NoteController;
import com.fic.notesandcategoriesapplication.util.HistoryAction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private NoteController noteController;
    private HistoryAdapter adapter;
    private Spinner filterSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        setTitle("Historial de Acciones");

        // Inicialización
        RecyclerView recyclerView = findViewById(R.id.recycler_view_history);
        filterSpinner = findViewById(R.id.spinner_filter_action);
        noteController = new ViewModelProvider(this).get(NoteController.class);

        // Configurar RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new HistoryAdapter();
        recyclerView.setAdapter(adapter);

        // Cargar datos por defecto (sin filtro)
        noteController.getAllHistory().observe(this, adapter::setHistory);

        // Configurar Filtro (Spinner)
        setupFilterSpinner();
    }

    private void setupFilterSpinner() {
        // Obtener todos los valores del enum HistoryAction
        List<String> actionNames = new ArrayList<>();
        actionNames.add("Todas las acciones"); // Opción por defecto

        for (HistoryAction action : HistoryAction.values()) {
            actionNames.add(action.getActionName());
        }

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                actionNames
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filterSpinner.setAdapter(spinnerAdapter);

        // 5. Los controladores deben manejar la lógica de interacción
        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedAction = parent.getItemAtPosition(position).toString();

                if (selectedAction.equals("Todas las acciones")) {
                    // Mostrar todo el historial
                    noteController.getAllHistory().observe(HistoryActivity.this, adapter::setHistory);
                } else {
                    // Obtener la clave de acción (p.ej., "nota creada") para el filtro
                    String actionKey = HistoryAction.values()[position - 1].getActionName();

                    // Filtrar por tipo de acción
                    noteController.getHistoryByAction(actionKey).observe(HistoryActivity.this, adapter::setHistory);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No hacer nada
            }
        });
    }
}