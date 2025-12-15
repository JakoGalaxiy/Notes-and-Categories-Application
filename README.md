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

ALUMNO: VÁZQUEZ FAVELA JESÚS DAVID
