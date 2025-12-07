/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package martin.viewtool.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

/**
 *
 * @author cesar
 */
public final class MediaLibrary {

    public List<MediaItem> scan(Path dir) throws IOException {
        if (!Files.isDirectory(dir)) 
            return List.of();
        try (Stream<Path> s = Files.list(dir)) {
            return s.filter(Files::isRegularFile)
                    .map(this::tosafeFile)
                    .filter(Objects::nonNull)
                    .sorted(Comparator.comparing(MediaItem::getDate).reversed())
                    .toList();
        }
    }

    private MediaItem tosafeFile(Path p) {
        try {
            String name = p.getFileName().toString();
            String ext = extOf(name);
            long size = Files.size(p);
            String mime = Optional.ofNullable(Files.probeContentType(p)).orElse(mimeByExt(ext));
            BasicFileAttributes a = Files.readAttributes(p, BasicFileAttributes.class);
            Instant date = a.lastModifiedTime() != null ? a.lastModifiedTime().toInstant()
                           : (a.creationTime() != null ? a.creationTime().toInstant() : Instant.EPOCH);
            return new MediaItem(p, name, ext, size, mime, date);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean delete(MediaItem file) throws IOException {
        return Files.deleteIfExists(file.getPath());
    }
    
    

    private static String extOf(String name) {
        int i = name.lastIndexOf('.');
        return (i > 0 && i < name.length() - 1) ? name.substring(i + 1).toLowerCase() : "";
    }

    private static String mimeByExt(String ext) {
        return switch (ext) {
            case "mp4","m4v","mov" -> "video/mp4";
            case "mp3" -> "audio/mpeg";
            case "m4a","aac" -> "audio/mp4";
            case "webm" -> "video/webm";
            case "wav" -> "audio/wav";
            default -> "application/octet-stream";
        };
    }
    
    
    
    
}

