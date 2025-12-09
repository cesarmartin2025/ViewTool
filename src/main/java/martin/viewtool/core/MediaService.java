/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package martin.viewtool.core;

import java.io.File;
import java.nio.file.Path;
import MediaSyncPolling.Media;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author cesar
 */
public class MediaService {

    private List<Media> networkMediaAll = new ArrayList<>();

    public MediaService() {
    }

    public List<Media> getNetworkMediaAll() {
        return new ArrayList<>(networkMediaAll);
    }

    public Path getDownloadBaseDir() {
        Path downloadedPath = Path.of(System.getProperty("user.home"), "ViewToolNetworkDownload");
        File pathToFile = downloadedPath.toFile();
        if (!pathToFile.exists()) {
            pathToFile.mkdirs();
        }
        return downloadedPath;

    }

    public File getLocalFile(Media media) {
        Path downloadedPath = getDownloadBaseDir();
        Path localPath = downloadedPath.resolve(media.mediaFileName);
        return localPath.toFile();
    }

    public void addNewMediaNetwork(List<Media> newMedia) {
        for (Media media : newMedia) {
            if (media == null) {
                continue;
            }

            boolean exists = false;
            for (Media file : networkMediaAll) {
                if (file != null && file.id == media.id && media.id > 0) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                networkMediaAll.add(media);
            }
        }
    }

    public List<Media> createMediaListCombined() {
        List<Media> mediaListCombined = new ArrayList<>(networkMediaAll);
        Set<String> networkNames = new HashSet<>();
        for (Media media : networkMediaAll) {
            if (media != null && media.mediaFileName != null) {
                networkNames.add(media.mediaFileName);
            }
        }
        File downloadDir = getDownloadBaseDir().toFile();
        File[] localFiles = downloadDir.listFiles();

        if (localFiles != null) {
            for (File file : localFiles) {
                if (file.isFile()) {
                    String name = file.getName();

                    // This condition check if 
                    if (!networkNames.contains(name)) {
                        Media media = new Media();
                        media.id = 0;
                        media.mediaFileName = name;
                        media.userId = -1;
                        media.downloadedFromUrl = "Unknown";
                        mediaListCombined.add(media);
                    }
                }
            }
        }
        return mediaListCombined;
    }

    public List<Media> filterMedia(String textField, List<Media> listMedia) {
        List<Media> mediaResult = new ArrayList<>();

        String textLowerCase = textField.toLowerCase();

        for (Media media : listMedia) {
            if (media != null && media.mediaFileName != null) {
                if (media.mediaFileName.toLowerCase().contains(textLowerCase)) {
                    mediaResult.add(media);
                }
            }
        }
        return mediaResult;
    }

}
