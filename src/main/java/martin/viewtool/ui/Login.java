/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package martin.viewtool.ui;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
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
    private String baseUrl = "https://dimedianetapi9.azurewebsites.net/";
    private String token;
    
    private LoginJFrame loginFrame;

    public Login(LoginJFrame loginFrame) {
        this.loginFrame= loginFrame;
        setLayout(null);
        setName("Login");

        buttonLogin = new JButton();
        buttonLogin.setBounds(100, 150, 100, 30);
        buttonLogin.setText("Login");

        labelEmail = new JLabel();
        labelEmail.setBounds(50, 40, 50, 50);
        labelEmail.setText("Email: ");

        fieldEmail = new JTextField();
        fieldEmail.setBounds(100, 50, 300, 30);

        labelPassword = new JLabel();
        labelPassword.setBounds(30, 90, 80, 50);
        labelPassword.setText("Password: ");

        fieldPassword = new JPasswordField();
        fieldPassword.setBounds(100, 100, 300, 30);

        checkBoxRemember = new JCheckBox();
        checkBoxRemember.setBounds(400, 90, 150, 50);
        checkBoxRemember.setText("Remember me");

        add(buttonLogin);
        add(labelEmail);
        add(fieldEmail);
        add(labelPassword);
        add(fieldPassword);
        add(checkBoxRemember);

        buttonLogin.addActionListener(new ActionListener() {

            @Override
            @SuppressWarnings("empty-statement")
            public void actionPerformed(ActionEvent e) {

                String email = fieldEmail.getText();
                String password = new String(fieldPassword.getPassword());
                boolean remember = checkBoxRemember.isSelected();

                apiClient = new ApiClient(baseUrl);

                if (email.isBlank() || password.isBlank()) {
                    Alerts.error(Login.this, "Login Failed. Please, write your email and your password");
                    return;
                } else {

                    try {
                        token = apiClient.login(email, password);
                    } catch (Exception ex) {
                        Alerts.error(Login.this, "Login failed. Please try again.");
                        return;
                    }
                    if (token != null) {
                       loginFrame.dispose();
                       new ViewToolApp(token).setVisible(true);
                    }
                    Alerts.info(Login.this, "Login was successfull.");
                    if (remember && token != null) {
                        Path p = Path.of("datos/token_txt.txt");

                        try {
                            if (!Files.exists(p.getParent())) {
                                Files.createDirectories(p.getParent());
                            }
                        } catch (IOException ex) {
                            Alerts.error(Login.this, "App didnt found the folder. Please, try again.");
                        }

                        BufferedWriter out;
                        try {
                            out = new BufferedWriter(new FileWriter(p.toFile()));
                            out.write(token);
                            out.close();

                        } catch (IOException ex) {
                            Alerts.error(Login.this, "Some problem occurred while was saving the token. Please, login again.");
                        }

                    }
                }

            }
        });
    }

}
