/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package martin.viewtool.ui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
     private javax.swing.JButton buttonSetting;
    
    private final PreferencesService prefService = new PreferencesService();

    private final DownloadController controller
            = new DownloadController(new YtDlpService(prefService.getYtDlpPath()));

    private final PlayService playService = new PlayService();
    private final PanelPreferences preferences = new PanelPreferences();

    
    private final Font BOLD_FONT = new Font("Segoe UI", java.awt.Font.BOLD, 13);
    private final Font PLAIN_FONT =new Font("Segoe UI", java.awt.Font.PLAIN, 13);
    

    
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
        GridBagConstraints gbc = new GridBagConstraints();

        
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        //primera fila
                
        youtubeIcon = new JLabel();
        labelUrl = new JLabel("URL:");
        labelUrl.setFont(new Font("Segoe UI", java.awt.Font.BOLD, 13));
        textFieldURL = new JTextField(40);
        checkBoxOnlyAudio = new JCheckBox();
        labelOnlyAudio = new JLabel("Only Audio");
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        topPanel.setOpaque(false);
        topPanel.add(youtubeIcon);
        topPanel.add(labelUrl);
        topPanel.add(textFieldURL);
        topPanel.add(new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), null, null)); 
        topPanel.add(checkBoxOnlyAudio);
        topPanel.add(labelOnlyAudio);
        
        //segunda fila

        audioIcon = new JLabel();
        
        buttonMP3 = new JRadioButton("MP3");
        
        videoIcon = new JLabel();
        
        buttonMP4 = new JRadioButton("MP4");
        buttonMP4.setFont(BOLD_FONT);
        
        buttonDownload = new JButton("Download");
        buttonDownload.setBackground(java.awt.Color.lightGray);
        buttonDownload.setForeground(java.awt.Color.BLACK);
  
        buttonPlayVideo = new JButton("Play last video");
        buttonPlayVideo.setBackground(java.awt.Color.lightGray);
        buttonPlayVideo.setForeground(java.awt.Color.BLACK);
  
        //Panel para agrupar los botones y los iconos MP3,MP4.
        
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        buttonsPanel.setOpaque(false);
        buttonsPanel.add(audioIcon);
        buttonsPanel.add(buttonMP3);
        buttonsPanel.add(new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), null, null)); 
        buttonsPanel.add(videoIcon);
        buttonsPanel.add(buttonMP4);
        buttonsPanel.add(new javax.swing.Box.Filler(new java.awt.Dimension(40, 0), null, null)); 
        buttonsPanel.add(buttonDownload);
        buttonsPanel.add(buttonPlayVideo); 
        
        
         //Configuracion comun para todos los paneles esten a la izquierda
        /*
        gbc.gridx = 0;
        gbc.gridwidth = 1; // Ocupa toda la fila
        gbc.weightx = 1.0;                            
        gbc.fill = GridBagConstraints.HORIZONTAL;     
        gbc.anchor = GridBagConstraints.NORTHWEST;
        */
        
        gbc.gridy=0;
        add(topPanel, gbc);
        gbc.gridy=1;
        add(buttonsPanel, gbc);
        

       
        //Fila para la barra de progreso.
        progressBar = new JProgressBar(0,100);
        progressBar.setStringPainted(true);
        progressBar.setValue(0);
       
     
        
        gbc.gridy = 2;
        gbc.weightx=0;
        gbc.fill = GridBagConstraints.NONE;
        progressBar.setPreferredSize(new Dimension(625,20));
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
        
        //Evento para chequear si el checkBox esta activado o no y poner en negrita el label cuando lo esta.
        checkBoxActionListener();
        
        //Lo mismo que el evento anterior pero con los botones MP3 y MP4.
        checkMP3AndMP4Listener();
        
    }
    
    
     private void checkBoxActionListener() {
        checkBoxOnlyAudio.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (checkBoxOnlyAudio.isSelected()) {
                    labelOnlyAudio.setFont(BOLD_FONT);
                } else {
                    labelOnlyAudio.setFont(PLAIN_FONT);
                }
            }
        });
    }
     
    private void checkMP3AndMP4Listener() {
        ActionListener changeFont = new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if (buttonMP3.isSelected()) {
                buttonMP3.setFont(BOLD_FONT);
                buttonMP4.setFont(PLAIN_FONT);
            } else if (buttonMP4.isSelected()) {
                buttonMP4.setFont(BOLD_FONT);
                buttonMP3.setFont(PLAIN_FONT);
            }  
            }   
        };
        
        buttonMP3.addActionListener(changeFont);
        buttonMP4.addActionListener(changeFont);
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
                        progressBar.setValue(0);
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
    
    

