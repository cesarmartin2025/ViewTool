/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package martin.viewtool.ui;

/**
 * Utility class with static methods for showing standard Swing dialog boxes.
 *
 * @author cesar
 */
import javax.swing.*;
import java.awt.*;

public final class Alerts {

    private Alerts() {
    }

    /**
     * Shows an information dialog.
     *
     * @param parent parent component, or {@code null}
     * @param msg    message to display
     */
    public static void info(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent,msg , "Information",
                JOptionPane.INFORMATION_MESSAGE);              
    }

    /**
     * Shows a warning dialog.
     *
     * @param parent parent component, or {@code null}
     * @param msg    warning message
     */
    public static void warn(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Warning",
                JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Shows an error dialog.
     *
     * @param parent parent component, or {@code null}
     * @param msg    error message
     */
    public static void error(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Shows a Yes/No confirmation dialog.
     *
     * @param parent parent component, or {@code null}
     * @param msg    question to display
     * @param title  dialog title
     * @return {@code true} if the user clicked Yes
     */
    public static boolean confirm(Component parent, String msg, String title) {
        return JOptionPane.showConfirmDialog(parent, msg, title,
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
    }

    /**
     * Shows an error dialog with the exception message.
     *
     * @param parent parent component, or {@code null}
     * @param ex     the exception to display
     */
    public static void showException(Component parent, Throwable ex) {
        String msg = ex.getMessage() != null ? ex.getMessage() : ex.getClass().getSimpleName();
        error(parent, msg);
    }
}
