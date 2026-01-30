/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package martin.viewtool.ui;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.io.IOException;
import java.nio.file.Path;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import martin.viewtool.config.PreferencesService;
import martin.viewtool.core.DownloadController;
import martin.viewtool.core.LibraryService;
import martin.viewtool.core.MediaFormat;
import martin.viewtool.core.PlayService;
import martin.viewtool.core.YtDlpService;


/**
 *
 * @author cesar
 */
public class PanelMain extends JPanel {
    private javax.swing.JLabel audioIcon;
    private javax.swing.JButton buttonDownload;
    private javax.swing.JRadioButton buttonMP3;
    private javax.swing.JRadioButton buttonMP4;
    private javax.swing.JButton buttonPlayVideo;
    private javax.swing.JCheckBox checkBoxOnlyAudio;
    private javax.swing.JLabel labelOnlyAudio;
    private javax.swing.JLabel labelUrl;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JTextField textFieldURL;
    private javax.swing.JLabel videoIcon;
    private javax.swing.JLabel youtubeIcon;
    
    private final PreferencesService prefService = new PreferencesService();

    private final DownloadController controller
            = new DownloadController(new YtDlpService(prefService.getYtDlpPath()));

    private final PlayService playService = new PlayService();
    private final PanelPreferences preferences = new PanelPreferences();

    private final LibraryService libraryService = new LibraryService(Path.of(prefService.getOutputDir().toString()));
    
    public PanelMain(){
        initComponents();
        setupEvents();
    }
    
    private void setIcon(String path, JLabel label) {
        ImageIcon icon = new ImageIcon(getClass().getResource(path));

        Image img = icon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);

        label.setIcon(new ImageIcon(img));

    }
    
    private void groupButtons() {
        ButtonGroup formatGroup = new ButtonGroup();
        formatGroup.add(buttonMP3);
        formatGroup.add(buttonMP4);

        buttonMP4.setSelected(true);
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        setBackground(java.awt.Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();

        
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        //Fila URL + Icono YT
        youtubeIcon = new JLabel();
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.weightx = 0;
        add(youtubeIcon, gbc);
        
        labelUrl = new JLabel("URL: ");
        gbc.gridx = 1;
        add(labelUrl, gbc);

        textFieldURL = new JTextField();
        gbc.gridx = 2;
        gbc.weightx = 1.0; 
        add(textFieldURL, gbc);

        checkBoxOnlyAudio = new JCheckBox();
        gbc.gridx = 3;
        gbc.weightx = 0;
        add(checkBoxOnlyAudio, gbc);

        labelOnlyAudio = new JLabel("Only audio");
        gbc.gridx = 4;
        add(labelOnlyAudio, gbc);

        //Fila botones MP3 Y MP4 +Iconos
        gbc.gridy = 1;
        gbc.gridx = 2; 
        gbc.weightx = 1.0;

        audioIcon = new JLabel();
        buttonMP3 = new JRadioButton("MP3");
        videoIcon = new JLabel();
        buttonMP4 = new JRadioButton("MP4");
        
        //Panel para agrupar los botones y los iconos MP3,MP4.
        
        JPanel formatPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        formatPanel.setOpaque(false);
        formatPanel.add(audioIcon);
        formatPanel.add(buttonMP3);
        formatPanel.add(new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), null, null)); 
        formatPanel.add(videoIcon);
        formatPanel.add(buttonMP4);

        gbc.anchor = GridBagConstraints.WEST;
        add(formatPanel, gbc);

        //Fila de botones de descarga y reproducir ultimo video
        gbc.gridy = 2;
        gbc.gridx = 2;
        gbc.gridwidth = 3; 
        
        buttonDownload = new JButton("Download");
        buttonPlayVideo = new JButton("Play last video");
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        actionPanel.setOpaque(false);
        actionPanel.add(buttonDownload);
        actionPanel.add(buttonPlayVideo);

        gbc.anchor = GridBagConstraints.EAST;
        add(actionPanel, gbc);

        //Fila para la barra de progreso.
        progressBar = new JProgressBar();
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 5;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0; 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.SOUTH; 
        gbc.insets = new Insets(40, 10, 10, 10); 
        add(progressBar, gbc);
        
        groupButtons();
        setIcon("/images/audioicon.png", audioIcon);
        setIcon("/images/videoicon.png", videoIcon);
        setIcon("/images/youtubeicon.png", youtubeIcon);
        
        //Para asegurarme que funcione correctamente, repinta el panel.
        revalidate();
        repaint();
    }
    
    private void setupEvents() {
        // Evento para reproducir el video
        buttonPlayVideo.addActionListener(e -> handlePlayVideo());

        // Evento para descargar
        buttonDownload.addActionListener(e -> handleDownload());
    }

    private void handlePlayVideo() {
        try {
            playService.playLastDownloaded(prefService.getOutputDir());
        } catch (IOException ex) {
            Alerts.showException(this, ex);
        }
    }

    private void handleDownload() {
        buttonDownload.setEnabled(false);
        progressBar.setValue(0);

        new SwingWorker<Integer, Integer>() {
            StringBuilder log = new StringBuilder();
            volatile boolean seenAnyPercent = false;

            @Override
            protected Integer doInBackground() throws Exception {
                //Validacion
                controller.validateUrl(textFieldURL.getText());
                String limit = prefService.getLimitSpeed();
                controller.validateSpeed(limit);

                Alerts.info(PanelMain.this, "The download is starting, please wait...");

                // Inicio de descarga
                return controller.startDownload(
                    textFieldURL.getText(),
                    buttonMP3.isSelected() ? MediaFormat.MP3 : MediaFormat.MP4,
                    checkBoxOnlyAudio.isSelected(),
                    prefService.getOutputDir(),
                    log,
                    pct -> publish(pct), // Consumer de porcentaje
                    limit,
                    preferences.isM3USelected()
                );
            }

            @Override
            protected void process(java.util.List<Integer> chunks) {
                int last = chunks.get(chunks.size() - 1);
                if (!seenAnyPercent) {
                    progressBar.setIndeterminate(false);
                    seenAnyPercent = true;
                }
                progressBar.setValue(last);
            }

            @Override
            protected void done() {
                buttonDownload.setEnabled(true);
                try {
                    int exit = get();
                    if (exit == 0) {
                        Alerts.info(PanelMain.this, "Download complete to "+prefService.getOutputDir().toString());
                        progressBar.setValue(100);
                    } else {
                        Alerts.warn(PanelMain.this, "Process finished with error code: " + exit);
                    }
                } catch (Exception ex) {
                    Alerts.showException(PanelMain.this, 
                        ex.getCause() != null ? ex.getCause() : ex);
                }
            }
        }.execute();
    }

    
}
    
    

