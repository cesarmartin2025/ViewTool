# ViewTool

**Author:** César Martín Pérez  
**Course:** Desarrollo de Aplicaciones Multiplataforma (DAM) – Módulo de Desarrollo de Interfaces  
**Date:** 27 October 2025  

---

## 🇪🇸 Descripción del proyecto

**ViewTool** es una aplicación de escritorio desarrollada en **Java Swing** que permite **descargar y gestionar contenidos multimedia** desde Internet utilizando la herramienta de línea de comandos **yt-dlp**.

Con la nueva actualización (Tarea DI01_3), la aplicación incorpora un gestor de biblioteca multimedia, que permite visualizar, filtrar, buscar y eliminar archivos descargados desde la propia aplicación.

---

## ⚙️ Características principales 

- **Ventana principal** → Muestra las funciones principales del programa:
    - Descargar vídeo de youtube mediante URL.
    - Elegir formato de salida mp3 o mp4.
    - Elegir si el vídeo lo quiere solo en formato audio.
    - Barra de progreso de la descarga.
    - Mostrar log del "yt-dlp.exe" una vez termina la descarga.
    - Reproducir último vídeo descargado.
- **Menú de opciones:**
  - **File > Exit** → Cierra la aplicación.
  - **Edit > Preferences** →Sustituye la vista principal, donde el usuario puede configurar:
    - Ruta para almacenar los archivos temporales.
    - Creación automática de un archivo `.m3u` para listas de reproducción descargadas.
    - Límite de velocidad de descarga.
    - Ubicación del ejecutable de `yt-dlp`.
  - **Help > About** → Muestra  información del autor y recursos utilizados.
  - **Media Management >Management Window** → Sustituye la vista principal, donde el usuario puede:
    - Ver los archivos en una lista.
    - Filtrar archivos por tipo de archivo(video,audio,other).
    - Buscar archivos concretos por su nombre en una tabla.
    - Borrar archivos seleccionándolos en la tabla.
     -**Integración con la DI Media Network (Actualización 09/12/2025)**

    La aplicación incorpora ahora un sistema que intengra la DI Media Network , permitiendo sincronizar, visualizar, descargar y subir archivos multimedia desde la red directamente en la aplicación.

**Funciones añadidas en el panel Management**:

    - Inicio de sesión y autenticación mediante el componente Media Network.
    - Sincronización y actualización automática de la biblioteca en red mediante eventos personalizados.
    - Funciones de descarga y subida de archivos a la red desde la aplicacion.
    - Funciones de borrado y apertura de archivos locales.
    - Unificación de archivos locales y de la Network en una tabla de archivos multimedia.
---

## 🧩 Estructura del proyecto

```
ViewTool/
 ├─ src/main/java/martin/viewtool/
 │   ├─ core/          → Lógica
 │   ├─ ui/            → Interfaz gráfica y controladores Swing
 │   └─ util/          → Clases auxiliares
 ├─ resources/         → Recursos utilizados
 ├─ yt-dlp.exe         → Binario externo utilizado por la aplicación
 └─ README.md
```

---

## 🧠 Recursos y referencias utilizadas

