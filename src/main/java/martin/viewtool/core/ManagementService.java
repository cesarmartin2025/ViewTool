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
 *
 * @author cesar
 */
public class ManagementService {

    private final LibraryService libraryService;
    private final MediaService mediaService = new MediaService();

    public ManagementService(LibraryService lib, MediaService media, PreferencesService prefs) {
        this.libraryService = lib;
    }

    public DefaultListModel<String> getUpdatedLocalListModel(String filterType) throws IOException {
        List<MediaItem> files = libraryService.getFilteredFiles(filterType);

        DefaultListModel<String> model = new DefaultListModel<>();
        for (MediaItem file : files) {
            model.addElement(file.getName());
        }
        return model;
    }

    public boolean deleteMedia(Media media) throws Exception {
        File localFile = mediaService.getLocalFile(media);
        return Files.deleteIfExists(localFile.toPath());
    }

    public void downloadMedia(Media media, MediaSyncPolling msp, String token) throws Exception {
        Path destPath = mediaService.getDownloadBaseDir().resolve(media.mediaFileName);
        if (Files.exists(destPath)) {
            throw new Exception("This file is already downloaded.");
        }
        msp.download(media.id, destPath.toFile(), token);
    }
    
    public void uploadMedia(File file, String youtubeUrl, MediaSyncPolling msp, String token) throws Exception {
        String name = file.getName().toLowerCase();
        if (!name.endsWith(".mp3") && !name.endsWith(".mp4") && !name.endsWith(".m4a")) {
            throw new Exception("Only MP3, MP4 or M4A files are allowed.");
        }
        
        msp.uploadFileMultipart(file, youtubeUrl, token);
    }
    
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
