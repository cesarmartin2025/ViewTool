/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package martin.viewtool.core;

import java.net.URI;
import java.nio.file.Path;

/**
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
