/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ComponentePrueba;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

/**
 *
 * @author cesar
 */
public class CustomEvent extends EventObject {
    private final List<Media> mediaList;
    private final LocalDateTime dateTime;
    
    
    
    public CustomEvent(Object source,List<Media> mediaList, LocalDateTime dateTime) {
        super(source);
        this.mediaList=mediaList;
        this.dateTime=dateTime;
        
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }
    public List<Media> getMediaList(){
       List<Media> copiaLista = new ArrayList<>(mediaList);
       return copiaLista;
    }
    
    
}
