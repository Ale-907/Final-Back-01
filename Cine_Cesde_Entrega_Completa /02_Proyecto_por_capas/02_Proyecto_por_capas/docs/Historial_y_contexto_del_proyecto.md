# Historial y contexto del proyecto Cine Cesde

Este documento conserva un resumen de la conversación y del trabajo realizado para poder continuar en el futuro. **No es una transcripción literal completa del chat.**

## Sobre el estudiante y el trabajo

- Estudiante de programación de segundo semestre.
- Trabajo de backend en Java con IntelliJ IDEA, Maven y base de datos MySQL/MariaDB mediante XAMPP y phpMyAdmin.
- Tema: administración de un cine.
- Objetivo: acercarse a la estructura por capas del proyecto del profesor, adaptando las entidades y funcionalidades a un cine.
- El estudiante quiere comprender el código y prepararse para explicarlo en una sustentación, no solo ejecutarlo.

## Ubicaciones importantes

- Carpeta general: `F:\BACK\JAVA CODE`.
- Proyecto de referencia del profesor: `F:\BACK\JAVA CODE\CODIGO PROFE\ShortHomeApp-main\ShortHomeApp-main`.
- Proyecto principal de cine: `F:\BACK\JAVA CODE\JAVA CODE CINE MOMENTO 2 (FINAL)\Cine_Cesde_Entrega_Completa\02_Proyecto_por_capas`.
- Carpeta `01_Correcciones`: el estudiante pidió expresamente **no modificarla**.
- Esquema SQL: `02_Proyecto_por_capas\sql\schema.sql`.
- Guía de aprendizaje en PDF: `02_Proyecto_por_capas\docs\Guia_aprendizaje_cine.pdf`.
- Versión HTML de la guía: `02_Proyecto_por_capas\docs\Guia_aprendizaje_cine.html`.

## Resumen cronológico de la conversación

1. El estudiante preguntó si había conversaciones anteriores sobre un trabajo de cine. Se aclaró que solo estaba disponible el contexto del chat actual.
2. Solicitó comparar su proyecto con el código del profesor y acercar las carpetas y clases al modelo de este, conservando el tema de cine.
3. Se identificó que los dos proyectos comparten una organización por capas: `domain`, `view`, `service`, `persistence`, `configuration` y `userinterface`. La organización interna de la persistencia era diferente.
4. El estudiante indicó que no se debía modificar `01_Correcciones` y preguntó qué entidades podían retirarse. Se explicaron las dependencias y se aclaró que sin la rúbrica no podía garantizarse una calificación o cumplimiento exacto.
5. Se acordó eliminar `Taquillero`, `Confitero` y `Empleado`; conservar `Cliente`, `Pelicula`, `Sala`, `Asiento`, `Ticket`, `Cine` y `ComboComida`; unificar `Sala2D` y `Sala3D` en una clase `Sala` con tipo 2D o 3D; y mantener el combo como opcional en el ticket.
6. Se realizaron los cambios por etapas. El estudiante pidió explícitamente completar los trabajos que habían quedado pendientes después de algunas respuestas parciales.
7. Se renombró el punto de entrada de `Application` a `App`, se separaron los repositorios JDBC por entidad, se añadieron mappers y se conectaron desde `Config`; se utilizó el patrón `DataBaseConnection.getInstance().getConnection()` y se cambiaron nombres terminados en `Imp` a `Impl`.
8. Se adaptó `PruebaCapas.java` a la sala unificada. Se comprobó la compilación Maven, 85 comprobaciones en memoria y una prueba JDBC de creación y verificación en una base temporal de MariaDB de XAMPP. La base temporal se eliminó después de la prueba. Estos resultados describen la verificación realizada en ese momento; no sustituyen volver a probar después de cambios futuros.
9. Se comparó nuevamente con el código del profesor: la estructura general es similar, aunque las entidades, cantidad de archivos, funcionalidades y algunos detalles son distintos. No se afirmó que la rúbrica del profesor estuviera garantizada.
10. El estudiante preguntó por qué en IntelliJ su proyecto mostraba archivos `.java` mientras el profesor veía clases. Se explicó que las clases Java se guardan en archivos `.java` y que la vista y el reconocimiento de `src/main/java` como fuente pueden afectar la presentación.
11. Se explicaron los pasos para abrir XAMPP, iniciar Apache y MySQL, abrir `http://localhost/phpmyadmin/`, crear o seleccionar `cine_cesde`, importar `sql/schema.sql` y ejecutar `app.App`.
12. El estudiante mostró phpMyAdmin: primero la estructura de `cliente` con columnas `cedula` y `nombre`; luego la vista de datos con dos registros visibles, cédulas `12` y `120`. Se explicó que la pestaña Estructura muestra columnas y Examinar muestra filas. El hecho de ver filas solo confirma el guardado desde Java si las filas fueron creadas mediante el programa.
13. El estudiante pidió una ruta de aprendizaje en PDF sobre las capas, las clases, sus relaciones, las decisiones del modelo y cómo sustentar el trabajo. Se creó `docs\Guia_aprendizaje_cine.pdf` y se comprobó que podía leerse. Incluye 10 páginas, ejercicios, plan de cinco sesiones, glosario y preguntas de sustentación.
14. El estudiante pidió guardar la conversación para retomarla en el futuro. Se creó este resumen. Para conservar cada mensaje literalmente, es necesario usar la función de exportación o historial de la interfaz con la que se accede al chat, si está disponible.

