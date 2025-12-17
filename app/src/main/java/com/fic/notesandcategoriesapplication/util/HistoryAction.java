package com.fic.notesandcategoriesapplication.util;

// 1.b. Valores de la acción
public enum HistoryAction {
    NOTE_CREATED("nota creada"),
    NOTE_UPDATED("nota actualizada"),
    NOTE_DELETED("nota eliminada"),

    CATEGORY_CREATED("categoría creada"),
    CATEGORY_DELETED("categoría eliminada"),
    CATEGORY_UPDATED("categoría actualizada")

    ; // Punto y coma obligatorio

    private final String actionName;

    HistoryAction(String actionName) {
        this.actionName = actionName;
    }

    public String getActionName() {
        return actionName;
    }
}