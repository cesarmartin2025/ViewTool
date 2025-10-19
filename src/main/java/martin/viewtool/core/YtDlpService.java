/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package martin.viewtool.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.IntConsumer;

import martin.viewtool.core.io.ProcessRunner;

/**
 *
 * @author cesar
 */
public class YtDlpService {

    private final Path ytDplPath;

    public YtDlpService(Path ytDlpPath) {
        this.ytDplPath = ytDlpPath;
    }

    public int download(DownloadRequest req, Appendable out, IntConsumer onPersent) throws IOException, InterruptedException {
        List<String> commands = new ArrayList<>();
        commands.add(ytDplPath.toString());

        // Salida: título.ext en la carpeta elegida
        commands.add("-o");
        commands.add("%(title)s.%(ext)s");

        switch (req.format()) {
            case MP3 -> {
                // Extraer audio a MP3
                commands.add("-x");
                commands.add("--audio-format");
                commands.add("mp3");
                commands.add("--audio-quality");
                commands.add("0"); // mejor calidad
            }
            case MP4 -> {
                // Forzar MP4 lo mejor posible
                commands.add("-f");
                commands.add("bestvideo[ext=mp4]+bestaudio[ext=m4a]/mp4");
            }

        }

        if (req.audioOnly() && req.format() == MediaFormat.MP4) {
            // Si marcaste "Only audio" pero elegiste MP4, podrías decidir forzar M4A:
            commands.add("-x");
            commands.add("--audio-format");
            commands.add("m4a");
        }

        commands.add("--newline");
        commands.add("--progress-template");
        commands.add("%(progress._percent_str)s");

        if (req.limit() != null) {
            commands.add("--limit-rate");
            commands.add(req.limit());
        }

        if (req.m3uFile()) {
            commands.add("--print-to-file");
            commands.add("after_move:%(filepath)s");
            commands.add(req.outputDir().resolve("playlist.m3u").toString());
        }

        commands.add(req.url().toString());

        File fileDirectory = req.outputDir() != null ? req.outputDir().toFile() : null;
        return ProcessRunner.run(commands, fileDirectory, out, onPersent);
    }
}
