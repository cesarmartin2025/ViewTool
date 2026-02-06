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
 
## 06/02/2026 - Actualización Usabilidad

## Colores
Utiliza una paleta de colores minimalista. Simula una estética retro y funcional, basada principalmente en interfaces clásicas de programas de escritorio, intentando buscar el foco en la claridad y la legibilidad. De forma puntual, se utilizan colores más llamativos para dirigir la atención del usuario en acciones concretas. Sin embargo, los colores predominantes en la aplicación son el blanco y el negro, buscando esa apariencia minimalista y coherente.

## Iconos
### JFrame principal
Iconos según el panel en el que esté el usuario:
- **Home**: si está en un panel distinto al principal.
- **Setting**: si está en un panel distinto al de ajustes (preferences).

### Panel Login
- Se han añadido iconos que representan la información que debe escribir el usuario en los campos de entrada.

### Panel Main
- Icono de Youtube para que sea más identificativo el campo de la URL.
- Iconos asociados a audio o video para los formatos MP3 y MP4.
Se ha optado por mantener los botones principales sin iconos, ya que no aportaban claridad adicional y sobrecargaban visualmente el panel.

### Panel Management
- Iconos representativos de la lista local y de la tabla de la base de datos en red.
- Iconos de acción, sustituyendo los botones tradicionales por iconos de cada acción en la tabla.

### Panel Preferences
- Iconos para las acciones disponibles que existen en el panel.
- Imagen que comunica visualmente que el panel se encuentra aún en desarrollo en cuanto a funcionalidad.

## Texto
En todo el programa hay un texto simple y claro, evitando frases largas o técnicas que el usuario no pueda comprender. La tipografía utilizada es la **SEGOE UI**, porque es la fuente estándar de aplicaciones de escritorio en Windows. Con esta tipografía se busca una coherencia con el estilo principal retro el cual está diseñado el programa. Se usa puntualmente negrita para destacar acciones no permitidas en la aplicación (además de un color llamativo). También en el uso de checkboxes para que el usuario pueda reconocer si esa acción está activada o no y en los radiobuttons para que identifique cuál de los botones está activo.

## Distribución de los componentes
La distribución de componentes se ha diseñado siguiendo los principios de diseños de interfaces de usuario vistos en la unidad, priorizando la legibilidad y el criterio de mínima sorpresa. Los elementos de cada pantalla se organizan en agrupaciones funcionales diferenciadas para que el usuario pueda identificar qué componentes pertenecen a una misma función. En todo el programa se respeta el flujo de lectura natural (de arriba a abajo y de izquierda a derecha), situando las acciones más relevantes al inicio de los paneles, facilitando el acceso al usuario. La integración de todos los paneles dentro de un JFrame único contribuye a mantener una consistencia estructural y evita cambios bruscos de contexto. De esta manera, es más fácil conseguir una coherencia visual y minimalista general en toda la aplicación.

## Affordance

- Se ha implementado un **método genérico** que modifica el cursor al pasar el ratón por encima de algunos componentes interactivos, para indicar que es clicable y facilitando el reconocimiento de las acciones disponibles.
Además, se han añadido **tooltips descriptivos** en aquellos botones que utilizan sólo iconos. De esta manera, con esta explicación breve y directa de la acción asociada a cada botón, se asegura que el significado de la interacción sea totalmente claro evitando así errores de entendimiento.
- También se ha implementado en algunos **botones** el cambio de estado para que se muestren desactivados cuando la aplicación no puede realizar la acción que requiere el usuario en ese momento.
- Se ha seleccionado una serie de iconos coherentes y representativos de las acciones asociadas, de modo que cada icono refuerza de manera gráfica su función y facilita la comprensión de la acción por parte del usuario, obviando en muchos casos la necesidad de texto visual en forma de apoyo.

## Feedback

- Se han añadido mensajes de error o de éxito en casos en los que la acción puede fallar, evitando JDialogs e introduciendo JLabels que aparecen y desaparecen indicando al usuario qué error está sucediendo y por qué. 
- Se ha mantenido la barra de progreso de descarga cuando se usa la herramienta yt-dlp.
- Se han implementado cambios automáticos en la tabla y lista si la acción del usuario modifica alguna de estas, como por ejemplo: colores para diferenciar si un archivo está en la nube, en local o en ambos lugares.
- Se han mantenido los JDialogs para errores importantes que el usuario necesita prestar atención y saber lo que ocurre.
- Se ha añadido un panel de carga al inicio de la aplicación, indicando al usuario que la aplicación está cargando y que necesita esperar para poder utilizarla.

