/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package martin.viewtool.config;

import java.nio.file.Path;
import java.util.prefs.Preferences;

/**
 * Reads and writes user preferences
 * @author cesar
 */
public class PreferencesService {

    private static final String KEY_YTDLP = "ytdlp.path";
    private static final String KEY_OUTPUT = "output.dir";
    private static final String KEY_LIMIT_RATE = "limit_rate";

    private final Preferences prefs = Preferences.userRoot().node("martin.viewtool");

    /**
     * Saves the path to the yt-dlp executable.
     *
     * @param p path to the yt-dlp binary
     */
    public void setYtDlpPath(Path p) {
        prefs.put(KEY_YTDLP, p.toString());
    }
    
    /**
     * Returns the saved download speed limit, or an empty string if not set.
     *
     * @return speed limit string
     */
    public String getLimitSpeed() {
        return prefs.get(KEY_LIMIT_RATE, "");
    }

    /**
     * Saves the download speed limit.
     *
     * @param raw raw speed string
     */
    public void setLimitSpeed(String raw) {
        prefs.put(KEY_LIMIT_RATE, raw != null ? raw : "");
    }

  

    /**
     * Saves the output directory for downloads.
     *
     * @param p path to the desired output directory
     */
    public void setOutputDir(Path p) {
        prefs.put(KEY_OUTPUT, p.toString());
    }

    /**
     * Returns the saved yt-dlp path, defaulting to "yt-dlp"
     *
     * @return path to the yt-dlp executable
     */
    public Path getYtDlpPath() {
        return Path.of(prefs.get(KEY_YTDLP, "yt-dlp"));
    }

    /**
     * Returns the saved output directory, creating it if it does not exist.
     *
     * @return path to the output directory
     */
    public Path getOutputDir() {
       
        Path defaultPath = Path.of(System.getProperty("user.home"), "ViewToolDownloads");
        
        Path path = Path.of(prefs.get(KEY_OUTPUT, defaultPath.toString()));

        if (!java.nio.file.Files.exists(path)) {
            try {
                java.nio.file.Files.createDirectories(path);
            } catch (Exception e) {
               System.err.print("The output directory could not be created.: " + e.getMessage());
            }
        }

        return path;

    }
}
