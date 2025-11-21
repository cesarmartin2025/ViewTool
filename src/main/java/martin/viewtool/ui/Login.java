/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package martin.viewtool.ui;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import martin.viewtool.core.ApiClient;

/**
 *
 * @author cesar
 */
public class Login extends JPanel {
    private JTextField fieldEmail;
    private JPasswordField fieldPassword;
    private JCheckBox checkBoxRemember;
    private JButton buttonLogin;
    private JLabel labelEmail;
    private JLabel labelPassword;
    
    private ApiClient apiClient;
    private String baseUrl;
    private String token;
    
    
    
    
    public Login(){
        setLayout(null);
        setName("Login");
        
        buttonLogin = new JButton();
        buttonLogin.setBounds(250,250,100,50);
        buttonLogin.setText("Login");
        
        labelEmail = new JLabel();
        labelEmail.setBounds(WIDTH, WIDTH, WIDTH, HEIGHT);
        
        fieldEmail = new JTextField();
        fieldEmail.setBounds(50, 50, 200, 100);
        
        labelPassword = new JLabel();
        labelPassword.setBounds(WIDTH, WIDTH, WIDTH, HEIGHT);
        
        fieldPassword = new JPasswordField();
        fieldPassword.setBounds(WIDTH, WIDTH, WIDTH, HEIGHT);
        
        checkBoxRemember = new JCheckBox();
        checkBoxRemember.setBounds(WIDTH, WIDTH, WIDTH, HEIGHT);
        
        
        
        
       
        
       
        add(buttonLogin);
        add(labelEmail);
        add(fieldEmail);
        add(labelPassword);
        add(fieldPassword);
        add(checkBoxRemember);
    }
}
