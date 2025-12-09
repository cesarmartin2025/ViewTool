/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package martin.viewtool.core;

import java.util.ArrayList;
import java.util.List;
import MediaSyncPolling.Media;

/**
 *
 * @author cesar
 */
public class MediaFilterService {
    
    public MediaFilterService(){}
    
    public List<MediaSyncPolling.Media> filterMedia(String textField,List<Media> listMedia) {
        List<Media> mediaResult = new ArrayList<>();

        for (MediaSyncPolling.Media media : listMedia) {
            if (media != null && media.mediaFileName != null) {
                if (media.mediaFileName.toLowerCase().contains(textField.toLowerCase())) {
                    mediaResult.add(media);
                }
            }
        }
        return mediaResult;
    }
}
