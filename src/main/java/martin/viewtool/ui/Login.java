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

    public Login() {

        setLayout(null);
        setName("Login");

        buttonLogin = new JButton();
        buttonLogin.setBounds(100, 150, 100, 30);
        buttonLogin.setText("Login");

        labelEmail = new JLabel();
        labelEmail.setBounds(50, 50, 50, 50);
        labelEmail.setText("Email: ");

        fieldEmail = new JTextField();
        fieldEmail.setBounds(100, 50, 300, 40);

        labelPassword = new JLabel();
        labelPassword.setBounds(30, 100, 80, 50);
        labelPassword.setText("Password: ");

        fieldPassword = new JPasswordField();
        fieldPassword.setBounds(100, 100, 300, 40);

        checkBoxRemember = new JCheckBox();
        checkBoxRemember.setBounds(400,100, 150, 50);
        checkBoxRemember.setText("Remember me");

        add(buttonLogin);
        add(labelEmail);
        add(fieldEmail);
        add(labelPassword);
        add(fieldPassword);
        add(checkBoxRemember);
    }
}
