/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package martin.viewtool.utils;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

/**
 * Utility class with helper methods for common Swing UI operations.
 *
 * @author cesar
 */
public class UIUtils {

    /** Bold Segoe UI font at size 13. */
    public static final Font BOLD_FONT = new Font("Segoe UI", java.awt.Font.BOLD, 13);
    /** Plain Segoe UI font at size 13. */
    public static final Font PLAIN_FONT = new Font("Segoe UI", java.awt.Font.PLAIN, 13);

    /**
     * Loads an image from the {@code /images/} classpath resource folder and
     * scales it to the specified dimensions.
     *
     * @param imageName file name of the image
     * @param ancho     desired width in pixels
     * @param alto      desired height in pixels
     * @return scaled {@link ImageIcon}
     */
    public ImageIcon getFixedSizeIcon(String imageName, int ancho, int alto) {
        
        String path = "/images/" + imageName;
        URL imgUrl = UIUtils.class.getResource(path);

        ImageIcon icono = new ImageIcon(imgUrl);
        Image img = icono.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }
    
    /**
     * Displays a temporary feedback message on a label and clears it after 5 seconds.
     *
     * @param infoLabel the label to update
     * @param message   message text to display
     * @param isError   if {@code true} the text is shown in red, otherwise in black
     */
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
    
    /**
     * Adds a mouse listener to the button that switches the cursor to a hand
     * when the pointer enters the button area.
     *
     * @param button the button to configure
     */
    public void setHoverButton(JButton button) {
        button.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                
                button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

        });
    }
    
}
