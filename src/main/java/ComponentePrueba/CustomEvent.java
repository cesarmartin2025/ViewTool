/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ComponentePrueba;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

/**
 *
 * @author cesar
 */
public class CustomEvent extends EventObject {
    private final List<Media> mediaList;
    private final String dateTime;
    
    
    
    public CustomEvent(Object source,List<Media> mediaList, String dateTime) {
        super(source);
        this.mediaList=mediaList;
        this.dateTime=dateTime;
        
    }

    public String getDateTime() {
        return dateTime;
    }
    public List<Media> getMediaList(){
       List<Media> copiaLista = new ArrayList<>(mediaList);
       return copiaLista;
    }
    
    
}
