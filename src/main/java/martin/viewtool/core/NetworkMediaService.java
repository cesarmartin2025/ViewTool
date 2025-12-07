/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package martin.viewtool.core;

import java.io.File;
import java.nio.file.Path;
import MediaSyncPolling.Media;

/**
 *
 * @author cesar
 */
public class NetworkMediaService {
    
    public Path getDownloadBaseDir() {
    return Path.of(System.getProperty("user.home"), "ViewToolNetworkDownload");
}

public File getLocalFile(Media media) {
    Path baseDir = getDownloadBaseDir();
    Path localPath = baseDir.resolve(media.mediaFileName);
    return localPath.toFile();
}
    
}
