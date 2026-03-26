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
 * Manages the in-memory list of network media and provides utilities for
 * resolving local file paths and combining local and network media lists.
 *
 * @author cesar
 */
public class MediaService {

    private List<Media> networkMediaAll = new ArrayList<>();

    public MediaService() {
    }

    /**
     * Returns a copy of all network media loaded.
     *
     * @return list of network {@link Media} objects
     */
    public List<Media> getNetworkMediaAll() {
        return new ArrayList<>(networkMediaAll);
    }

    /**
     * Returns the base download directory, creating it if it does not exist.
     * Defaults to {@code ~/ViewToolDownloads}.
     *
     * @return path to the download directory
     */
    public Path getDownloadBaseDir() {
        Path downloadedPath = Path.of(System.getProperty("user.home"), "ViewToolDownloads");
        File pathToFile = downloadedPath.toFile();
        if (!pathToFile.exists()) {
            pathToFile.mkdirs();
        }
        return downloadedPath;

    }

    /**
     * Resolves the expected local {@link File} for the given media.
     *
     * @param media the media whose local path is needed
     * @return file object pointing to the local copy
     */
    public File getLocalFile(Media media) {
        Path downloadedPath = getDownloadBaseDir();
        Path localPath = downloadedPath.resolve(media.mediaFileName);
        return localPath.toFile();
    }

    /**
     * Adds new media from the network to the in-memory list, skipping duplicates by ID.
     *
     * @param newMedia list of media received from the API
     */
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

    /**
     * Builds a combined list of network media plus any local-only files not present on the network.
     * Local-only entries are created with {@code id=0} and {@code userId=-1}.
     *
     * @return combined media list
     */
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

    /**
     * Filters a media list by file name.
     *
     * @param textField search text entered by the user
     * @param listMedia source list to filter
     * @return filtered list containing only items whose file name contains the search text
     */
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
