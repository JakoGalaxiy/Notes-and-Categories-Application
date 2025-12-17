Actividad 2. Relaciones y consultas avanzadas
Notes and Categories Application

Modelar una relación 1:N en SQLite usando Room y realizar consultas avanzadas (agrupación
y búsqueda). El resultado esperado es una aplicación que muestre notas organizadas por
categorías y permita búsquedas dinámicas.

Instrucciones:
1. Crear entidades Category y Note con tablas categories y notes
a. Category: category_id category_name
b. Note: note_id, note_title, note_content, created_at, category_id
2. Definir relación 1:N entre categorías y notas.
3. DAO con métodos para obtener notas por categoría y buscar notas por texto usando
LIKE.
4. Configurar la clase de base de datos.
5. Implementar vistas en XML:
a. Interfaz principal mostrando categorías y sus notas.
b. Interfaz secundaria para agregar notas y categorías.
6. Los controladores deben gestionar la lógica de interacción.

--------------------------------------------------------------------------------------------

Actividad 3. Historial de acciones
Action History Application
Implementar un sistema de auditoría interna que registre las operaciones CRUD realizadas
en al menos una de las aplicaciones anteriores (Task Management o Notes and Categories).
El resultado esperado es que la aplicación elegida muestre un historial consultable de las
acciones realizadas por el usuario.
Instrucciones:
1. Crear la entidad History con tabla history y columnas:
a. history_id
b. action: valores como "insert_task", "update_note", "delete_task"
c. created_at
d. details: información breve del registro afectado (ejemplo: título de la tarea o
nota).

2. Definir un DAO con métodos para:
a. Insertar un nuevo registro de historial cada vez que se ejecute una acción
CRUD.
b. Obtener todo el historial.
c. Filtrar historial por fecha o tipo de acción.
3. Configurar la clase de base de datos.
4. Implementar vistas en XML:
a. Interfaz principal mostrando historial en lista.
b. Opciones de filtro por fecha o tipo de acción.
5. Los controladores deben manejar la lógica de interacción y registrar
automáticamente las acciones en la tabla history.
6. Validar que action y created_at no estén vacíos.
7. Restricción adicional: El historial debe implementarse y consultarse dentro de al
menos una de las aplicaciones anteriores (tareas o notas), para que se observe el
registro de acciones en contexto.

ALUMNO: VÁZQUEZ FAVELA JESÚS DAVID
