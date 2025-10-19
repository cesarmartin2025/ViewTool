/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package martin.viewtool.core;

import java.net.URI;
import java.nio.file.Path;
import java.util.function.IntConsumer;

/**
 *
 * @author cesar
 */
public class DownloadController {
    private final YtDlpService service;

    public DownloadController(YtDlpService service) {
        this.service = service;
    }

    public void validateUrl(String raw) {
        ValidationService.requireValidUrl(raw); 
    }
    
    public void validateSpeed(String speed){
        ValidationService.requireValidLimit(speed);
    }
    
    


    public int startDownload(String rawUrl, MediaFormat format, boolean onlyAudio, Path outputDir, Appendable out, IntConsumer onPersent,String rawLimit)
            throws Exception {
        URI url = ValidationService.requireValidUrl(rawUrl);
        String limit = ValidationService.requireValidLimit(rawLimit);
        DownloadRequest req = new DownloadRequest(url, format, onlyAudio, outputDir,limit);
        return service.download(req, out, onPersent);
    }
}