/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package martin.viewtool.core;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import MediaSyncPolling.Media;
import MediaSyncPolling.MediaSyncPolling;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.swing.SwingUtilities;

/**
 *
 * @author cesar
 */
public final class MediaTableModel extends AbstractTableModel {

    private List<Media> allFiles;      // Todos los datos de la API
    private List<Media> currentView;   // Solo los datos de la página actual
    private final String[] columns = {"Location", "Name", "URL", "Nick"};
    private final Path downloadPath;

    private final Map<Integer, String> userNames = new ConcurrentHashMap<>();
    private final MediaSyncPolling mediaSyncPolling;
    private final String token;

    public MediaTableModel(List<Media> allFiles, MediaService mediaService, int page, int pageSize, MediaSyncPolling mediaSyncPolling, String token) {
        this.allFiles = allFiles;
        this.downloadPath = mediaService.getDownloadBaseDir();
        this.mediaSyncPolling = mediaSyncPolling;
        this.token = token;
        updatePage(page, pageSize);
    }

    public void setList(List<Media> newList) {
        this.allFiles = newList;
    }
    
    public Map<Integer, String> getUserNames() {
        return userNames;
    }

    public void updatePage(int page, int pageSize) {
        //Calcula cual indice de la lista comienza en la pagina actual.
        int fromIndex = (page - 1) * pageSize;
        // Maneja el final con el Math.min para que la pagina no de error si los archivos son menos que el tamaño de la pagina.
        int toIndex = Math.min(fromIndex + pageSize, allFiles.size());

        //crea una vista mediante de la lista desde fromIndex hasta toIndex
        if (fromIndex < allFiles.size()) {
            this.currentView = allFiles.subList(fromIndex, toIndex);
        } else {
            //devuelve una lista vacia si no hay mas archivos
            this.currentView = new ArrayList<>();
        }
        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return currentView.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return switch (columnIndex) {
            case 0 ->
                String.class;
            case 1 ->
                String.class;
            case 2 ->
                String.class;
            case 3 ->
                String.class;
            default ->
                Object.class;
        };
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        //Protege al sistema si la lista de la pagina actual es null  o si la fila es mayor al tamaño de la pagina que esta ejecutandose.
        if (currentView == null || rowIndex >= currentView.size()) {
            return "";
        }

        Media file = currentView.get(rowIndex);

        return switch (columnIndex) {
            case 0 ->
                resolveLocation(file);
            case 1 ->
                file.mediaFileName;
            case 2 ->
                file.downloadedFromUrl;
            case 3 ->
                resolveNickName(file.userId);
            default ->
                "";
        };
    }

    private String resolveNickName(int userId) {
        if (userNames.containsKey(userId)) {
            return userNames.get(userId);
        }
        //Mientras la app busca el nick, aparece Cargando... en la tabla.

        userNames.put(userId, "Cargando...");

        //Un hilo en segundo plano que ejecuta la busqueda y guardado de los nombres de usuario. 
        Thread hiloBusqueda = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    String nickName = mediaSyncPolling.getNickName(userId, token);
                    //Clave-Valor
                    userNames.put(userId, nickName);

                } catch (Exception e) {
                    // En caso de no encontrar el nombre, rescribe con el user ID.
                    userNames.put(userId, "Not found. ( USER ID: " + userId + ")");
                }

                Runnable fireTableRunnable = new Runnable() {
                    @Override
                    public void run() {
                        fireTableDataChanged();
                    }
                };

                // Usa el hilo EDT para actualizar la tabla.
                SwingUtilities.invokeLater(fireTableRunnable);
            }
        });

        hiloBusqueda.start();

        return userNames.get(userId);
    }

    private String resolveLocation(Media media) {

        Path localPath = downloadPath.resolve(media.mediaFileName);

        boolean existsLocal = Files.exists(localPath);
        boolean existsNetwork = (media.id > 0);

        if (existsLocal && existsNetwork) {
            return "Both";
        }
        if (existsLocal) {
            return "Local";
        }
        if (existsNetwork) {
            return "Network";
        }
        return "None";
    }

    public Media getFile(int row) {
        return currentView.get(row);
    }
}
