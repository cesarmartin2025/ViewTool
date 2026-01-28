/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package martin.viewtool.ui;

import MediaSyncPolling.CheckListMediaEvent;
import MediaSyncPolling.CheckListMediaListener;
import MediaSyncPolling.MediaSyncPolling;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import martin.viewtool.config.PreferencesService;
import martin.viewtool.core.LibraryService;
import MediaSyncPolling.Media;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import martin.viewtool.core.MediaItem;
import martin.viewtool.core.MediaTableModel;
import martin.viewtool.core.MediaService;
import martin.viewtool.core.TokenService;
import javax.swing.RowFilter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import java.util.regex.Pattern;

/**
 *
 * @author cesar
 */
public class PanelManagement extends javax.swing.JPanel {

    private final PreferencesService prefService = new PreferencesService();

    private final LibraryService libraryService = new LibraryService(Path.of(prefService.getOutputDir().toString()));
    private ViewToolApp jframe;
    private final TokenService tokenService;
    private final MediaService mediaService = new MediaService();
    private List<Media> listMedia = new ArrayList<>();
    private MediaTableModel tableModel;

    private boolean listenerAdded = false;
    private String token;

    private TableRowSorter<MediaTableModel> tableSorter;
    private boolean searchLiveInstalled = false;

    public PanelManagement(ViewToolApp jframe) {
        this.jframe = jframe;
        this.tokenService = jframe.getTokenService();
        initComponents();
        token = tokenService.getToken();

        comboFilterListener();
        LiveSearchOnTable();

    }
    
    //Metodo para hacer busqueda en la tabla en tiempo real tecla por tecla mediante el DocumentListener. Al detectar un cambio, llama al metodo que filtra los resultados.
    

