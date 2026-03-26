/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package martin.viewtool.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import javax.swing.DefaultListModel;
import martin.viewtool.config.PreferencesService;
import MediaSyncPolling.Media;
import MediaSyncPolling.MediaSyncPolling;

/**
 * Service that handles operations on media files:
 * listing, deleting, downloading from the network, uploading, and opening locally.
 *
 * @author cesar
 */
public class ManagementService {

    private final LibraryService libraryService;
    private final MediaService mediaService = new MediaService();

    /**
     * Creates a new ManagementService.
     *
     * @param lib   service for scanning local files
     * @param media service for managing the combined local/network media list
     * @param prefs user preferences (reserved for future use)
     */
    public ManagementService(LibraryService lib, MediaService media, PreferencesService prefs) {
        this.libraryService = lib;
    }

    /**
     * Returns a {@link DefaultListModel} populated with the names of locally filtered media files.
     *
     * @param filterType filter category: "All", "Videos", or "Audios"
     * @return list model ready to bind to a JList
     * @throws IOException if the local directory cannot be read
     */
    public DefaultListModel<String> getUpdatedLocalListModel(String filterType) throws IOException {
        List<MediaItem> files = libraryService.getFilteredFiles(filterType);

        DefaultListModel<String> model = new DefaultListModel<>();
        for (MediaItem file : files) {
            model.addElement(file.getName());
        }
        return model;
    }

    /**
     * Deletes the local copy of a media file.
     *
     * @param media the media whose local file should be deleted
     * @return {@code true} if deleted, {@code false} if the file did not exist
     * @throws Exception if an I/O error occurs
     */
    public boolean deleteMedia(Media media) throws Exception {
        File localFile = mediaService.getLocalFile(media);
        return Files.deleteIfExists(localFile.toPath());
    }

    /**
     * Downloads a network media file to the local download directory.
     *
     * @param media  the media to download
     * @param msp    sync polling component used to download
     * @param token  authentication token
     * @throws Exception if the file already exists locally or the download fails
     */
    public void downloadMedia(Media media, MediaSyncPolling msp, String token) throws Exception {
        Path destPath = mediaService.getDownloadBaseDir().resolve(media.mediaFileName);
        if (Files.exists(destPath)) {
            throw new Exception("This file is already downloaded.");
        }
        msp.download(media.id, destPath.toFile(), token);
    }
    
    /**
     * Uploads a local media file to the server.
     * Only MP3, MP4, and M4A files are accepted.
     *
     * @param file       local file to upload
     * @param youtubeUrl source URL associated with the file
     * @param msp        sync polling component used to perform the upload
     * @param token      authentication token
     * @throws Exception if the file type is not allowed or the upload fails
     */
    public void uploadMedia(File file, String youtubeUrl, MediaSyncPolling msp, String token) throws Exception {
        String name = file.getName().toLowerCase();
        if (!name.endsWith(".mp3") && !name.endsWith(".mp4") && !name.endsWith(".m4a")) {
            throw new Exception("Only MP3, MP4 or M4A files are allowed.");
        }
        
        msp.uploadFileMultipart(file, youtubeUrl, token);
    }
    
    /**
     * Opens the local copy of the given media with the system default application.
     *
     * @param media the media to open
     * @throws Exception if the file is not downloaded locally or the Desktop API is unavailable
     */
    public void openLocalFile(Media media) throws Exception {
        File localFile = mediaService.getLocalFile(media);

        if (!localFile.exists()) {
            throw new Exception("This media is not downloaded in the local directory.");
        }

        if (!java.awt.Desktop.isDesktopSupported()) {
            throw new UnsupportedOperationException("Desktop API not supported on this platform.");
        }

        java.awt.Desktop.getDesktop().open(localFile);
    }

}