## Estructura y funcionamiento actual

El punto de entrada es `src\main\java\app\App.java`. `configuration\Config.java` crea y conecta la conexión JDBC, mappers, repositorios, servicios, vistas y menú.

Recorrido típico de registro de cliente:

```text
App → Config → UserInterface → ClienteView
    → ClienteServiceInterface → ClienteServiceImpl
    → ClienteRepositoryPort → ClienteRepositoryDB
    → MySQL/MariaDB
```

- `domain`: clases de negocio y `ClasificacionEnum`.
- `userinterface`: menús de consola.
- `view`: solicita y presenta datos al usuario.
- `service`: lógica de negocio, ayudas y validaciones.
- `service/inputport`: contratos de servicios.
- `service/outputport`: contratos de repositorios.
- `persistence/dbconnection`: conexión JDBC.
- `persistence/mapper`: interfaz `RowMapper` y mappers de entidades.
- `persistence/repository`: repositorios `*RepositoryDB` para la base y `*RepositoryImpl` en memoria para las pruebas.

El programa configurado utiliza repositorios JDBC. `Sala` distingue `2D` y `3D` mediante un atributo de tipo. Un `Ticket` relaciona cliente, película, sala, asiento y combo opcional. El esquema utiliza una tabla intermedia `cine_pelicula` y claves relacionadas. Los puntos del cliente se calculan a partir de tickets y precios de combos en la consulta del repositorio.

**Alcance:** no hay entidad de función con fecha y hora; la sala se modela como una única función y un asiento no puede tener dos tickets activos en esa sala. El esquema conserva columnas `dolby` y `gafas` de la versión anterior, no usadas por el modelo de sala actual.

## Preparación y apertura

1. Abrir XAMPP Control Panel e iniciar Apache y MySQL.
2. Abrir `http://localhost/phpmyadmin/`.
3. Para una instalación nueva, crear la base `cine_cesde`, seleccionarla e importar `sql\schema.sql`. Si ya existe con tablas o datos, revisarlos antes: el script no migra automáticamente esquemas anteriores.
4. En IntelliJ abrir la carpeta `02_Proyecto_por_capas` como proyecto Maven y configurar un JDK 17 o superior.
5. Ejecutar `app.App`.

Por defecto la conexión utiliza `jdbc:mysql://localhost:3306/cine_cesde?serverTimezone=UTC`, usuario `root` y contraseña vacía. Se pueden ajustar con las variables `CINE_DB_URL`, `CINE_DB_USER` y `CINE_DB_PASSWORD`. No incluir contraseñas reales en este documento.

## Para retomar con un asistente en el futuro

Compartir este archivo junto con la solicitud nueva. Antes de modificar el proyecto, revisar de nuevo los archivos actuales: pueden haber cambiado desde esta conversación. Conservar la temática de cine y no modificar `01_Correcciones` salvo que el estudiante lo solicite expresamente. Para una revisión de cumplimiento académico, pedir o consultar la rúbrica del profesor.
