/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package martin.viewtool.core;

import java.net.URI;
import java.nio.file.Path;

/**
 * Holds all parameters needed to start a yt-dlp download.
 *
 * @param url       validated URI to download from
 * @param format    desired output format ({@link MediaFormat#MP3} or {@link MediaFormat#MP4})
 * @param audioOnly if {@code true} and format is MP4, extracts audio only as M4A
 * @param outputDir directory where the downloaded file will be saved
 * @param limit     bandwidth limit in megabytes, or {@code null} for unlimited
 * @param m3uFile   if {@code true}, a playlist.m3u file is generated
 *
 * @author cesar
 */
public record DownloadRequest(
        URI url,
        MediaFormat format,
        boolean audioOnly,
        Path outputDir,
        String limit,
        boolean m3uFile
) {}
