/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package martin.viewtool.ui;

import javax.swing.JFrame;

/**
 *
 * @author cesar
 */
public class LoginJFrame extends JFrame {
    public LoginJFrame() {
        super("Login");
        add(new Login(this));
        setSize(600, 300);
        setResizable(false);
    }
}
