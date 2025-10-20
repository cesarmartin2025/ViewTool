# ViewTool

**Author:** César Martín Pérez  
**Course:** Desarrollo de Aplicaciones Multiplataforma (DAM) – Módulo de Servicios y Procesos  
**Date:** 27 October 2025  

---

## 🇪🇸 Descripción del proyecto

**ViewTool** es una aplicación de escritorio desarrollada en **Java Swing** que permite **descargar y gestionar contenidos multimedia** desde Internet utilizando la herramienta de línea de comandos **yt-dlp**.  

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

## 🧾 Licencia y créditos

Proyecto educativo desarrollado por **César Martín Pérez** para el módulo **Desarrollo de Interfaces(DAM)**.  
Todos los derechos de las librerías y recursos externos pertenecen a sus respectivos autores.

---

# 🇬🇧 English Version 🇬🇧
## Project description

**ViewTool** is a desktop application developed in **Java Swing** that allows users to **download and manage multimedia content** from the Internet using **yt-dlp**.  


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

## License

Educational project by **César Martín Pérez** for the **Interface development** program (DAM).  
All third-party resources belong to their respective authors.

---