    private void LiveSearchOnTable() {
        if (searchLiveInstalled) {
            return;
        }

        textFieldFile.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                applyTableFilter();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                applyTableFilter();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                applyTableFilter();
            }
        });

        searchLiveInstalled = true;
    }
    
    // Metodo que usa la TableRowSorter en el modelo de la tabla para filtrar el texto que hay en el TextField.

    private void applyTableFilter() {
        if (tableSorter == null) {
            return;
        }

        String text = textFieldFile.getText();

        if (text == null || text.isBlank()) {
            tableSorter.setRowFilter(null);
            return;
        }
        
        tableSorter.setRowFilter(
                RowFilter.regexFilter("(?i)" + Pattern.quote(text))
        );
    }

    public final void comboFilterListener() {
        comboFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                refreshLocalList();
            }
        });

    }

    // Inicia la carga de archivos cuando el panel se muestra por pantalla.
    @Override
    public void addNotify() {
        super.addNotify();

        // carga automática de JList y JTable
        initialLoad();
        startPolling();
    }

    //Uso un Timer con valor 0 para que cargue los datos una vez se pinte el panel.
    private void initialLoad() {

        javax.swing.Timer timer = new javax.swing.Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                refreshLocalList();
                rebuildMediaTable();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    // Actualiza la lista local segun el item que ha elegido el usuario
    private void refreshLocalList() {
        try {
            String selected = (String) comboFilter.getSelectedItem();
            List<MediaItem> files = libraryService.getFilteredFiles(selected);

            DefaultListModel<String> model = new DefaultListModel<String>();
            for (MediaItem file : files) {
                model.addElement(file.getName());
            }
            listFiles.setModel(model);

        } catch (IOException ex) {
            Alerts.error(this, "Error: " + ex.getMessage());
        }
    }

    //Activa el Componente MediaSyncPolling para ver si hay archivos. Si los hay, llama a otro metodo para redibujar la tabla y añadir esos archivos.
    private void startPolling() {
        try {
            MediaSyncPolling mediaSyncPolling = jframe.getComponent();
            token = tokenService.getToken();
            mediaSyncPolling.setToken(token);
            mediaSyncPolling.setPollingInterval(5);

            if (!listenerAdded) {
                mediaSyncPolling.addCustomEventListener(new CheckListMediaListener() {
                    @Override
                    public void checkListMediaReceived(CheckListMediaEvent evt) {
                        List<Media> newMedias = evt.getMediaList();
                        if (newMedias == null || newMedias.isEmpty()) {
                            return;
                        }
                        applyNetworkUpdateTable(newMedias);
                    }
                });
                listenerAdded = true;
            }

            mediaSyncPolling.setRunning(true);

        } catch (Exception ex) {
            Alerts.error(this, "Error: " + ex.getMessage());
        }
    }

    //Añade los archivos a la Tabla mediante el metodo 'addNewMediaNetwork' y redibuja la tabla.
    //Lo hace mediante un Timer para que no colapse la aplicacion y espere a que todos los eventos en cola hayan finalizado para inciar el metodo.
    private void applyNetworkUpdateTable(final List<Media> newMedia) {
        javax.swing.Timer timer = new javax.swing.Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                mediaService.addNewMediaNetwork(newMedia);
                rebuildMediaTable();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }


    private void rebuildMediaTable() {
        List<Media> mediaListCombined = mediaService.createMediaListCombined();

        tableModel = new MediaTableModel(mediaListCombined, mediaService);
        tableFiles.setModel(tableModel);

        tableSorter = new TableRowSorter<>(tableModel);
        tableFiles.setRowSorter(tableSorter);

        listMedia = new ArrayList<>(mediaListCombined);

        columnPrefs();
        applyTableFilter();
    }

    private Media getSelectedMedia(String action) {
        int selectedRow = tableFiles.getSelectedRow();
        if (selectedRow < 0) {
            Alerts.error(
                    this,
                    "Please select a file to " + action + "."
            );
            return null;
        }
        int modelRow = tableFiles.convertRowIndexToModel(selectedRow);

        MediaTableModel model = (martin.viewtool.core.MediaTableModel) tableFiles.getModel();
        Media file = model.getFile(modelRow);
        return file;

    }

    private void columnPrefs() {
        var columnModel = tableFiles.getColumnModel();

        // Columna 0: Location
        columnModel.getColumn(0).setPreferredWidth(50);

        // Columna 1:Name
        columnModel.getColumn(1).setPreferredWidth(300);

        // Columna 2: URL
        columnModel.getColumn(2).setPreferredWidth(300);
        ;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelManagement = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listFiles = new javax.swing.JList<>();
        buttonRefreshList = new javax.swing.JButton();
        comboFilter = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableFiles = new javax.swing.JTable();
        buttonRefreshTable = new javax.swing.JButton();
        textFieldFile = new javax.swing.JTextField();
        labelSearchFile = new javax.swing.JLabel();
        buttonDeleteFile = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        buttonDownloadFile = new javax.swing.JButton();
        buttonUploadFile = new javax.swing.JButton();
        buttonOpenFile = new javax.swing.JButton();

        panelManagement.setMaximumSize(new java.awt.Dimension(1920, 1080));
        panelManagement.setMinimumSize(new java.awt.Dimension(800, 600));
        panelManagement.setName(""); // NOI18N
        panelManagement.setLayout(null);

        jScrollPane1.setViewportView(listFiles);

        panelManagement.add(jScrollPane1);
        jScrollPane1.setBounds(0, 50, 410, 400);

        buttonRefreshList.setText("Refresh list");
        buttonRefreshList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRefreshListActionPerformed(evt);
            }
        });
        panelManagement.add(buttonRefreshList);
        buttonRefreshList.setBounds(0, 450, 100, 23);

        comboFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Videos", "Audios" }));
        comboFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboFilterActionPerformed(evt);
            }
        });
        panelManagement.add(comboFilter);
        comboFilter.setBounds(330, 450, 80, 22);

        tableFiles.setAutoCreateRowSorter(true);
        tableFiles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane2.setViewportView(tableFiles);

        panelManagement.add(jScrollPane2);
        jScrollPane2.setBounds(430, 50, 920, 400);

        buttonRefreshTable.setText("Refresh table");
        buttonRefreshTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRefreshTableActionPerformed(evt);
            }
        });
        panelManagement.add(buttonRefreshTable);
        buttonRefreshTable.setBounds(430, 450, 110, 23);

        textFieldFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textFieldFileActionPerformed(evt);
            }
        });
        textFieldFile.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                textFieldFileKeyPressed(evt);
            }
        });
        panelManagement.add(textFieldFile);
        textFieldFile.setBounds(1150, 20, 200, 22);

        labelSearchFile.setText("Search :");
        panelManagement.add(labelSearchFile);
        labelSearchFile.setBounds(1100, 10, 50, 40);

        buttonDeleteFile.setText("Delete File");
        buttonDeleteFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDeleteFileActionPerformed(evt);
            }
        });
        panelManagement.add(buttonDeleteFile);
        buttonDeleteFile.setBounds(790, 450, 130, 23);

        jLabel1.setText("Media downloaded by ytb-dlp:");
        panelManagement.add(jLabel1);
        jLabel1.setBounds(10, 20, 210, 30);

        jLabel2.setText("DI Media Network Library :");
        panelManagement.add(jLabel2);
        jLabel2.setBounds(430, 20, 170, 30);

        buttonDownloadFile.setText("Download File");
        buttonDownloadFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDownloadFileActionPerformed(evt);
            }
        });
        panelManagement.add(buttonDownloadFile);
        buttonDownloadFile.setBounds(920, 450, 140, 23);

        buttonUploadFile.setText("Upload File");
        buttonUploadFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonUploadFileActionPerformed(evt);
            }
        });
        panelManagement.add(buttonUploadFile);
        buttonUploadFile.setBounds(660, 450, 130, 23);

        buttonOpenFile.setText("Open File");
        buttonOpenFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOpenFileActionPerformed(evt);
            }
        });
        panelManagement.add(buttonOpenFile);
        buttonOpenFile.setBounds(540, 450, 120, 23);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(panelManagement, javax.swing.GroupLayout.PREFERRED_SIZE, 1357, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(173, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(panelManagement, javax.swing.GroupLayout.PREFERRED_SIZE, 847, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void buttonRefreshListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRefreshListActionPerformed
        try {

            String selected = (String) comboFilter.getSelectedItem();
            List<MediaItem> files = libraryService.getFilteredFiles(selected);

            DefaultListModel<String> model = new DefaultListModel<>();
            for (MediaItem file : files) {
                model.addElement(file.getName());
            }
            listFiles.setModel(model);

        } catch (IOException ex) {
            Alerts.error(this, "Error: " + ex.getMessage());
        }
    }//GEN-LAST:event_buttonRefreshListActionPerformed

    private void buttonRefreshTableActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRefreshTableActionPerformed
        rebuildMediaTable();
        startPolling();
    }//GEN-LAST:event_buttonRefreshTableActionPerformed

    private void buttonDeleteFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDeleteFileActionPerformed

        Media file = getSelectedMedia("Delete");
        if (file == null) {
            return;
        }

        File localFile = mediaService.getLocalFile(file);

        try {
            boolean deleted = Files.deleteIfExists(localFile.toPath());

            if (deleted) {
                    Alerts.info(this, "File deleted successfully.");
                } else {
                    Alerts.error(this, "File is on network, could not be deleted.");
                }
           
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error deleting file: " + ex.getMessage());
        }

    }//GEN-LAST:event_buttonDeleteFileActionPerformed

    private void buttonDownloadFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDownloadFileActionPerformed
        MediaSyncPolling mediaSyncPolling = jframe.getComponent();
        Media file = getSelectedMedia("Download");
        
        if (file == null) {
            return;
        }
        int idMedia = file.id;

        Path destPath = mediaService.getDownloadBaseDir().resolve(file.mediaFileName);
        if(Files.exists(destPath)){
            Alerts.error(this, "This file is already downloaded.");
            return;
        } 
        File destFile = destPath.toFile();
        try {
            mediaSyncPolling.download(idMedia, destFile, token);
            Alerts.info(this, "File successfully downloaded to "+prefService.getOutputDir().toString());
        } catch (Exception ex) {
            Alerts.error(this, "Error downloading file: " + ex.getMessage());
        }
    }//GEN-LAST:event_buttonDownloadFileActionPerformed

    private void buttonUploadFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonUploadFileActionPerformed
        JFileChooser jFileChooserUpload = new JFileChooser();
        int result = jFileChooserUpload.showOpenDialog(this);
        if (result == javax.swing.JFileChooser.APPROVE_OPTION) {
            File selectedFile = jFileChooserUpload.getSelectedFile();
            if (selectedFile.isDirectory()) {
                Alerts.error(this,
                        "Please select a file, not a directory.");
                return;
            }

            String name = selectedFile.getName().toLowerCase();
            if (!name.endsWith(".mp3") && !name.endsWith(".mp4") && !name.endsWith(".m4a")) {
                Alerts.error(this,
                        "Only MP3, MP4 or M4A files are allowed.");
                return;
            }

            String youtubeUrl = JOptionPane.showInputDialog(
                    this,
                    "Enter the YouTube URL for this media:",
                    "YouTube URL",
                    JOptionPane.QUESTION_MESSAGE
            );

            if (youtubeUrl == null) {
                return;
            }

            youtubeUrl = youtubeUrl.trim();
            if (youtubeUrl.isEmpty()) {
                Alerts.error(this,
                        "URL cannot be empty.");
                return;
            }
            MediaSyncPolling msp = jframe.getComponent();
            try {
                msp.uploadFileMultipart(selectedFile, youtubeUrl, token);
            } catch (Exception e) {
                Alerts.error(this, e.getMessage());
            }

            Alerts.info(this,
                    "File uploaded:\n" + selectedFile.getAbsolutePath());
        }

    }//GEN-LAST:event_buttonUploadFileActionPerformed

    private void buttonOpenFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOpenFileActionPerformed

        Media file = getSelectedMedia("Open");
        if (file == null) {
            return;
        }

        File localFile = mediaService.getLocalFile(file);

        if (!localFile.exists()) {
            Alerts.error(this,
                    "This media is not downloaded in the local directory.");
            return;
        }

        try {
            if (!java.awt.Desktop.isDesktopSupported()) {
                throw new UnsupportedOperationException("Desktop API not supported on this platform.");
            }
            java.awt.Desktop.getDesktop().open(localFile);
        } catch (Exception ex) {
            Alerts.error(this,
                    "Error opening file: " + ex.getMessage());
        }
    }//GEN-LAST:event_buttonOpenFileActionPerformed

    private void textFieldFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textFieldFileActionPerformed
        applyTableFilter();

    }//GEN-LAST:event_textFieldFileActionPerformed

    private void comboFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboFilterActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboFilterActionPerformed

    private void textFieldFileKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textFieldFileKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_textFieldFileKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonDeleteFile;
    private javax.swing.JButton buttonDownloadFile;
    private javax.swing.JButton buttonOpenFile;
    private javax.swing.JButton buttonRefreshList;
    private javax.swing.JButton buttonRefreshTable;
    private javax.swing.JButton buttonUploadFile;
    private javax.swing.JComboBox<String> comboFilter;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel labelSearchFile;
    private javax.swing.JList<String> listFiles;
    private javax.swing.JPanel panelManagement;
    private javax.swing.JTable tableFiles;
    private javax.swing.JTextField textFieldFile;
    // End of variables declaration//GEN-END:variables
}