- [yt-dlp (GitHub)](https://github.com/yt-dlp/yt-dlp) – Motor principal de descarga.  
- [FFmpeg](https://ffmpeg.org/) – Conversión y extracción de audio. 

---

## 🧩 Problemas encontrados y soluciones

| Problema | Solución aplicada |
|-----------|-------------------|
| El log no se actualizaba en tiempo real durante la descarga | Se añadió una barra de progreso para mostrar al usuario la salida en tiempo real |
| El `.m3u` no se creaba correctamente con `yt-dlp` | Se integró el parámetro `--print-to-file` con la ruta del archivo `.m3u`. |
| El botón “Play last video” no encontraba archivos en algunas rutas | Se añadió un filtro por extensiones `.mp4`, `.mp3` y `.m4a`. |
| Los archivos no se actualizaban al cambiar de filtro | Se creó un método utilizando un botón para actualizar listas y tablas. |
| La eliminación de archivos no mostraba confirmación |Se utilizó la clase Alerts antes de ejecutar MediaLibrary.delete(). |
| Las fechas se mostraban con formato poco legible (Instant) | Se creó un DateTimeFormatter para mostrar un formato legible para las fechas. |
| Al cambiar el modelo de la JTable desde el Designer se perdían configuraciones |	creamos un modelo de Tabla usando la extension AbstractTableModel y un método de ajustes de columnas antes de inicializar la tabla.
| La aplicación no integraba archivos locales con los de la Network |	Se implementó un sistema de fusión de listas  unificando datos locales y remotos.
| La tabla no mostraba correctamente si un archivo estaba en local, en la Network o en ambas | Se añadió una columna calculada que determina el estado comprobando existencia en disco y valor del media.id.
| Los archivos locales movidos manualmente no se detectaban al iniciar la aplicación| Se añadió un escaneo automático del directorio para modificar la lista local.
|La tabla perdía datos al sincronizar con el componente| Se implementó un método que añade solo las novedades detectadas por los eventos del componente.
|Limitar los archivos que se pueden subir al servidor para que solo fuesen .mp3, .mp4 o .m4a| Se implementó un filtro de extensiones mediante name.endswith().
|El usuario debía escribir la URL del vídeo después de elegir el archivo que quería subir| Se creó un cuadro de entrada para que el usuario pudiese introducir la URL del vídeo.




---

## 📋 Requisitos técnicos

- Java 17 o superior  
- Sistema operativo Windows (probado en Windows 10)  
- `yt-dlp.exe` y `ffmpeg.exe` accesibles en el sistema  

---

## 🏁 Ejecución

Compilar y ejecutar con:
```bash
java -jar ViewTool.jar
```

o desde el IDE.

---

## 09/12/2025 – Actualización MediaSyncPolling

- Se añadió la sincronización de DI Media Network.
- Se añadió una tabla de medios unificada (local y de red).
- Se añadieron funciones de descarga y carga mediante el componente MediaSyncPolling.
- Se agregó la función de eliminación y apertura para medios locales.
- Se agregó la actualización automática de la tabla de medios mediante eventos personalizados.
 ---  
## 🧾 Licencia y créditos

Proyecto educativo desarrollado por **César Martín Pérez** para el módulo **Desarrollo de Interfaces(DAM)**.  
Todos los derechos de las librerías y recursos externos pertenecen a sus respectivos autores.

---

# 🇬🇧 English Version 🇬🇧
## Project description

**ViewTool** is a desktop application developed in **Java Swing** that allows users to **download and manage multimedia content** from the Internet using **yt-dlp**.

The latest update (Assignment DI01_2) introduces a Media Library Manager, enabling users to visualize, filter, search, and delete downloaded files directly from the application.


---

## Main features
- **Main Window:** → Displays the program's main functions:
    - Download YouTube video via URL.
    - Choose MP3 or MP4 output format.
    - Choose whether you want the video in audio only.
    - Download progress bar.
    - Display the "yt-dlp.exe" log once the download is complete.
    - Play the last downloaded video.
- **Menu structure:**
  - **File > Exit** → Closes the program.  
  - **Edit > Preferences** → Swaps to a `JPanel` view where the user can configure:
    - Temporary files directory.  
    - Automatic creation of `.m3u` playlist files.  
    - Download speed limit.  
    - Location of the `yt-dlp` binaries.  
  - **Help > About** → author and project information.
  - **Media Management > Management Window** → Replaces the main view, where the user can:
    - View files in a list.
    - Filter files by file type (video, audio, other).
    - Search for specific files by name in a table.
    - Delete files by selecting them in the table.  
---

## 🧩 Project Structure

```
ViewTool/
 ├─ src/main/java/martin/viewtool/
 │   ├─ core/          → logic
 │   ├─ ui/            → GUI and Swing controllers
 │   └─ util/          → Aux classes
 ├─ resources/         → Resources
 ├─ yt-dlp.exe         → Executable used for the app
 └─ README.md
```

---

## References

- [yt-dlp](https://github.com/yt-dlp/yt-dlp)  
- [FFmpeg](https://ffmpeg.org/)  

---

## Known issues and solutions

| Issue | Solution |
|--------|-----------|
| Log didn’t update in real-time | Added 'ProgressBar' for to show the download progress |
| `.m3u` file not created | Implemented `--print-to-file` with output path. |
| Play button didn’t find files | Filtered valid media extensions. |
| Files weren't updating when changing filters | A method was created using a button to update lists and tables. |
| File deletion didn't display a confirmation | The Alerts class was used before running MediaLibrary.delete(). |
| Dates were displayed in an unreadable format (Instant) | A DateTimeFormatter was created to display a readable format for dates. |
| Changing the JTable model from the Designer caused settings to be lost | We created a Table model using the AbstractTableModel extension and a column setting method before initializing the table. |
---

## Technical requirements

- Java 17 or later  
- Windows OS (tested on Windows 10)  
- `yt-dlp.exe` and `ffmpeg.exe` available in system PATH  

---

## Run

```bash
java -jar ViewTool.jar
```

or

```bash
java -cp dist/ViewTool.jar martin.viewtool.ui.ViewToolApp
```

---

## 2025-12-09 – MediaSyncPolling update

-Added support for DI Media Network synchronization.
-Added unified media table (local + network).
-Added download/upload functions using the MediaSyncPolling component.
-Added delete function for local medias.
-Added “open local file” action.
-Added automatic refresh of media table via custom events.


## License

Educational project by **César Martín Pérez** for the **Interface development** program (DAM).  
All third-party resources belong to their respective authors.

---
