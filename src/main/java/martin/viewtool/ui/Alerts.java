/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package martin.viewtool.ui;

/**
 *
 * @author cesar
 */
import javax.swing.*;
import java.awt.*;

public final class Alerts {

    private Alerts() {
    }
    
    
    
    public static void info(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent,msg , "Information",
                JOptionPane.INFORMATION_MESSAGE);
               
    }

    public static void warn(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Warning",
                JOptionPane.WARNING_MESSAGE);
    }

    public static void error(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public static boolean confirm(Component parent, String msg, String title) {
        return JOptionPane.showConfirmDialog(parent, msg, title,
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }

    public static void showException(Component parent, Throwable ex) {
        String msg = ex.getMessage() != null ? ex.getMessage() : ex.getClass().getSimpleName();
        error(parent, msg);
    }
}
