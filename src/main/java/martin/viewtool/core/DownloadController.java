/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package martin.viewtool.core;

import java.net.URI;
import java.nio.file.Path;
import java.util.function.IntConsumer;

/**
 * Coordinates validation and execution of media downloads via {@link YtDlpService}.
 *
 * @author cesar
 */
public class DownloadController {
    private final YtDlpService service;

    /**
     * Creates a new DownloadController backed by the given service.
     *
     * @param service the yt-dlp service used to perform downloads
     */
    public DownloadController(YtDlpService service) {
        this.service = service;
    }

    /**
     * Validates that the given URL is well-formed.
     *
     * @param raw raw URL string entered by the user
     * @throws IllegalArgumentException if the URL is invalid
     */
    public void validateUrl(String raw) {
        ValidationService.requireValidUrl(raw);
    }

    /**
     * Validates that the given speed limit string contains only digits.
     *
     * @param speed raw speed string entered by the user
     * @throws IllegalArgumentException if the format is invalid
     */
    public void validateSpeed(String speed){
        ValidationService.requireValidLimit(speed);
    }

    /**
     * Validates inputs and starts a download via yt-dlp.
     *
     * @param rawUrl    URL to download
     * @param format    desired output format
     * @param onlyAudio extract audio only when format is MP4
     * @param outputDir destination directory
     * @param out       appendable to receive yt-dlp log lines
     * @param onPersent callback receiving progress percentage (0–100)
     * @param rawLimit  bandwidth limit in megabytes, or null for unlimited
     * @param m3uFile   generate a playlist.m3u file
     * @return yt-dlp process exit code
     * @throws Exception if validation fails or the process encounters an error
     */
    public int startDownload(String rawUrl, MediaFormat format, boolean onlyAudio, Path outputDir, Appendable out, IntConsumer onPersent,String rawLimit,boolean m3uFile)
            throws Exception {
        URI url = ValidationService.requireValidUrl(rawUrl);
        String limit = ValidationService.requireValidLimit(rawLimit);
        DownloadRequest req = new DownloadRequest(url, format, onlyAudio, outputDir,limit,m3uFile);
        return service.download(req, out, onPersent);
    }
}
