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
 * Service that provides access to local media files through a {@link MediaLibrary}.
 *
 * @author cesar
 */
public final class LibraryService {
    private final MediaLibrary mediaLibrary = new MediaLibrary();
    private final Path downloadDir;

    /**
     * Creates a LibraryService that scans the given directory.
     *
     * @param downloadDir local directory containing downloaded media files
     */
    public LibraryService(Path downloadDir) {
        this.downloadDir = downloadDir;
    }

    /**
     * Returns all media files found in the download directory.
     *
     * @return list of all {@link MediaItem} objects
     * @throws IOException if an I/O error occurs
     */
    public List<MediaItem> getFiles() throws IOException {
        List<MediaItem> allItems = mediaLibrary.scan(downloadDir);

        return allItems;
                
    }

    /**
     * Returns media files filtered by type.
     *
     * @param filter one of "All", "Videos", "Audios", or any other value to skip filtering
     * @return filtered list of {@link MediaItem} objects
     * @throws IOException if an I/O error occurs
     */
    public List<MediaItem> getFilteredFiles(String filter) throws IOException {
        List<MediaItem> allItems = mediaLibrary.scan(downloadDir);

        return allItems.stream()
                .filter(item -> switch (filter) {
                     case "All" -> item.getMimeType().startsWith("video/") || item.getMimeType().startsWith("audio/");
                    case "Videos" -> item.getMimeType().startsWith("video/");
                    case "Audios" -> item.getMimeType().startsWith("audio/");
                    default -> true;
                })
                .collect(Collectors.toList());
    }
}