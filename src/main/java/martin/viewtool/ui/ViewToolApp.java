/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package martin.viewtool.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.UIManager;

import martin.viewtool.core.TokenService;

/**
 *
 * @author cesar
 */
public class ViewToolApp extends javax.swing.JFrame {

    private final TokenService tokenService = new TokenService();
    private String token;
    private final String apiUrl = "https://dimedianetapi9.azurewebsites.net/";
    private boolean loggedIn = false;

    PanelManagement panelManagement;
    PanelMain panelMain;
    PanelPreferences panelPreferences;

    private JPanel root;
    private JPanel panelButtonMainAndSetting;
    private JPanel dinamicPanel;

    /**
     * Creates new form ViewToolApp
     */
    public ViewToolApp() {
        applyGlobalUI();
        initComponents();
        buildLayout();
        token = tokenService.getToken();
        mediaSyncPolling1.setApiUrl(apiUrl);
        panelManagement = new PanelManagement(this);
        panelMain = new PanelMain();
        panelPreferences = new PanelPreferences();

        if (token == null) {
            Login login = new Login(this);
            showPanel(login);

        } else {
            loggedSuccess(token);
        }

    }

    private ImageIcon sizeIcon(String ruta, int ancho, int alto) {
        ImageIcon icono = new ImageIcon(getClass().getResource(ruta));
        Image img = icono.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
        return new ImageIcon(img);
    }

    private static void applyGlobalUI() {
        // Definir los estilos
        Color myBackgroundColor = Color.WHITE;
        java.awt.Font myFont = new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14);

        // Aplicar a todos los Paneles
        UIManager.put("Panel.background", myBackgroundColor);
        //Aplica a todos los componentes
        UIManager.put("Label.font", myFont);
        UIManager.put("Button.font", myFont);
        // Lo dejo comentado porque esta en duda
        // UIManager.put("Button.border", BorderFactory.createLineBorder(Color.GRAY));
        UIManager.put("Button.background", java.awt.Color.lightGray);
        UIManager.put("Button.foreground", java.awt.Color.BLACK);
        UIManager.put("ComboBox.background", java.awt.Color.lightGray);
        UIManager.put("ComboBox.foreground", java.awt.Color.BLACK);
        UIManager.put("ToolTip.background", java.awt.Color.lightGray);
        UIManager.put("CheckBox.background", myBackgroundColor);
        UIManager.put("RadioButton.background", myBackgroundColor);
        UIManager.put("OptionPane.background", myBackgroundColor);
        UIManager.put("OptionPane.foreground", myFont);

