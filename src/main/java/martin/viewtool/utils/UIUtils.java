/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package martin.viewtool.utils;

import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

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
    
    public void showFeedback(JLabel infoLabel,String message, boolean isError) {
    infoLabel.setForeground(isError ? java.awt.Color.RED : java.awt.Color.BLACK);
    infoLabel.setText(message);

    javax.swing.Timer timer = new javax.swing.Timer(5000, new ActionListener() {
        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            // Solo limpia si el mensaje actual sigue siendo el mismo 
            if (infoLabel.getText().equals(message)) {
                infoLabel.setText("");
            }
        }
    });
    timer.setRepeats(false);
    timer.start();
}
    
}
