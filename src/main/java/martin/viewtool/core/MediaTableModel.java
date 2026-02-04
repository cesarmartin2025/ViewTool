/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package martin.viewtool.core;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import MediaSyncPolling.Media;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 *
 * @author cesar
 */
public final class MediaTableModel extends AbstractTableModel {

    private List<Media> allFiles;      // Todos los datos de la API
    private List<Media> currentView;   // Solo los datos de la página actual
    private final String[] columns = {"Location", "Name", "URL"};
    private final Path downloadPath;

    public MediaTableModel(List<Media> allFiles, MediaService mediaService, int page, int pageSize) {
        this.allFiles = allFiles;
        this.downloadPath = mediaService.getDownloadBaseDir();
        updatePage(page, pageSize);
    }

    public void setFullList(List<Media> newList) {
        this.allFiles = newList;
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
            default ->
                Object.class;
        };
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        //Protege al sistema si la lista de la pagina actual es null  o si la fila es mayor a la pagina que esta ejecutandose.
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
            default ->
                "";
        };
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