        // Fondo de panel siempre opaco por coherencia a la hora de implementar las caracteristicas visuales.
        UIManager.put("Panel.opaque", true);
    }

    private void buildLayout() {

        root = new JPanel(new BorderLayout());

        //Panel del Boton fijo para volver a Main
        panelButtonMainAndSetting = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelButtonMainAndSetting.add(buttonMain);
        panelButtonMainAndSetting.add(buttonSetting);

        buttonSetting.setIcon(sizeIcon("/images/settingicon.png", 30, 30));
        buttonMain.setIcon(sizeIcon("/images/homeicon.png", 30, 30));

        //Panel dinamico que muestra los paneles de la app
        dinamicPanel = new JPanel(new BorderLayout());

        root.add(panelButtonMainAndSetting, BorderLayout.NORTH);
        root.add(dinamicPanel, BorderLayout.CENTER);

        setContentPane(root);
    }

    public final void loggedSuccess(String token) {
        this.token = token;
        this.loggedIn = true;
        showMainAndManagement(panelMain, panelManagement);
        buttonSetting.setVisible(true);

    }

    public MediaSyncPolling.MediaSyncPolling getComponent() {
        return mediaSyncPolling1;
    }

    public TokenService getTokenService() {
        return tokenService;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    private void showPanel(JPanel panel) {
        dinamicPanel.removeAll();
        panel.setOpaque(true);

        dinamicPanel.add(panel, BorderLayout.CENTER);

        if (!isLoggedIn()) {
            panelButtonMainAndSetting.setVisible(false);
        } else {
            panelButtonMainAndSetting.setVisible(true);
        }

        dinamicPanel.revalidate();
        dinamicPanel.repaint();

        pack();
        setLocationRelativeTo(null);

        buttonSetting.setVisible(false);
        buttonMain.setVisible(true);

    }

    public void showMainAndManagement(JPanel top, JPanel bottom) {
        JPanel combined = new JPanel(new BorderLayout());
        top.setPreferredSize(new java.awt.Dimension(0, 167));
        combined.add(top, BorderLayout.NORTH);
        combined.add(bottom, BorderLayout.CENTER);
        showPanel(combined);
        buttonSetting.setVisible(true);
        buttonMain.setVisible(false);

        pack();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dialogAbout = new javax.swing.JDialog();
        TextAreaAbout = new javax.swing.JTextArea();
        jOptionPane1 = new javax.swing.JOptionPane();
        mediaSyncPolling1 = new MediaSyncPolling.MediaSyncPolling();
        buttonMain = new javax.swing.JButton();
        buttonSetting = new javax.swing.JButton();
        menuBarMain = new javax.swing.JMenuBar();
        MenuFile = new javax.swing.JMenu();
        MenuLogoutItem = new javax.swing.JMenuItem();
        MenuItemExit = new javax.swing.JMenuItem();
        MenuEdit = new javax.swing.JMenu();
        MenuItemPreferences = new javax.swing.JMenuItem();
        MenuHelp = new javax.swing.JMenu();
        MenuItemAbout = new javax.swing.JMenuItem();

        dialogAbout.setTitle("About");
        dialogAbout.setModal(true);
        dialogAbout.setSize(new java.awt.Dimension(300, 300));

        TextAreaAbout.setEditable(false);
        TextAreaAbout.setColumns(20);
        TextAreaAbout.setRows(5);
        TextAreaAbout.setText("Author: César Martín Pérez.\nSubject: DI - DAM 2025/2026\nDate: 27/10/2025\n\nRecourses:\n- yt-dlp\n- ffmpeg/ffprobe\n");

        javax.swing.GroupLayout dialogAboutLayout = new javax.swing.GroupLayout(dialogAbout.getContentPane());
        dialogAbout.getContentPane().setLayout(dialogAboutLayout);
        dialogAboutLayout.setHorizontalGroup(
            dialogAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dialogAboutLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(TextAreaAbout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        dialogAboutLayout.setVerticalGroup(
            dialogAboutLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(TextAreaAbout, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ViewTool");
        setBackground(new java.awt.Color(255, 255, 255));
        setFocusCycleRoot(false);
        setSize(new java.awt.Dimension(800, 600));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                formMouseClicked(evt);
            }
        });

        buttonMain.setBackground(new java.awt.Color(255, 255, 255));
        buttonMain.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/homeicon.png"))); // NOI18N
        buttonMain.setBorder(null);
        buttonMain.setBorderPainted(false);
        buttonMain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonMainActionPerformed(evt);
            }
        });

        buttonSetting.setBackground(new java.awt.Color(255, 255, 255));
        buttonSetting.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/settingicon.png"))); // NOI18N
        buttonSetting.setBorder(null);
        buttonSetting.setBorderPainted(false);
        buttonSetting.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonSettingActionPerformed(evt);
            }
        });

        MenuFile.setText("File");

        MenuLogoutItem.setText("Logout");
        MenuLogoutItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuLogoutItemActionPerformed(evt);
            }
        });
        MenuFile.add(MenuLogoutItem);

        MenuItemExit.setText("Exit");
        MenuItemExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemExitActionPerformed(evt);
            }
        });
        MenuFile.add(MenuItemExit);

        menuBarMain.add(MenuFile);

        MenuEdit.setText("Edit");

        MenuItemPreferences.setText("Preferences");
        MenuItemPreferences.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemPreferencesActionPerformed(evt);
            }
        });
        MenuEdit.add(MenuItemPreferences);

        menuBarMain.add(MenuEdit);

        MenuHelp.setText("Help");
        MenuHelp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuHelpActionPerformed(evt);
            }
        });

        MenuItemAbout.setText("About");
        MenuItemAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuItemAboutActionPerformed(evt);
            }
        });
        MenuHelp.add(MenuItemAbout);

        menuBarMain.add(MenuHelp);

        setJMenuBar(menuBarMain);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(1000, 1000, 1000)
                .addComponent(mediaSyncPolling1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buttonSetting)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttonMain)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(buttonMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(buttonSetting, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(435, 435, 435)
                .addComponent(mediaSyncPolling1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        setBounds(0, 0, 1180, 506);
    }// </editor-fold>//GEN-END:initComponents

    private void MenuItemExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemExitActionPerformed
        System.exit(0); // TODO add your handling code here:
    }//GEN-LAST:event_MenuItemExitActionPerformed

    private void MenuItemAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemAboutActionPerformed
        dialogAbout.setVisible(true);        // TODO add your handling code here:
    }//GEN-LAST:event_MenuItemAboutActionPerformed

    private void formMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_formMouseClicked

    private void MenuHelpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuHelpActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_MenuHelpActionPerformed

    private void MenuItemPreferencesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuItemPreferencesActionPerformed
        if (isLoggedIn()) {
            showPanel(panelPreferences);
        }


    }//GEN-LAST:event_MenuItemPreferencesActionPerformed

    private void MenuLogoutItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuLogoutItemActionPerformed
        if (isLoggedIn()) {
            showPanel(new Login(this));
            setLoggedIn(false);
            tokenService.deleteToken();
            mediaSyncPolling1.setRunning(false);
            panelButtonMainAndSetting.setVisible(false);
        }

    }//GEN-LAST:event_MenuLogoutItemActionPerformed

    private void buttonMainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonMainActionPerformed
        if (isLoggedIn()) {
            showMainAndManagement(panelMain, panelManagement);
        }
    }//GEN-LAST:event_buttonMainActionPerformed

    private void buttonSettingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonSettingActionPerformed
        showPanel(panelPreferences);
    }//GEN-LAST:event_buttonSettingActionPerformed

    /**
     * @param args the command line arguments
     */

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu MenuEdit;
    private javax.swing.JMenu MenuFile;
    private javax.swing.JMenu MenuHelp;
    private javax.swing.JMenuItem MenuItemAbout;
    private javax.swing.JMenuItem MenuItemExit;
    private javax.swing.JMenuItem MenuItemPreferences;
    private javax.swing.JMenuItem MenuLogoutItem;
    private javax.swing.JTextArea TextAreaAbout;
    private javax.swing.JButton buttonMain;
    private javax.swing.JButton buttonSetting;
    private javax.swing.JDialog dialogAbout;
    private javax.swing.JOptionPane jOptionPane1;
    private MediaSyncPolling.MediaSyncPolling mediaSyncPolling1;
    private javax.swing.JMenuBar menuBarMain;
    // End of variables declaration//GEN-END:variables
}
