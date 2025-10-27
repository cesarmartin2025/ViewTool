/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package martin.viewtool.core;

import java.nio.file.Path;
import java.time.Instant;

/**
 *
 * @author cesar
 */
public final class MediaItem {

   
    private final Path path;
    private final String name;
    private final String ext;
    private final long sizeBytes;
    private final String mimeType;
    private final Instant date; 

    public MediaItem(Path path, String name, String ext, long sizeBytes, String mimeType, Instant date) {
        this.path = path;
        this.name = name;
        this.ext = ext;
        this.sizeBytes = sizeBytes;
        this.mimeType = mimeType;
        this.date = date;
    }
    
     public Path getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public String getExt() {
        return ext;
    }

    public long getSizeBytes() {
        return sizeBytes;
    }

    public String getMimeType() {
        return mimeType;
    }

    public Instant getDate() {
        return date;
    }
}

