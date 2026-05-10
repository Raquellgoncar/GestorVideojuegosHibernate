# CheckPoint 🎮
**Gestor personal de biblioteca de videojuegos**
Raquel González Carranza — DAM2 2025-26

CheckPoint es una aplicación de escritorio desarrollada en Java que permite
llevar un registro personal de los videojuegos jugados, con valoraciones,
anotaciones privadas, marcado de favoritos y recomendaciones personalizadas
a través de la API de RAWG.

---

## Requisitos del sistema

- **Sistema operativo:** Windows 10 / 11 (x64)
- **Java:** JDK 11 o superior
- **Maven:** 3.6 o superior (incluido en NetBeans)
- **Base de datos:** MySQL Server 8.0
- **IDE recomendado:** Apache NetBeans 25

---

## Instalación

### 1. Base de datos

1. Abre **phpMyAdmin** o el cliente MySQL de tu preferencia.
2. Ejecuta el script de creación incluido en la carpeta `/sql`:
   Impórtalo desde phpMyAdmin con **Importar → Seleccionar archivo**.
3. Verifica que MySQL está activo en `localhost:3306` con usuario `root`
   y sin contraseña. Esta es la configuración por defecto de la aplicación.
   Si tu MySQL usa otro usuario o contraseña, contacta con la autora
   para obtener la configuración correcta.

### 2. Proyecto

1. Abre **NetBeans** y selecciona **File → Open Project**.
2. Navega hasta la carpeta del proyecto y ábrelo.
3. Clic derecho sobre el proyecto → **Build with Dependencies**.
   Maven descargará todas las dependencias automáticamente.
4. Verifica que termina con **BUILD SUCCESS**.

---

## Ejecución

1. Clic derecho sobre el proyecto → **Run**.
2. Se abrirá la pantalla de login de CheckPoint.
3. Usa una de las cuentas de prueba o regístrate con una cuenta nueva.

---

## Credenciales de prueba

| Usuario | Contraseña | Biblioteca |
|---------|------------|------------|
| Raquel  | A1234567   | Con datos (15 juegos) |
| Prueba  | A1234567   | Sin datos |

> La contraseña debe tener mínimo 8 caracteres y al menos una letra.

---

## Dependencias principales

| Librería | Versión | Licencia |
|----------|---------|----------|
| Hibernate Core | 5.6.15 | LGPL v2.1 |
| MySQL Connector/J | 8.0.33 | GPL v2 |
| FlatLaf | 3.4 | Apache 2.0 |
| JFreeChart | 1.5.x | LGPL v2.1 |
| org.json | 20240303 | JSON License |
| password4j | 1.8.4 | Apache 2.0 |
| Jakarta Mail | 2.0.1 | EPL 2.0 |

---

## API externa

La aplicación usa la API pública de **RAWG** (rawg.io) para búsqueda de
juegos en catálogo y recomendaciones personalizadas. Se requiere conexión
a internet para estas funcionalidades.

---

## Notas

- Los datos se almacenan localmente en MySQL. No se envía ninguna
  información a servidores externos salvo las consultas a RAWG.
- Si MySQL no está activo al arrancar, la aplicación mostrará un error
  de conexión en la consola de NetBeans.
- Las imágenes de fondo pertenecen a sus respectivos propietarios y se
  usan exclusivamente con fines académicos.