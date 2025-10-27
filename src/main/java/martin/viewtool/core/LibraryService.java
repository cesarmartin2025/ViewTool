/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package martin.viewtool.core;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author cesar
 */
public final class LibraryService {
    private final MediaLibrary mediaLibrary = new MediaLibrary();
    private final Path downloadDir;

    public LibraryService(Path downloadDir) {
        this.downloadDir = downloadDir;
    }
    
    public List<MediaItem> getFiles() throws IOException {
        List<MediaItem> allItems = mediaLibrary.scan(downloadDir);

        return allItems;
                
    }

    public List<MediaItem> getFilteredFiles(String filter) throws IOException {
        List<MediaItem> allItems = mediaLibrary.scan(downloadDir);

        return allItems.stream()
                .filter(item -> switch (filter) {
                    case "Videos" -> item.getMimeType().startsWith("video/");
                    case "Audios" -> item.getMimeType().startsWith("audio/");
                    case "Others" -> !item.getMimeType().startsWith("video/") && !item.getMimeType().startsWith("audio/");
                    default -> true;
                })
                .collect(Collectors.toList());
    }
}