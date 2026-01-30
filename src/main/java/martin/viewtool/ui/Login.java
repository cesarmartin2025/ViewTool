/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package martin.viewtool.ui;

import MediaSyncPolling.MediaSyncPolling;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import martin.viewtool.core.TokenService;

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
    private String token;
    private TokenService tokenService;
    private ViewToolApp jframe;

    public Login(ViewToolApp jframe) {
        this.jframe = jframe;
        this.tokenService = jframe.getTokenService();
        setLayout(new BorderLayout());
        setName("Login");
        setBorder(new EmptyBorder(20, 20, 20, 20));

        
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); 
        gbc.fill = GridBagConstraints.HORIZONTAL;

        
        labelEmail = new JLabel("Email:");
        ImageIcon emailIcon = new ImageIcon(getClass().getResource("/images/correoicon.png"));
        Image emailimg = emailIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        labelEmail.setIcon(new ImageIcon(emailimg));
        
        labelEmail.setHorizontalAlignment(SwingConstants.RIGHT); 
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0; // No crece
        formPanel.add(labelEmail, gbc);

        fieldEmail = new JTextField(15); 
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0; 
        formPanel.add(fieldEmail, gbc);

       
        labelPassword = new JLabel("Pass :");
        ImageIcon passwordIcon = new ImageIcon(getClass().getResource("/images/passwordicon.png"));
        Image passimg = passwordIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);
        labelPassword.setIcon(new ImageIcon(passimg));
        
        labelPassword.setHorizontalAlignment(SwingConstants.RIGHT);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        formPanel.add(labelPassword, gbc);

        fieldPassword = new JPasswordField(15);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1.0;
        formPanel.add(fieldPassword, gbc);

      
        checkBoxRemember = new JCheckBox("Remember me");
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1.0;
        formPanel.add(checkBoxRemember, gbc);

      
        buttonLogin = new JButton("Login");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(buttonLogin);

 
        add(formPanel, BorderLayout.NORTH); // NORTH para que no se estiren verticalmente
        add(buttonPanel, BorderLayout.CENTER);

        buttonLogin.addActionListener(new ActionListener() {

            @Override
            @SuppressWarnings("empty-statement")
            public void actionPerformed(ActionEvent e) {

                String email = fieldEmail.getText();
                String password = new String(fieldPassword.getPassword());
                boolean remember = checkBoxRemember.isSelected();

                MediaSyncPolling mediaSyncPolling = jframe.getComponent();
                if (email.isBlank() || password.isBlank()) {
                    Alerts.error(Login.this, "Login Failed. Please, write your email and your password");
                    return;
                } else {

                    try {
                        token = mediaSyncPolling.login(email, password);
                    } catch (Exception ex) {
                        Alerts.error(Login.this, "Login failed. Please try again.");
                        return;
                    }
                    if (token != null) {
                        tokenService.setToken(token);

                        jframe.setLoggedIn(true);
                    }

                    if (remember && token != null) {
                        tokenService.saveToken(token);

                    }

                    jframe.loggedSuccess(token);
                }

            }
        });
    }

}
