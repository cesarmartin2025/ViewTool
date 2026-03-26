/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package martin.viewtool.core;

import java.nio.file.Path;
import java.time.Instant;

/**
 * Immutable representation of a local media file on disk.
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

    /**
     * Creates a new MediaItem.
     *
     * @param path      path to the file
     * @param name      file name
     * @param ext       file extension
     * @param sizeBytes file size in bytes
     * @param mimeType  MIME type
     * @param date      last modified
     */
    public MediaItem(Path path, String name, String ext, long sizeBytes, String mimeType, Instant date) {
        this.path = path;
        this.name = name;
        this.ext = ext;
        this.sizeBytes = sizeBytes;
        this.mimeType = mimeType;
        this.date = date;
    }

    /** @return absolute path to the file */
    public Path getPath() {
        return path;
    }

    /** @return file name */
    public String getName() {
        return name;
    }

    /** @return file extension */
    public String getExt() {
        return ext;
    }

    /** @return file size in bytes */
    public long getSizeBytes() {
        return sizeBytes;
    }

    /** @return MIME type of the file */
    public String getMimeType() {
        return mimeType;
    }

    /** @return last modified */
    public Instant getDate() {
        return date;
    }
}

