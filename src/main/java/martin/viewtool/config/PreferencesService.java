/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package martin.viewtool.config;

import java.nio.file.Path;
import java.util.prefs.Preferences;

/**
 *
 * @author cesar
 */
public class PreferencesService {

    private static final String KEY_YTDLP = "ytdlp.path";
    private static final String KEY_OUTPUT = "output.dir";
    private static final String KEY_LIMIT_RATE = "limit_rate";

    

    private final Preferences prefs = Preferences.userRoot().node("martin.viewtool");

    public void setYtDlpPath(Path p) {
        prefs.put(KEY_YTDLP, p.toString());
    }
    
    public String getLimitSpeed() {
        return prefs.get(KEY_LIMIT_RATE, ""); // vacío = sin límite
    }

    public void setLimitSpeed(String raw) {
        prefs.put(KEY_LIMIT_RATE, raw != null ? raw : "");
    }

  

    public void setOutputDir(Path p) {
        prefs.put(KEY_OUTPUT, p.toString());
    }

    public Path getYtDlpPath() {
        return Path.of(prefs.get(KEY_YTDLP, "yt-dlp"));
    }

    public Path getOutputDir() {
        // Ruta por defecto → user.home/ViewToolDownloads
        Path defaultPath = Path.of(System.getProperty("user.home"), "ViewToolDownloads");

        // Leer de preferencias (o usar la ruta por defecto)
        Path path = Path.of(prefs.get(KEY_OUTPUT, defaultPath.toString()));

        // Crear carpeta si no existe
        if (!java.nio.file.Files.exists(path)) {
            try {
                java.nio.file.Files.createDirectories(path);
            } catch (Exception e) {
               System.err.print("No se pudo crear el directorio de salida: " + e.getMessage());
            }
        }

        return path;

    }
}
