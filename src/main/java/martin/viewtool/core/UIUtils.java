/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package martin.viewtool.core;

import java.awt.Font;
import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

/**
 *
 * @author cesar
 */
public class UIUtils {
    
     public static final Font BOLD_FONT = new Font("Segoe UI", java.awt.Font.BOLD, 13);
    public static final Font PLAIN_FONT = new Font("Segoe UI", java.awt.Font.PLAIN, 13);
    
    //Metodo para establecer un tamaño fijo de iconos.
    
    public ImageIcon getFixedSizeIcon(String imageName, int ancho, int alto) {
        
        String path = "/images/" + imageName;
        URL imgUrl = UIUtils.class.getResource(path);

        ImageIcon icono = new ImageIcon(imgUrl);
        Image img = icono.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
    
}
