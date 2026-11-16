# Cine Cesde: proyecto por capas y JDBC

Aplicacion de consola Java 17 con persistencia MySQL/MariaDB mediante JDBC. Los puertos existentes tienen adaptadores para clientes, peliculas, combos, cines y su catalogo, salas (de tipo 2D o 3D), asientos y tickets. El programa no crea datos de ejemplo al iniciar. El menu utiliza los adaptadores JDBC.

## Requisitos

- JDK 17 o posterior, IntelliJ IDEA y Maven (IntelliJ incluye Maven). El Java 8 que pueda estar en el PATH no sirve para compilar.
- XAMPP con MySQL/MariaDB iniciado o un servidor compatible independiente.
- Internet para que Maven descargue MySQL Connector/J y los plugins la primera vez.

## Preparar la base con XAMPP y phpMyAdmin

1. En el panel de XAMPP inicie MySQL y Apache. Abra `http://localhost/phpmyadmin/`.
2. Cree una base **nueva** llamada `cine_cesde`, con codificacion `utf8mb4`. No seleccione una base con datos existentes del usuario.
3. Seleccione esa base, abra **Importar**, elija `sql/schema.sql` dentro del proyecto y ejecute la importacion. El archivo crea tablas sin borrarlas y no carga ejemplos. `CREATE TABLE IF NOT EXISTS` no migra ni repara tablas preexistentes incompatibles: utilice una base nueva.
4. Compruebe que aparecen las tablas `cliente`, `pelicula`, `combo`, `cine`, `cine_pelicula`, `sala`, `asiento` y `ticket`. Conserve copias de seguridad antes de cualquier modificacion manual de datos.

Por defecto se conecta a `jdbc:mysql://localhost:3306/cine_cesde?serverTimezone=UTC`, usuario `root` y contrasena vacia, frecuente en una instalacion local de XAMPP. Si su servidor utiliza otros datos, configure estas variables de entorno en la configuracion de ejecucion de IntelliJ o en su terminal:

```text
CINE_DB_URL=jdbc:mysql://localhost:3306/cine_cesde?serverTimezone=UTC
CINE_DB_USER=su_usuario
CINE_DB_PASSWORD=su_contrasena
```

No incluya credenciales reales en el repositorio. Utilice un usuario con permisos sobre la base elegida. Los valores por defecto son solo para desarrollo local, no para produccion. La base y las tablas no se crean automaticamente al arrancar. Si hay un error de conexion o SQL se informa y no se cambia silenciosamente a memoria.

## Abrir y ejecutar en IntelliJ

1. Seleccione **Open** y abra `02_Proyecto_por_capas`, la carpeta que contiene `pom.xml`. Importe el proyecto Maven y espere las dependencias.
2. En **File > Project Structure**, configure un Project SDK JDK 17 o posterior. Configure tambien el JDK de Maven si es necesario.
3. Cree o edite la configuracion de ejecucion de `app.App` y defina las variables de entorno indicadas si no utiliza los valores locales por defecto.
4. Ejecute `app.App`. Desde consola use los menus para crear y consultar registros. Para vender un ticket cree primero cliente, pelicula y sala; elija un asiento existente y, opcionalmente, un combo. Se puede asociar una pelicula a un cine desde el menu Cine.
5. Cierre y vuelva a abrir el programa para consultar lo guardado. En phpMyAdmin puede inspeccionar las tablas de la base seleccionada.

Alternativamente, con Maven y JDK 17 configurados en PATH, ejecute `ejecutar.bat` en Windows o `./ejecutar.sh` en sistemas compatibles. Usan Maven para incluir el controlador JDBC; ejecutar solo `java -cp target/classes` no incluye la dependencia.

## Diseno y reglas

`configuration/Config.java` crea la conexion, los mappers y los repositorios JDBC de cada entidad, y los conecta con los servicios y las vistas. Los repositorios `*RepositoryDB` guardan en MySQL; los `*RepositoryImpl` en memoria se usan en la prueba de capas. La conexion compartida se obtiene mediante `DataBaseConnection.getInstance().getConnection()`. El esquema usa claves foraneas, una clave compuesta para asientos, una relacion de catalogo y una restriccion que impide vender el mismo asiento dos veces. Las escrituras de sala/asientos y cine/catalogo usan transacciones. Los puntos se calculan al consultar el cliente a partir de tickets y precios de combo; el servicio impide modificar un combo usado por tickets en el uso normal.

La aplicacion representa una sala como una unica funcion sin fechas ni horarios: un asiento solo puede tener un ticket activo por sala. Las eliminaciones relacionadas pueden ser rechazadas por servicios o claves foraneas. Las operaciones usan multiples consultas, y las validaciones de negocio no son atomicas entre procesos concurrentes; use una sola instancia y no modifique simultaneamente las tablas desde phpMyAdmin. No esta preparada para produccion ni es una migracion de una base existente. Los montos utilizan `double`, siguiendo el modelo original.

## Pruebas y verificacion

`mvn test` compila, pero Maven no descubre automaticamente los archivos de `pruebas/`. Para ejecutar la prueba en memoria despues de compilar, con JDK 17 configurado:

```text
javac --release 17 -encoding UTF-8 -cp target/classes -d target/classes pruebas/PruebaCapas.java
java -cp target/classes PruebaCapas
```

La prueba manual `pruebas/PruebaJdbc.java` requiere una base **vacia y desechable** con el esquema importado y Connector/J en el classpath. Ejecute primero con argumento `crear` y luego en otro proceso con argumento `verificar`; crea y modifica registros con IDs fijos. Nunca la ejecute contra datos reales. La prueba comprueba relaciones, persistencia entre procesos, puntos, renombrado de asiento y operaciones de ticket, pero no cubre todos los errores, concurrencia ni todos los caminos de la interfaz.

Se verificaron la compilacion Maven, 85 comprobaciones en memoria y las fases JDBC de creacion y verificacion en una base temporal de MariaDB de XAMPP.