## Restricciones

Además de mantener anteriormente las restricciones ya impuestas:

- Se han añadido restricciones visuales como botones deshabilitados para acciones que no se pueden ejecutar.
- Se han añadido restricciones funcionales cuando el usuario intenta realizar una acción que no es posible ejecutarla (No permitir borrar/descargar/abrir si no está seleccionado el archivo, no permitir borrar/abrir un archivo que está en la nube...).

## Otras mejoras de usabilidad
### Navegación y estructura

- Agrupación de las opciones del menubar, simplificando al máximo la cantidad de menús y elementos de menú.
- Encuadre del panel Login en el JFrame, adaptando la ventana al tamaño del panel.
- Combinación del panel main y panel management en la página principal del programa.
- Eliminación del scroll horizontal.
- Cambios de apariencia en JDialogs para respetar el estilo de la aplicación.
    
### Rendimiento

- Carga automática de la lista y la tabla sin intervención del usuario nada más iniciar la aplicación.
- Mejora de rendimiento de la aplicación, añadiendo un método de paginación en la tabla.
- Variación del tiempo de polling del mediaSyncPolling después de iniciarlo.
- Panel de carga al iniciar la aplicación.
    
### Gestión y visualización

- Actualización de la lista automática al seleccionar en el combobox el tipo de archivo que muestra.
- Modificación en la lista para que del directorio sólo muestre archivos de video o audio.
- Información en la tabla del nombre del usuario asociado al archivo subido en la nube.
- Información de la carpeta en la cual se han descargado los archivos.
- Información en la tabla de los resultados letra por letra de manera automática.
    
### Mejora del flujo de uso

- Eliminación de JDialogs redundantes en toda la aplicación.
- Introducción de labels informativos sustituyendo a los JDialogs para mostrar información relevante al usuario.

### Claridad visual

- Modificación checkbox para que aparezca en negrita si está seleccionado.

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
   
    - **Integration with the DI Media Network (Updated 09/12/2025)**

The application now includes a system that integrates with the DI Media Network, allowing you to synchronize, view, download, and upload media files from the network directly within the application.

**Added features in the Management panel:**

- Login and authentication via the Media Network component.

- Automatic synchronization and updating of the network library via custom events.

- Download and upload functions to the network from within the application.

- Local file deletion and opening functions.

- Unification of local and network files into a single media file table.
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

| Problem | Applied Solution |
|---------|------------------|
| The log was not updating in real time during the download | A progress bar was added to display real-time output to the user. |
| The `.m3u` file was not being created correctly with `yt-dlp` | The `--print-to-file` parameter was integrated with the `.m3u` file path. |
| The “Play last video” button could not find files in some paths | A filter for `.mp4`, `.mp3`, and `.m4a` extensions was added. |
| Files were not refreshing when changing the filter | A method was created using a button to refresh lists and tables. |
| File deletion did not show a confirmation dialog | The Alerts class was used before executing `MediaLibrary.delete()`. |
| Dates were displayed in a barely readable format (Instant) | A `DateTimeFormatter` was created to display human-readable dates. |
| Changing the JTable model from the Designer caused configuration loss | A custom TableModel using `AbstractTableModel` was created, along with a method for column adjustments before initializing the table. |
| The application did not integrate local files with those from the Network | A list-merging system was implemented to unify local and remote data. |
| The table did not correctly show whether a file was local, on the Network, or in both | A calculated column was added, determining the state by checking disk existence and `media.id`. |
| Local files moved manually were not detected when starting the application | An automatic directory scan was added to update the local file list. |
| The table lost data when synchronizing with the component | A method was implemented to add only newly detected items from the component events. |
| Limit uploads to only `.mp3`, `.mp4`, or `.m4a` files | An extension filter was implemented using `name.endswith()`. |
| The user had to type the video URL after choosing the file to upload | An input dialog was added so the user could enter the video URL. |

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

---

## 06/02/2026 – Usability Update

## Colors
A minimalist color palette is used. It simulates a retro and functional aesthetic, mainly based on classic desktop application interfaces, aiming to focus on clarity and readability. Occasionally, more striking colors are used to draw the user’s attention to specific actions. However, the predominant colors in the application are black and white, reinforcing a minimalist and consistent appearance.

