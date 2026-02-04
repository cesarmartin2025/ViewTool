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
import java.awt.Cursor;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.File;
import java.util.concurrent.ExecutionException;
import javax.swing.JFileChooser;
import martin.viewtool.core.MediaItem;
import martin.viewtool.core.MediaTableModel;
import martin.viewtool.core.MediaService;
import martin.viewtool.core.TokenService;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.TableRowSorter;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.ToolTipManager;
import martin.viewtool.core.ManagementService;
import martin.viewtool.core.MediaRowRenderer;
import martin.viewtool.core.UIUtils;

/**
 *
 * @author cesar
 */
public class PanelManagement extends javax.swing.JPanel {

    private final PreferencesService prefService = new PreferencesService();

    private final LibraryService libraryService = new LibraryService(Path.of(prefService.getOutputDir().toString()));
    private final ViewToolApp jframe;
    private final TokenService tokenService;
    private final MediaService mediaService = new MediaService();
    private MediaTableModel tableModel;
    private final UIUtils utils = new UIUtils();
    private final ManagementService managementService = new ManagementService(libraryService, mediaService, prefService);

    private boolean listenerAdded = false;
    private String token;

    private TableRowSorter<MediaTableModel> tableSorter;
    private boolean isLiveSearchActive = false;

    private JPanel loadingPanel;

    // Uso algunos boolean tipo volatile para que todos procesos puedan ver si cambia la variable en el instante.
    private volatile boolean isLoadingOverlayVisible = false;
    private volatile boolean localReady = false;
    private volatile boolean networkReady = false;
    private volatile boolean initialLoadInProgress = false;
    private boolean hasBeenInitialized = false;

    private SwingWorker<List<Media>, Void> rebuildWorker;

    //Atributos para paginar la tabla
    private List<Media> fullMediaList = new java.util.ArrayList<>();
    private int currentPage = 1;
    private final int pageSize = 50;

    public PanelManagement(ViewToolApp jframe) {
        this.jframe = jframe;
        this.tokenService = jframe.getTokenService();
        initComponents();
        openingLabel.setFont(UIUtils.BOLD_FONT);
        agrupedIcon();
        //Para que el ToolTipText del JList desaparezca despues de 3 segundos.
        ToolTipManager.sharedInstance().setDismissDelay(3000);
        //Para que desaparezca el scroll horizontal
        listScrollPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        initLoadingPanel();
        token = tokenService.getToken();

        comboFilterListener();
        LiveSearchOnTable();

    }

    private void agrupedIcon() {
        buttonRefreshList.setIcon(utils.getFixedSizeIcon("refreshicon.png", 25, 25));
        buttonDeleteFile.setIcon(utils.getFixedSizeIcon("deleteicon.png", 25, 25));
        buttonDownloadFile.setIcon(utils.getFixedSizeIcon("downloadicon.png", 25, 25));
        buttonOpenFile.setIcon(utils.getFixedSizeIcon("playvideoicon.png", 25, 25));
        buttonRefreshTable.setIcon(utils.getFixedSizeIcon("refreshicon.png", 25, 25));
        buttonUploadFile.setIcon(utils.getFixedSizeIcon("uploadicon.png", 25, 25));
        titleListLabel.setIcon(utils.getFixedSizeIcon("libraryicon.png", 40, 40));
        buttonPrev.setIcon(utils.getFixedSizeIcon("backicon.png", 25, 25));
        buttonNext.setIcon(utils.getFixedSizeIcon("fronticon.png", 25, 25));

    }

