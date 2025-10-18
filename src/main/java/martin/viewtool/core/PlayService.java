/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package martin.viewtool.core;

import java.awt.Desktop;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

/**
 *
 * @author cesar
 */
public class PlayService {

    public void playLastDownloaded(Path downloadDir) throws IOException {
        if (!Files.isDirectory(downloadDir)) {
            throw new IOException("El directorio de descargas no existe: " + downloadDir);
        }

        try (Stream<Path> files = Files.list(downloadDir)) {
            Optional<Path> newest = files
                    .filter(Files::isRegularFile)
                    .filter(p -> {
                        String name = p.getFileName().toString().toLowerCase();
                        return name.endsWith(".mp4") || name.endsWith(".mp3") || name.endsWith(".m4a");
                    })
                    .max(Comparator.comparingLong(p -> p.toFile().lastModified()));

            if (newest.isPresent()) {
                Path file = newest.get();
                System.out.println("Reproduciendo: " + file);
                Desktop.getDesktop().open(file.toFile());
            } else {
                throw new IOException("No se encontraron vídeos o audios en " + downloadDir);
            }
        }
    }
}