## Icons
### Main JFrame
Icons depending on the panel the user is currently in:
- **Home**: when the user is on a panel other than the main one.
- **Settings**: when the user is on a panel other than the settings (preferences) panel.

### Login Panel
- Icons have been added to represent the information the user must enter in the input fields.

### Main Panel
- YouTube icon to make the URL field more easily identifiable.
- Icons associated with audio or video for MP3 and MP4 formats.  
The main buttons have intentionally been kept without icons, as they did not provide additional clarity and visually overloaded the panel.

### Management Panel
- Representative icons for the local list and the network database table.
- Action icons, replacing traditional buttons with icons for each action within the table.

### Preferences Panel
- Icons for the available actions within the panel.
- An image that visually communicates that this panel is still under development in terms of functionality.

## Text
Throughout the application, simple and clear text is used, avoiding long or overly technical phrases that the user may not understand. The typography used is **SEGOE UI**, as it is the standard font for desktop applications on Windows. This choice seeks consistency with the overall retro style on which the application is designed. Bold text is used selectively to highlight actions that are not allowed within the application (in addition to a striking color). Bold is also used in checkboxes so the user can easily recognize whether an action is enabled or not, and in radio buttons to identify which option is currently active.

## Component Layout
The component layout has been designed following the user interface design principles studied in the unit, prioritizing readability and the principle of minimum surprise. The elements on each screen are organized into distinct functional groups so that the user can easily identify which components belong to the same function. Throughout the application, the natural reading flow (top to bottom and left to right) is respected, placing the most relevant actions at the beginning of each panel to facilitate user access. Integrating all panels within a single JFrame helps maintain structural consistency and avoids abrupt context changes. This approach makes it easier to achieve overall visual and minimalist coherence across the entire application.

## Affordance

- A **generic method** has been implemented that changes the cursor when hovering over certain interactive components, indicating that they are clickable and facilitating recognition of available actions.  
  Additionally, **descriptive tooltips** have been added to buttons that use icons only. With these brief and direct explanations of the action associated with each button, the meaning of the interaction is made completely clear, thus avoiding misunderstandings.
- A state change has also been implemented in some **buttons**, so they appear disabled when the application cannot perform the requested action at that moment.
- A set of coherent and representative icons has been selected for the associated actions, so that each icon graphically reinforces its function and facilitates user understanding, often eliminating the need for supporting text.

## Feedback

- Error or success messages have been added in cases where an action may fail, avoiding JDialogs and instead introducing JLabels that appear and disappear, informing the user of what error is occurring and why.
- The download progress bar has been retained when using the yt-dlp tool.
- Automatic updates have been implemented in the table and list when a user action modifies them, such as using colors to differentiate whether a file is in the cloud, locally stored, or in both locations.
- JDialogs have been retained for critical errors that require the user’s attention and awareness.
- A loading panel has been added at application startup, informing the user that the application is loading and that they must wait before using it.

## Restrictions

In addition to maintaining the previously imposed restrictions:

- Visual restrictions have been added, such as disabled buttons for actions that cannot be executed.
- Functional restrictions have been added when the user attempts to perform an action that is not possible (e.g., not allowing delete/download/open actions if no file is selected, not allowing delete/open actions on a file that is stored in the cloud, etc.).

## Other Usability Improvements
### Navigation and Structure

- Grouping of menubar options, minimizing the number of menus and menu items as much as possible.
- Centering the Login panel within the JFrame, adapting the window size to the panel.
- Combining the main panel and management panel on the main application screen.
- Removal of horizontal scrolling.
- Appearance changes in JDialogs to respect the application’s style.

### Performance

- Automatic loading of the list and table without user intervention as soon as the application starts.
- Application performance improvements by adding a pagination method to the table.
- Adjustment of the mediaSyncPolling polling interval after initialization.
- Loading panel at application startup.

### Management and Visualization

- Automatic list update when selecting the file type to display from the combobox.
- Modification of the list so that only video or audio files are shown from the directory.
- Displaying in the table the username associated with the file uploaded to the cloud.
- Displaying information about the folder where files have been downloaded.
- Displaying table results character by character, without requiring the user to press the search button.

### Improved User Flow

- Removal of redundant JDialogs throughout the application.
- Addition of informational labels to replace JDialogs when displaying relevant information to the user.

### Visual Clarity

- Modification of checkboxes so that they appear in bold when selected.
  
---



## License

Educational project by **César Martín Pérez** for the **Interface development** program (DAM).  
All third-party resources belong to their respective authors.

---