    // Crea un panel opaco que muestra el cursor de 'cargando' en el raton
    private void initLoadingPanel() {
        loadingPanel = new JPanel();
        loadingPanel.setOpaque(false);
        loadingPanel.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        loadingPanel.setFocusable(true);
        loadingPanel.setEnabled(true);

        //Escucha y consume los eventos del raton para que no se propague a otros componentes
        MouseAdapter blocker = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                e.consume();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                e.consume();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                e.consume();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                e.consume();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                e.consume();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                e.consume();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                e.consume();
            }
        };

        loadingPanel.addMouseListener(blocker);
        loadingPanel.addMouseMotionListener(blocker);

        // Bloquea el scroll del raton
        loadingPanel.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                e.consume();
            }
        });

        //Bloquea el teclado para que no interactue con la app
        loadingPanel.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                e.consume();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                e.consume();
            }

            @Override
            public void keyTyped(KeyEvent e) {
                e.consume();
            }
        });
    }

    // Metodo que bloquea la pantalla del usuario mostrando el loadingPanel creado anteriormente.
    private void freezeUI() {
        if (isLoadingOverlayVisible) {
            return;
        }
        isLoadingOverlayVisible = true;

        Runnable showLoadingPanel = new Runnable() {
            @Override
            public void run() {
                JRootPane rootPane = SwingUtilities.getRootPane(PanelManagement.this);

                if (rootPane != null) {
                    rootPane.setGlassPane(loadingPanel);
                    loadingPanel.setVisible(true);
                    loadingPanel.requestFocusInWindow();
                }

                // Refuerza que aparezca la rueda de 'cargando' por si el raton no esta exactamente sobre el glassPane
                Window window = SwingUtilities.getWindowAncestor(PanelManagement.this);
                if (window != null) {
                    window.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                }
            }
        };

        //Uso el invokeLater para que el runnable 'showLoadingPanel' que se ejecute en el hilo EDT.
        SwingUtilities.invokeLater(showLoadingPanel);
    }

    //Metodo que desbloquea la pantalla y le quita la visibilidad al loadingPanel.
    private void unfreezeUI() {
        isLoadingOverlayVisible = false;

        Runnable showMainPanel = new Runnable() {
            @Override
            public void run() {
                Window window = javax.swing.SwingUtilities.getWindowAncestor(PanelManagement.this);
                if (window != null) {
                    window.setCursor(Cursor.getDefaultCursor());
                }
                if (loadingPanel != null) {
                    loadingPanel.setVisible(false);
                }
            }

        };

        //Uso el invokeLater para que el runnable 'showMainPanel' que se ejecute en el hilo EDT.
        SwingUtilities.invokeLater(showMainPanel);
    }

    //Metodo para comprobar que tanto los datos locales como los que provienen de la red estan cargados para descongelar la pantalla.
    private void checkSynchronization() {
        if (!initialLoadInProgress) {
            return;
        }
        if (localReady && networkReady) {
            initialLoadInProgress = false;
            unfreezeUI();
        }
    }

    //Metodo para hacer busqueda en la tabla en tiempo real tecla por tecla mediante el DocumentListener. Al detectar un cambio, llama al metodo que filtra los resultados.
    private void LiveSearchOnTable() {
        if (isLiveSearchActive) {
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

        isLiveSearchActive = true;
    }

    // Metodo que usa la TableRowSorter en el modelo de la tabla para filtrar el texto que hay en el TextField.
    private void applyTableFilter() {
        String text = textFieldFile.getText().toLowerCase();

        if (text.isBlank()) {
            //Si no hay texto muestra la lista completa (paginada con el metodo creado).
            tableModel.setFullList(fullMediaList);
        } else {
            //Cuando busca, busca en la lista completa y no solo en la paginacion que esta el usuario.
            List<Media> filtered = fullMediaList.stream()
                    .filter(m -> m.mediaFileName.toLowerCase().contains(text)
                    || (m.downloadedFromUrl != null && m.downloadedFromUrl.toLowerCase().contains(text)))
                    .toList();

            tableModel.setFullList(filtered);
            //Cambia el valor a 1 para que se muestre el resultado en la pagina principal de la lista.
            currentPage = 1;
        }

        updatePagination();
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
        if (!hasBeenInitialized) {
            hasBeenInitialized = true;
            initialLoad();
        } else {
            unfreezeUI();
        }

    }

    //Uso un Timer con valor 0 para que cargue los datos una vez se pinte el panel.
    private void initialLoad() {

        javax.swing.Timer timer = new javax.swing.Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                initialLoadInProgress = true;
                localReady = false;
                networkReady = false;

                freezeUI();
                refreshLocalList();
                refreshTableData(true);
                startPolling();
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    // Actualiza la lista local segun el item que ha elegido el usuario
    private void refreshLocalList() {
        try {
            String selected = (String) comboFilter.getSelectedItem();

            DefaultListModel<String> model = managementService.getUpdatedLocalListModel(selected);

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

                        if (initialLoadInProgress && !networkReady) {
                            networkReady = true;
                        }

                        if (newMedias != null && !newMedias.isEmpty()) {
                            applyNetworkUpdateTable(newMedias);
                        }

                        if (initialLoadInProgress) {
                            checkSynchronization();
                        }
                    }
                });
                listenerAdded = true;
            }

            mediaSyncPolling.setRunning(true);
            //He añadido un segundo intervalo despues del inicial para que la app no este actualizando de manera tan seguida y se quede pillada la app
            mediaSyncPolling.setPollingInterval(60);

        } catch (Exception ex) {
            Alerts.error(this, "Error: " + ex.getMessage());
        }
    }

    //Añade los archivos a la Tabla mediante el metodo 'addNewMediaNetwork' y redibuja la tabla.
    //Lo hace mediante un Timer para que no colapse la aplicacion y espere a que todos los eventos en cola hayan finalizado para inciar el metodo.
    private void applyNetworkUpdateTable(final List<Media> newMedia) {
        javax.swing.Timer timer = new javax.swing.Timer(0, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mediaService.addNewMediaNetwork(newMedia);
                refreshTableData(false);
            }
        });
        timer.setRepeats(false);
        timer.start();
    }

    private void refreshTableData(boolean isInitialLoad) {
        if (rebuildWorker != null && !rebuildWorker.isDone()) {
            rebuildWorker.cancel(true);
        }
        if (isInitialLoad) {
            freezeUI();
        }

        rebuildWorker = new SwingWorker<>() {

            @Override
            protected List<Media> doInBackground() {
                return mediaService.createMediaListCombined();
            }

            @Override
            protected void done() {
                try {
                    if (isCancelled()) {
                        return;
                    }
                    fullMediaList = get();
                    applyMediaTableModel(fullMediaList);

                    if (isInitialLoad) {
                        localReady = true;
                        checkSynchronization();
                    }

                } catch (Exception ex) {
                    Alerts.error(PanelManagement.this, "Error rebuilding table: " + ex.getMessage());
                    if (isInitialLoad) {
                        localReady = true;
                        checkSynchronization();
                    }
                }

            }

        };

        rebuildWorker.execute();
    }

    private void applyMediaTableModel(List<Media> mediaListCombined) {
        if (tableModel == null) {
            tableModel = new MediaTableModel(mediaListCombined, mediaService, currentPage, pageSize);
            tableFiles.setModel(tableModel);

            tableSorter = new TableRowSorter<>(tableModel);
            tableFiles.setRowSorter(tableSorter);

            MediaRowRenderer renderer = new MediaRowRenderer();
            for (int i = 0; i < tableFiles.getColumnCount(); i++) {
                tableFiles.getColumnModel().getColumn(i).setCellRenderer(renderer);
            }
            columnPrefs();
        } else {

            tableModel.setFullList(mediaListCombined);
            tableModel.updatePage(currentPage, pageSize);
        }

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

    private void updatePagination() {
        //Usa el Math.ceil para redondear hacia arriba y que ningun archivo quede fuera del conteo de paginacion.
        int totalItems = fullMediaList.size();
        double result = totalItems / pageSize;
        int totalPages = (int) Math.ceil(result);

        labelPageStatus.setText("Page " + currentPage + " of " + totalPages);

        buttonNext.setEnabled(currentPage < totalPages);
        buttonPrev.setEnabled(currentPage > 1);

        tableModel.updatePage(currentPage, pageSize);

        //Crea un runnable para que se ejecute en el Event Dispatch Thread (EDT) usando el invokeLater.
        Runnable scrollReset = new Runnable() {
            @Override
            public void run() {

                tableScrollPane.getVerticalScrollBar().setValue(0);
            }
        };
        SwingUtilities.invokeLater(scrollReset);
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
        listScrollPane = new javax.swing.JScrollPane();
        listFiles = new javax.swing.JList<>();
        buttonRefreshList = new javax.swing.JButton();
        comboFilter = new javax.swing.JComboBox<>();
        tableScrollPane = new javax.swing.JScrollPane();
        tableFiles = new javax.swing.JTable();
        buttonRefreshTable = new javax.swing.JButton();
        textFieldFile = new javax.swing.JTextField();
        labelSearchFile = new javax.swing.JLabel();
        buttonDeleteFile = new javax.swing.JButton();
        titleListLabel = new javax.swing.JLabel();
        titleTableLabel = new javax.swing.JLabel();
        buttonDownloadFile = new javax.swing.JButton();
        buttonUploadFile = new javax.swing.JButton();
        buttonOpenFile = new javax.swing.JButton();
        openingLabel = new javax.swing.JLabel();
        labelPageStatus = new javax.swing.JLabel();
        buttonPrev = new javax.swing.JButton();
        buttonNext = new javax.swing.JButton();

        panelManagement.setMaximumSize(new java.awt.Dimension(1920, 1080));
        panelManagement.setMinimumSize(new java.awt.Dimension(800, 600));
        panelManagement.setName(""); // NOI18N
        panelManagement.setLayout(null);

        listFiles.setToolTipText("Archivos descargados en: "+prefService.getOutputDir().toString());
        listFiles.setSelectionBackground(new java.awt.Color(153, 153, 153));
        listFiles.setSelectionForeground(new java.awt.Color(255, 255, 255));
        listScrollPane.setViewportView(listFiles);

        panelManagement.add(listScrollPane);
        listScrollPane.setBounds(10, 50, 550, 420);

        buttonRefreshList.setBackground(new java.awt.Color(255, 255, 255));
        buttonRefreshList.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/refreshicon.png"))); // NOI18N
        buttonRefreshList.setBorder(null);
        buttonRefreshList.setBorderPainted(false);
        buttonRefreshList.setPreferredSize(new java.awt.Dimension(40, 40));
        buttonRefreshList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRefreshListActionPerformed(evt);
            }
        });
        panelManagement.add(buttonRefreshList);
        buttonRefreshList.setBounds(10, 470, 40, 40);

        comboFilter.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Videos", "Audios" }));
        comboFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboFilterActionPerformed(evt);
            }
        });
        panelManagement.add(comboFilter);
        comboFilter.setBounds(480, 470, 80, 20);

        tableFiles.setAutoCreateRowSorter(true);
        tableFiles.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tableFiles.setSelectionBackground(new java.awt.Color(153, 153, 153));
        tableFiles.setSelectionForeground(new java.awt.Color(255, 255, 255));
        tableFiles.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tableScrollPane.setViewportView(tableFiles);

        panelManagement.add(tableScrollPane);
        tableScrollPane.setBounds(580, 50, 950, 420);

        buttonRefreshTable.setBackground(new java.awt.Color(255, 255, 255));
        buttonRefreshTable.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/refreshicon.png"))); // NOI18N
        buttonRefreshTable.setBorder(null);
        buttonRefreshTable.setBorderPainted(false);
        buttonRefreshTable.setPreferredSize(new java.awt.Dimension(40, 40));
        buttonRefreshTable.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRefreshTableActionPerformed(evt);
            }
        });
        panelManagement.add(buttonRefreshTable);
        buttonRefreshTable.setBounds(580, 470, 50, 50);

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
        textFieldFile.setBounds(1150, 20, 380, 22);

        labelSearchFile.setBackground(new java.awt.Color(255, 255, 255));
        labelSearchFile.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        labelSearchFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/lupaicon.png"))); // NOI18N
        panelManagement.add(labelSearchFile);
        labelSearchFile.setBounds(1110, 15, 30, 25);

        buttonDeleteFile.setBackground(new java.awt.Color(255, 255, 255));
        buttonDeleteFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/deleteicon.png"))); // NOI18N
        buttonDeleteFile.setBorder(null);
        buttonDeleteFile.setBorderPainted(false);
        buttonDeleteFile.setPreferredSize(new java.awt.Dimension(40, 40));
        buttonDeleteFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDeleteFileActionPerformed(evt);
            }
        });
        panelManagement.add(buttonDeleteFile);
        buttonDeleteFile.setBounds(840, 470, 50, 50);

        titleListLabel.setBackground(new java.awt.Color(255, 255, 255));
        titleListLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        titleListLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/libraryicon.png"))); // NOI18N
        titleListLabel.setText("Media downloaded:");
        titleListLabel.setPreferredSize(new java.awt.Dimension(40, 40));
        panelManagement.add(titleListLabel);
        titleListLabel.setBounds(10, 10, 170, 40);

        titleTableLabel.setBackground(new java.awt.Color(255, 255, 255));
        titleTableLabel.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        titleTableLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/databaseicon.png"))); // NOI18N
        titleTableLabel.setText("Media network library:");
        titleTableLabel.setPreferredSize(new java.awt.Dimension(40, 40));
        panelManagement.add(titleTableLabel);
        titleTableLabel.setBounds(580, 10, 180, 40);

        buttonDownloadFile.setBackground(new java.awt.Color(255, 255, 255));
        buttonDownloadFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/downloadicon.png"))); // NOI18N
        buttonDownloadFile.setBorder(null);
        buttonDownloadFile.setBorderPainted(false);
        buttonDownloadFile.setPreferredSize(new java.awt.Dimension(40, 40));
        buttonDownloadFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonDownloadFileActionPerformed(evt);
            }
        });
        panelManagement.add(buttonDownloadFile);
        buttonDownloadFile.setBounds(940, 470, 40, 50);

        buttonUploadFile.setBackground(new java.awt.Color(255, 255, 255));
        buttonUploadFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/uploadicon.png"))); // NOI18N
        buttonUploadFile.setBorder(null);
        buttonUploadFile.setBorderPainted(false);
        buttonUploadFile.setPreferredSize(new java.awt.Dimension(40, 40));
        buttonUploadFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonUploadFileActionPerformed(evt);
            }
        });
        panelManagement.add(buttonUploadFile);
        buttonUploadFile.setBounds(750, 470, 50, 50);

        buttonOpenFile.setBackground(new java.awt.Color(255, 255, 255));
        buttonOpenFile.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/playvideoicon.png"))); // NOI18N
        buttonOpenFile.setBorder(null);
        buttonOpenFile.setBorderPainted(false);
        buttonOpenFile.setPreferredSize(new java.awt.Dimension(40, 40));
        buttonOpenFile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonOpenFileActionPerformed(evt);
            }
        });
        panelManagement.add(buttonOpenFile);
        buttonOpenFile.setBounds(670, 470, 40, 50);

        openingLabel.setText("             ");
        panelManagement.add(openingLabel);
        openingLabel.setBounds(1000, 490, 200, 16);

        labelPageStatus.setText("Page 1 of ...");
        panelManagement.add(labelPageStatus);
        labelPageStatus.setBounds(1440, 480, 90, 20);

        buttonPrev.setBackground(new java.awt.Color(255, 255, 255));
        buttonPrev.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/backicon.png"))); // NOI18N
        buttonPrev.setBorder(null);
        buttonPrev.setBorderPainted(false);
        buttonPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPrevActionPerformed(evt);
            }
        });
        panelManagement.add(buttonPrev);
        buttonPrev.setBounds(1360, 480, 30, 20);

        buttonNext.setBackground(new java.awt.Color(255, 255, 255));
        buttonNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/fronticon.png"))); // NOI18N
        buttonNext.setBorder(null);
        buttonNext.setBorderPainted(false);
        buttonNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonNextActionPerformed(evt);
            }
        });
        panelManagement.add(buttonNext);
        buttonNext.setBounds(1390, 480, 30, 20);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelManagement, javax.swing.GroupLayout.DEFAULT_SIZE, 1560, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelManagement, javax.swing.GroupLayout.PREFERRED_SIZE, 527, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 107, Short.MAX_VALUE))
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
        refreshTableData(false);
    }//GEN-LAST:event_buttonRefreshTableActionPerformed

    private void buttonDeleteFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDeleteFileActionPerformed

        Media file = getSelectedMedia("Delete");
        if (file == null) {
            return;
        }

        try {
            if (managementService.deleteMedia(file)) {
                Alerts.info(this, "File deleted successfully.");
                refreshLocalList();
            } else {
                Alerts.error(this, "File is on network, could not be deleted.");
            }
        } catch (Exception ex) {
            Alerts.error(this, "Error deleting file: " + ex.getMessage());
        }

    }//GEN-LAST:event_buttonDeleteFileActionPerformed

    private void buttonDownloadFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonDownloadFileActionPerformed
        Media file = getSelectedMedia("Download");
        if (file == null) {
            return;
        }

        try {
            managementService.downloadMedia(file, jframe.getComponent(), token);
            Alerts.info(this, "File successfully downloaded to " + prefService.getOutputDir().toString());
            refreshLocalList();
        } catch (Exception ex) {
            Alerts.error(this, "Error: " + ex.getMessage());
        }
    }//GEN-LAST:event_buttonDownloadFileActionPerformed

    private void buttonUploadFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonUploadFileActionPerformed
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile();

            String youtubeUrl = JOptionPane.showInputDialog(this, "Enter the YouTube URL:");
            if (youtubeUrl == null || youtubeUrl.trim().isEmpty()) {
                return;
            }

            try {
                managementService.uploadMedia(selectedFile, youtubeUrl.trim(), jframe.getComponent(), token);
                Alerts.info(this, "File uploaded:\n" + selectedFile.getAbsolutePath());
            } catch (Exception ex) {
                Alerts.error(this, ex.getMessage());
            }
        }
    }//GEN-LAST:event_buttonUploadFileActionPerformed

    private void buttonOpenFileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonOpenFileActionPerformed
        Media file = getSelectedMedia("Open");
        if (file == null) {
            return;
        }
        openingLabel.setText("Opening...");

        SwingWorker<Void, Void> openFileWorker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                managementService.openLocalFile(file);
                return null;
            }

            @Override
            protected void done() {

                try {
                    get();
                } catch (InterruptedException | ExecutionException ex) {
                    Alerts.error(null, "This media is not downloaded in the local directory.");
                } finally {
                    openingLabel.setText("");
                }
            }
        };

        openFileWorker.execute();
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

    private void buttonNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonNextActionPerformed
        double totalItems = fullMediaList.size();
        double result = totalItems / pageSize;
        int totalPages = (int) Math.ceil(result);

        if (currentPage < totalPages) {
            currentPage++;
            updatePagination();
        }
    }//GEN-LAST:event_buttonNextActionPerformed

    private void buttonPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPrevActionPerformed
        if (currentPage > 1) {
            currentPage--;
            updatePagination();
        }
    }//GEN-LAST:event_buttonPrevActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton buttonDeleteFile;
    private javax.swing.JButton buttonDownloadFile;
    private javax.swing.JButton buttonNext;
    private javax.swing.JButton buttonOpenFile;
    private javax.swing.JButton buttonPrev;
    private javax.swing.JButton buttonRefreshList;
    private javax.swing.JButton buttonRefreshTable;
    private javax.swing.JButton buttonUploadFile;
    private javax.swing.JComboBox<String> comboFilter;
    private javax.swing.JLabel labelPageStatus;
    private javax.swing.JLabel labelSearchFile;
    private javax.swing.JList<String> listFiles;
    private javax.swing.JScrollPane listScrollPane;
    private javax.swing.JLabel openingLabel;
    private javax.swing.JPanel panelManagement;
    private javax.swing.JTable tableFiles;
    private javax.swing.JScrollPane tableScrollPane;
    private javax.swing.JTextField textFieldFile;
    private javax.swing.JLabel titleListLabel;
    private javax.swing.JLabel titleTableLabel;
    // End of variables declaration//GEN-END:variables
}
